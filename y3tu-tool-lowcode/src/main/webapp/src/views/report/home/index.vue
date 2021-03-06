<template>
    <el-container>
        <el-main>
            <el-tabs>
                <el-tab-pane>

                    <template #label>
                        <span><i class="el-icon-edit" style="padding-right: 5px"/>报表列表</span>
                    </template>

                    <div class="main-container" style="padding: 0">
                        <div class="head-container">
                            <label class="form-item-label">报表名称</label>
                            <el-input clearable v-model="pageInfo.entity.name" placeholder="请输入报表名称" style="width:200px"
                                      class="form-item"
                                      @keyup.enter="query"/>
                            <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain
                                       @click="query">
                                查询
                            </el-button>
                            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain
                                       @click="reset">
                                重置
                            </el-button>
                        </div>

                        <el-divider/>

                        <div v-loading="pageInfo.pageLoading" style="display: flex;flex-wrap: wrap;">

                            <div class="excel-view-item excel-list-add" @click="createReport">
                                <i class="el-icon-plus excel-list-add-icon"></i>
                            </div>

                            <div v-for="(item,index) in pageInfo.records"
                                 :key="index"
                                 class="excel-view-item"
                                 @mouseover="item.editable=true"
                                 @mouseout="item.editable=false">

                                <!--缩略图-->
                                <div class="thumb">
                                    <img :src="excelImg"/>
                                    <div v-loading="itemLoading" class="excel-edit-container" v-show="item.editable">
                                        <a @click="updateReport(item)">
                                            修改
                                        </a>
                                    </div>
                                </div>

                                <!--底部-->
                                <div class="item-footer">
                                    <span class="item-name">{{ item.name }}</span>
                                    <div style="margin-left: 10%;">
                                        <a class="opt-show">
                                            <el-tooltip content="预览报表" placement="top">
                                                <i @click="preview(item)" class="el-icon-view"
                                                   style="font-size: 16px"></i>
                                            </el-tooltip>
                                        </a>
                                        <a class="opt-show">
                                            <el-tooltip content="删除报表" placement="top">
                                                <i @click="del(item)" class="el-icon-delete"
                                                   style="font-size: 16px"></i>
                                            </el-tooltip>
                                        </a>
                                        <a class="opt-show">
                                          <el-tooltip content="报表跳转" placement="top">
                                            <i @click="goPublish(item)" class="el-icon-s-promotion" style="font-size: 16px"></i>
                                          </el-tooltip>
                                        </a>
                                        <a class="opt-show">
                                            <el-tooltip content="复制链接" placement="top">
                                                <i @click="copyLink(item)" class="el-icon-copy-document" style="font-size: 16px"></i>
                                            </el-tooltip>
                                        </a>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <el-pagination
                                class="page"
                                @size-change="sizeChange"
                                @current-change="pageChange"
                                :current-page="pageInfo.current"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="pageInfo.pageSize"
                                layout="total, sizes, prev, pager, next, jumper"
                                :total="pageInfo.total">
                        </el-pagination>
                    </div>
                </el-tab-pane>
                <el-tab-pane>
                    <template #label>
                        <span><i class="el-icon-guide" style="padding-right: 5px"/>模板案例</span>
                    </template>
                    模板案例
                </el-tab-pane>
            </el-tabs>
        </el-main>

        <el-drawer
                size="90%"
                title="报表配置"
                v-model="drawer"
                direction="rtl"
                destroy-on-close>
            <editor v-bind:value="report" @success="editorSuccess"/>
        </el-drawer>


        <el-dialog
                v-model="previewDialog"
                width="80%"
                destroy-on-close
                center>
            <preview v-bind:value="report"/>
        </el-dialog>

    </el-container>
</template>

<script>

    import {page, get, del} from './api'

    import editor from './editor'
    import preview from "./preview";
    import excelImg from '@/assets/images/excel.jpg'

    export default {
        name: 'reportList',
        components: {editor, preview},
        data() {
            return {
                excelImg:excelImg,
                pageInfo: {
                    entity: {
                        name: ''
                    },
                    pageLoading: false,
                    current: 1,
                    total: 0,
                    pageSize: 10,
                    records: [],
                },
                drawer: false,
                previewDialog: false,
                report: {},
                itemLoading: false
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
                this.pageInfo.entity.name = '';
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
            createReport() {
                this.drawer = true;
                this.report = {
                    id: '',
                    name: '',
                    type: '',
                    tableHeader: '',
                    querySql: '',
                    dsId: '',
                    status: '',
                    templateType: '',
                    params: [],
                }
            },
            updateReport(item) {
                this.itemLoading = true;
                get(item.id).then(res => {
                    this.itemLoading = false;
                    this.report = res.data;
                    this.drawer = true;
                })
            },
            editorSuccess() {
                this.drawer = false;
                this.query()
            },
            del(item) {
                this.$confirm('选中数据将被永久删除, 是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    del(item.id).then(() => {
                        this.$toast('删除成功', 'success', 3000);
                        this.query()
                    })
                })
            },
            preview(item) {
                this.itemLoading = true;
                get(item.id).then(res => {
                    this.itemLoading = false;
                    this.report = res.data;
                    this.previewDialog = true;
                })
            },
            goPublish(item){
              let url = window.location.href
              let index = url.indexOf('report');
              let prefix  = url.substr(0,index);
              let to = prefix +"report/publish?name="+item.name;
              window.open(to, "_blank");
            },
            copyLink(item){
              let _this = this;
              let url = window.location.href
              let index = url.indexOf('report');
              let prefix  = url.substr(0,index);
              let to = prefix +"report/publish?name="+item.name;
              this.$copyText(to).then(function () {
                _this.$notify({
                  title: '复制成功',
                  type: 'success'
                });
              }, function () {
                _this.$notify({
                  title: '复制失败',
                  type: 'warning'
                });
              })
            }
        }
    }
</script>

<style>
    .page {
        display: flex;
        justify-content: center;
        -webkit-box-pack: center;
    }

    .excel-list-add {
        height: 184px;
        width: 258px;
        border: 1px solid #00baff;
        font-size: 14px;
        color: #8eeeff;
        background-image: linear-gradient(-90deg, rgba(0, 222, 255, .39) 0, rgba(0, 174, 255, .19) 100%);
        box-shadow: 0 0 10px 0 rgba(55, 224, 255, .3);
        cursor: pointer;
    }

    .excel-list-add-icon {
        height: 100%;
        color: black;
        flex-direction: column;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .excel-view-item {
        position: relative;
        margin: 16px;
        display: flex;
        flex-direction: column;
        width: 260px;
        height: 170px;
        border: 1px solid #3a4659;
        overflow: hidden;
    }

    .excel-view-item:hover {
        box-shadow: 0 0 20px 0 #000;
        border: 1px solid #00baff;
    }

    .excel-view-item .thumb {
        position: relative;
        height: calc(100% - 36px);
    }

    .excel-view-item .thumb img {
        width: 100%;
        height: 100%
    }

    .excel-view-item .item-footer {
        font-size: 12px;
        width: 100%;
        height: 36px;
        display: flex;
        align-items: center;
        position: absolute;
        bottom: 0;
        justify-content: space-between;
        background: #1d262e;
        box-sizing: border-box;
        padding: 0 10px;
        color: #bcc9d4;
    }

    .excel-view-item .item-name {
        width: 100px;
        padding: 0 5px;
        line-height: 28px;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        border: 1px solid transparent;
    }

    .opt-show {
        color: #bcc9d4;
        margin-right: 10px;
        cursor: pointer;
    }

    .excel-edit-container {
        position: absolute;
        top: 0;
        left: 0;
        background-color: rgba(29, 38, 46, 0.8);
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .excel-edit-container a {
        display: inline-block;
        vertical-align: middle;
        height: 32px;
        line-height: 32px;
        padding: 0 30px;
        box-sizing: border-box;
        outline: 0;
        text-align: center;
        font-size: 14px;
        background-image: linear-gradient(-225deg, #00d3f1 0, #12b3ff 100%);
        color: #293f52;
        border: none;
        transition: .3s ease;
        cursor: pointer;
    }

    /*1.显示滚动条：当内容超出容器的时候，可以拖动：*/
    .el-drawer__body {
        overflow: auto;
    }

    /*2.隐藏滚动条，太丑了*/
    .el-drawer__container ::-webkit-scrollbar {
        display: none;
    }
</style>