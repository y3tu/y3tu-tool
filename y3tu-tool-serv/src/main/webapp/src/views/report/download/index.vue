<template>
    <div class="main-container">

        <div class="head-container">
            <label class="form-item-label">报表名称</label>
            <el-input clearable v-model="pageInfo.entity.name" placeholder="请输入名称" style="width:200px"
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
                    <span>{{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column label="生成状态" :show-overflow-tooltip="true" align="center" min-width="150px">
                <template #default="scope">
                    <span>{{ scope.row.type }}</span>
                </template>
            </el-table-column>
            <el-table-column label="生成结果" align="center" min-width="80px">
                <template #default="scope">
                    <span>{{ scope.row.dbType }}</span>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" show-overflow-tooltip min-width="200px">
                <template #default="scope">
                    <span>{{ scope.row.dbUrl }}</span>
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


    import {page} from './api'

    export default {
        name: 'download',
        data() {
            return {
                pageInfo: {
                    entity: {
                        name: ''
                    },
                    params: {
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