<template>
    <div class="main-container">
        <div class="head-container">
            <el-row>
                <el-col :span="8" v-for="param in report.params" :key="param.id">
                    <el-tooltip class="item" effect="dark" :content="param.name" placement="top-start">
                        <label style="width:30%" class="form-item-label">{{ param.name }}</label>
                    </el-tooltip>
                    <label>
                        <el-input v-if="param.type===1" clearable
                                  style="width:53%"
                                  v-model="param.value"
                                  class="form-item"
                                  @keyup.enter="query"/>
                        <dict-select v-if="param.type===2||param.type===3"
                                     :mode="param.type===2?'single':'multiple'"
                                     :style="'width:53%'"
                                     :dict-code="param.dictCode"
                                     v-model:value="param.value"/>
                        <el-date-picker
                                v-if="param.type===4"
                                v-model="param.value"
                                type="month"
                                class="form-item"
                                placeholder="选择月">
                        </el-date-picker>
                        <el-date-picker
                                v-if="param.type===5"
                                v-model="param.value"
                                type="date"
                                class="form-item"
                                placeholder="选择日期">
                        </el-date-picker>
                        <el-date-picker
                                v-if="param.type===6"
                                v-model="param.value"
                                type="datetime"
                                class="form-item"
                                placeholder="选择日期时间">
                        </el-date-picker>
                    </label>
                </el-col>
            </el-row>

            <el-row type="flex" justify="end">
                <el-col :span="7">
                    <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain @click="query">
                        查询
                    </el-button>
                    <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain
                               @click="reset">
                        重置
                    </el-button>
                    <el-button class="form-item" size="mini" type="primary" icon="el-icon-download" plain
                               :loading="exportLoading"
                               @click="exportData">
                        导出Excel
                    </el-button>
                </el-col>
            </el-row>

        </div>

        <el-divider/>

        <!--分页组件-->
        <el-pagination
                :total="pageInfo.total"
                :page-size="pageInfo.pageSize"
                :current-page="pageInfo.current"
                style="margin-top: 8px;"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="sizeChange"
                @current-change="pageChange"/>

        <el-divider/>

        <!--通用报表才展示报表头-->
        <merge-header-table
                v-if="report.type==='common'"
                :header="JSON.parse(report.tableHeader)"
                :loading="pageInfo.pageLoading"
                :table-data="pageInfo.records"/>

        <div v-loading="pageInfo.pageLoading" v-if="report.type==='jasper'" v-html="jasperHtml"
             style="width: 100%;overflow-x: auto">

        </div>

    </div>
</template>

<script>

    import {reportHtml, isBigData, createReportDownload, exportExcel} from './api'
    import dictSelect from "../dict/dictSelect";
    import mergeHeaderTable from './mergeHeaderTable'

    export default {
        name: "preview",
        components: {dictSelect, mergeHeaderTable},
        props: {
            value: {
                type: Object
            },
        },
        data() {
            return {
                pageInfo: {
                    pageLoading: false,
                    current: 1,
                    total: 0,
                    pageSize: 10,
                    records: [],
                },
                jasperHtml: '',
                exportLoading: false
            }
        },
        computed: {
            report() {
                return this.value;
            }
        },
        methods: {
            query() {
                this.report.pageInfo = this.pageInfo;
                this.pageInfo.pageLoading = true;
                reportHtml(this.report).then(res => {
                    if (res) {
                        if (this.report.type === 'common') {
                            this.pageInfo.records = res.data.records;
                            this.pageInfo.total = res.data.total;
                            this.pageInfo.pageLoading = false;
                        } else if (this.report.type === 'jasper') {
                            this.jasperHtml = res.data.html;
                            this.pageInfo.total = res.data.pageInfo.total;
                            this.pageInfo.pageLoading = false;
                        }
                    }
                }).catch(() => {
                    this.pageInfo.pageLoading = false;
                })
            },
            reset() {
                this.report.params.forEach((param) => {
                    param.value = '';
                })
            },
            exportData() {
                isBigData(this.report).then(res => {
                    if (res.data) {
                        this.exportLoading = true;
                        //大数据量报表写入报表生成日志
                        // 如果报表已经生成好，直接获取到报表下载链接开始下载报表；如果未生成好，跳转到报表下载页面
                        createReportDownload(this.report).then(() => {
                          this.exportLoading = false;
                          this.$toast('正在生成导出文件，请稍后在报表下载页面查看报表生成情况并下载！', 'success', 5000)
                        })
                    } else {
                        this.exportLoading = true;
                        let call = exportExcel(this.report, this.report.name + ".xls");
                        call.then(() => {
                            this.exportLoading = false;
                        })
                        this.$toast('正在生成导出文件，请稍后！', 'success', 5000)
                    }
                })
            },
            sizeChange(e) {
                this.pageInfo.current = 1;
                this.pageInfo.size = e;
                this.query()
            },
            pageChange(e) {
                this.pageInfo.current = e;
                this.query()
            }
        }
    }
</script>

<style>

</style>