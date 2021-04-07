<template>
    <div class="main-container">

        <div class="head-container">
            <label class="form-item-label">报表名称</label>
            <el-input clearable v-model="pageInfo.params.reportName" placeholder="请输入名称" style="width:200px"
                      class="form-item"
                      @keyup.enter="query"/>
            <label class="form-item-label">创建时间</label>
            <el-date-picker
                    size="medium"
                    v-model="pageInfo.params.createTimes"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
            </el-date-picker>
            <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain @click="query">
                查询
            </el-button>
            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain @click="reset">
                重置
            </el-button>
        </div>

        <el-divider/>

        <el-table
                ref="table"
                border
                size="mini"
                v-loading="pageInfo.pageLoading"
                :data="pageInfo.records"
                style="width: 100%;">
            <el-table-column label="报表名称" prop="name" align="center" show-overflow-tooltip min-width="120px">
                <template #default="scope">
                    <span>{{ scope.row.reportName }}</span>
                </template>
            </el-table-column>
            <el-table-column label="生成状态" :formatter="statusFormatter" :show-overflow-tooltip="true" align="center"
                             min-width="150px">
            </el-table-column>
            <el-table-column label="查询参数" align="center" show-overflow-tooltip min-width="200px">
                <template #default="scope">
                    <span>{{ scope.row.paramJson }}</span>
                </template>
            </el-table-column>
            <el-table-column label="下载次数" align="center" min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.downloadTimes }}</span>
                </template>
            </el-table-column>
            <el-table-column label="错误描述" align="center" min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.errMsg }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="100px">
                <template #default="scope">
                    <i class="el-icon-download table-operation" style="color: #2db7f5;"
                       @click="download(scope.row)"/>
                    <i class="el-icon-refresh table-operation" style="color: #2db7f5;"
                       @click="handleAgain(scope.row)"/>
                </template>
            </el-table-column>
        </el-table>

        <!--分页组件-->
        <el-pagination
                :total="pageInfo.total"
                :page-size="pageInfo.pageSize"
                :current-page="pageInfo.current"
                style="margin-top: 8px;"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="sizeChange"
                @current-change="pageChange"/>

    </div>
</template>

<script>


    import {page, downloadFile, handleAgain} from './api'

    export default {
        name: 'download',
        data() {
            return {
                pageInfo: {
                    params: {
                        reportName: '',
                        createTimes: []
                    },
                    pageLoading: false,
                    current: 1,
                    total: 0,
                    pageSize: 10,
                    records: [],
                }
            }
        },
        created() {
            this.$nextTick(() => {
                this.query();
            })
        },
        methods: {
            query() {
                this.pageInfo.pageLoading = true
                page(this.pageInfo).then(res => {
                    this.pageInfo.pageLoading = false
                    this.pageInfo = res.data;
                }).catch(() => {
                    this.pageInfo.pageLoading = false
                })
            },
            reset() {
                this.pageInfo.params = {
                    reportName: '',
                    createTimes: []
                }
            },
            sizeChange(e) {
                this.pageInfo.current = 1;
                this.pageInfo.size = e;
                this.query()
            },
            pageChange(e) {
                this.pageInfo.current = e;
                this.query()
            },
            statusFormatter(row) {
                switch (row.status) {
                    case '00W':
                        return '待生成'
                    case '00B':
                        return '正在生成中'
                    case '00A':
                        return '可下载'
                    case '00X':
                        return '失效'
                    case '00E':
                        return '异常'
                    default:
                        return '其他'
                }
            },
            download(row) {
                if (row.status != '00A') {
                    this.$toast('报表还不能下载，请稍后再试！', 'warning', 3000);
                    return
                }
                this.$toast('开始下载，请稍后！', '', 3000);
                downloadFile(row.id, row.reportName + ".xlsx");
            },
            handleAgain(row) {
                this.$toast('重新生成报表！', '', 3000);
                handleAgain(row.id).then(()=>{
                    this.query()
                });
            }
        }
    }
</script>

<style>

</style>