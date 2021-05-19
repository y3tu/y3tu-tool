<template>
    <div class="main-container">
        <div class="head-container">
            <label class="form-item-label">数据源名称</label>
            <el-input clearable v-model="pageInfo.entity.name" placeholder="请输入数据源名称" style="width:200px"
                      class="form-item"
                      @keyup.enter="query"/>
            <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain @click="query">
                查询
            </el-button>
            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain @click="reset">
                重置
            </el-button>
            <el-button class="form-item" size="mini" type="primary" icon="el-icon-plus" circle @click="create"/>
        </div>

        <el-divider/>

        <el-table
                ref="table"
                border
                size="mini"
                v-loading="pageInfo.pageLoading"
                :data="pageInfo.records"
                style="width: 100%;">
            <el-table-column label="数据源名称" prop="name" align="center" show-overflow-tooltip min-width="120px">
                <template #default="scope">
                    <span>{{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column label="驱动" :show-overflow-tooltip="true" align="center" min-width="150px">
                <template #default="scope">
                    <span>{{ scope.row.dbDriver }}</span>
                </template>
            </el-table-column>
            <el-table-column label="数据库类型" align="center" min-width="80px">
                <template #default="scope">
                    <span>{{ scope.row.dbType }}</span>
                </template>
            </el-table-column>
            <el-table-column label="数据源地址" align="center" show-overflow-tooltip min-width="200px">
                <template #default="scope">
                    <span>{{ scope.row.dbUrl }}</span>
                </template>
            </el-table-column>
            <el-table-column label="登录用户名" align="center" min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.dbUsername }}</span>
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center" show-overflow-tooltip min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.remarks }}</span>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" min-width="110px">
                <template #default="scope">
                    <span>{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="更新时间" align="center" min-width="110px">
                <template #default="scope">
                    <span>{{ scope.row.updateTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="150px" class-name="small-padding fixed-width">
                <template #default="scope">
                    <i class="el-icon-connection table-operation" style="color: #87d068;" @click="test(scope.row)"/>
                    <i class="el-icon-setting table-operation" style="color: #2db7f5;" @click="edit(scope.row)"/>
                    <i class="el-icon-delete table-operation" style="color: #f50;" @click="del(scope.row)"/>
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

        <el-divider/>

        <transition name="el-zoom-in-bottom">
            <div v-show="showEditor" style="margin-top: 20px">
                <Editor ref="editor"
                        style="width: 30%"
                        @success="query"/>
            </div>
        </transition>


    </div>
</template>

<script>

    import Editor from './editor'

    import {page, del, testConnect} from './api'

    export default {
        name: 'dataSource',
        components: {Editor},
        data() {
            return {
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
                showEditor: false

            }
        },
        created() {
            this.$nextTick(() => {
                this.query();
            })
        },
        methods: {
            query() {
                this.showEditor = false;
                this.pageInfo.pageLoading = true
                page(this.pageInfo).then(res => {
                    this.pageInfo.pageLoading = false
                    this.pageInfo = res.data;
                }).catch(() => {
                    this.pageInfo.pageLoading = false
                })
            },
            reset() {

            },
            edit(row) {
                this.showEditor = true;
                this.$refs.editor.setDataSource(row);
            },
            create() {
                this.showEditor = true;
                this.$refs.editor.setDataSource();
            },
            del(row) {
                this.$confirm('选中数据将被永久删除, 是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    del(row.id).then(() => {
                        this.$toast('删除成功', 'success', 3000);
                        this.query()
                    })
                })
            },
            test(row) {
                testConnect(row.id).then(res => {
                    if (res.data) {
                        this.$toast('连接成功', 'success', 3000);
                    } else {
                        this.$toast('连接失败,请检查配置!', 'error', 3000);
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