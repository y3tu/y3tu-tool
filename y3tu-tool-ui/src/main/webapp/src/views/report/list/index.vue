<template>
    <el-container>
        <el-aside style="width: 200px">
            <h2 class="title">报表设计器</h2>
            <el-menu>
                <el-submenu index="1">
                    <template #title>报表管理</template>
                    <el-menu-item-group>
                        <el-menu-item index="1-1">打印设计</el-menu-item>
                        <el-menu-item index="1-2">数据报表</el-menu-item>
                        <el-menu-item index="1-3">图形报表</el-menu-item>
                    </el-menu-item-group>
                </el-submenu>
            </el-menu>
        </el-aside>
        <el-main>
            <el-tabs>
                <el-tab-pane>
                    <template #label>
                        <span><i class="el-icon-edit"></i>报表设计</span>
                    </template>

                    <el-pagination
                            class="page"
                            @size-change="sizeChange"
                            @current-change="pageChange"
                            :current-page="currentPage"
                            :page-sizes="[10, 20, 30, 40]"
                            :page-size="pageSize"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="total">
                    </el-pagination>

                    <div style="display: flex;flex-wrap: wrap;">

                        <div class="excel-view-item excel-list-add">
                            <a @click="createExcel">
                                <i class="el-icon-plus" style="font-size:20px; padding-bottom: 5px;"></i>
                                <p style="letter-spacing: 2px;font-size: 14px;">新建报表</p>
                            </a>
                        </div>


                        <div v-for="(item,index) in reportList"
                             :key="index"
                             class="excel-view-item"
                             @mouseover="item.editable=true"
                             @mouseout="item.editable=false">

                            <!--缩略图-->
                            <div class="thumb">
                                <img src="../../../assets/images/excel.jpg"/>
                                <div class="excel-edit-container" v-show="item.editable">
                                    <a :href="getExcelEditUrl(item)" target="_blank">
                                        设计
                                    </a>
                                </div>
                            </div>

                            <!--底部-->
                            <div class="item-footer">
                                <span class="item-name">{{ item.name }}</span>
                                <div style="margin-left: 20%;">
                                    <a class="opt-show" :href="getExcelViewUrl(item)" target="_blank">
                                        <el-tooltip content="预览模板" placement="top">
                                            <i class="el-icon-view" style="font-size: 16px"></i>
                                        </el-tooltip>
                                    </a>
                                    <a class="opt-show">
                                        <el-tooltip content="收藏模板" placement="top">
                                            <i class="el-icon-folder-add" style="font-size: 16px"></i>
                                        </el-tooltip>
                                    </a>
                                    <a class="opt-show">
                                        <el-tooltip content="删除模板" placement="top">
                                            <i class="el-icon-delete" style="font-size: 16px"></i>
                                        </el-tooltip>
                                    </a>
                                    <a class="opt-show">
                                        <el-tooltip content="复制模板" placement="top">
                                            <i class="el-icon-copy-document" style="font-size: 16px"></i>
                                        </el-tooltip>
                                    </a>
                                </div>
                            </div>

                        </div>
                    </div>

                </el-tab-pane>
                <el-tab-pane>
                    <template #label>
                        <span><i class="el-icon-guide"></i>模板案例</span>
                    </template>
                    模板案例
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>
</template>

<script>
    export default {
        name: 'reportList',
        data() {
            return {
                // 当前页码
                currentPage: 1,
                // 总条数
                total: 0,
                // 每页的数据条数
                pageSize: 10,
                reportList: [{
                    editable: false,
                    name: 'test'
                }],
            }
        },
        methods: {
            sizeChange(val) {
                this.currentPage = 1;
                this.pageSize = val;
            },
            pageChange(val) {
                this.currentPage = val;
            },
            createExcel(){

            },
            getExcelEditUrl() {

            },
            getExcelViewUrl() {

            }
        }
    }
</script>

<style>
    .title {
        font-size: 20px;
        text-align: center;
        line-height: 60px;
        font-weight: 500;
    }

    .page {
        display: flex;
        justify-content: center;
        -webkit-box-pack: center;
    }

    .excel-list-add{
        height: 184px;
        width: 258px;
        border: 1px solid #00baff;
        font-size: 14px;
        color: #8eeeff;
        background-image: linear-gradient(-90deg, rgba(0, 222, 255, .39) 0, rgba(0, 174, 255, .19) 100%);
        box-shadow: 0 0 10px 0 rgba(55, 224, 255, .3);
        cursor: pointer;
    }
    .excel-list-add a{
        height: 100%;
        color: #8eeeff;
        flex-direction: column;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .excel-list-add a:hover{
        color:#2d8cf0;
    }
    .excel-view-item{
        position: relative;
        margin: 16px;
        display: flex;
        flex-direction: column;
        width: 290px;
        height: 200px;
        border: 1px solid #3a4659;
        overflow: hidden;
    }

    .excel-view-item:hover{
        box-shadow: 0 0 20px 0 #000;
        border: 1px solid #00baff;
    }
    .excel-view-item .thumb {
        position: relative;
        height: calc(100% - 36px);
    }
    .excel-view-item .thumb img{
        width: 100%;height: 100%
    }
    .excel-view-item .item-footer{
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
    .excel-view-item .item-name{
        width: 100px;
        padding: 0 5px;
        line-height: 28px;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        border: 1px solid transparent;
    }
    .opt-show{
        color: #bcc9d4;
        margin-right: 10px;
        cursor: pointer;
    }

    .excel-edit-container{
        position: absolute;
        top: 0;
        left: 0;
        background-color: rgba(29,38,46,0.8);
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .excel-edit-container a{
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
</style>