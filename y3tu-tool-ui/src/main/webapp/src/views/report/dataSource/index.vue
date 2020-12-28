<template>
    <div class="main-container">
        <div class="head-container">
            <label class="form-item-label">数据源名称</label>
            <el-input clearable v-model="name" placeholder="请输入数据源名称" style="width:200px" class="form-item"
                      @keyup.enter="search"/>
            <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain @click="search">
                搜索
            </el-button>
            <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain @click="reset">
                重置
            </el-button>
            <el-button class="form-item" size="mini" type="primary" icon="el-icon-plus" circle @click="reset"/>
            <el-button class="form-item" size="mini" type="danger" icon="el-icon-close" circle @click="reset"/>
        </div>

        <el-divider/>

        <el-table
                ref="table"
                border
                v-loading="pageLoading"
                :data="records"
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
            <el-table-column label="创建时间" align="center" min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="更新时间" align="center" min-width="100px">
                <template #default="scope">
                    <span>{{ scope.row.updateTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="150px" class-name="small-padding fixed-width">
                <template #default="row">
                    <i class="el-icon-connection table-operation" style="color: #87d068;" @click="test(row)"/>
                    <i class="el-icon-setting table-operation" style="color: #2db7f5;" @click="edit(row)"/>
                    <i class="el-icon-delete table-operation" style="color: #f50;" @click="deleteData(row)"/>
                </template>
            </el-table-column>
        </el-table>

        <!--分页组件-->
        <el-pagination
                :total="total"
                :current-page="current"
                style="margin-top: 8px;"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="sizeChange"
                @current-change="pageChange"/>

        <transition name="el-zoom-in-bottom">
            <div v-show="showEditor" style="margin-top: 20px">
                <Editor style="width: 30%"/>
            </div>
        </transition>


    </div>
</template>

<script>

    import Editor from './editor'

    export default {
        name: 'dataSource',
        components: {Editor},
        data() {
            return {
                name: '',
                pageLoading: false,
                records: [],
                total: 0,
                current: 1,
                showEditor: false

            }
        },
        methods: {
            search() {

            },
            reset() {
                this.showEditor = !this.showEditor;
            },
            sizeChange() {

            },
            pageChange() {

            }
        }
    }
</script>

<style>

</style>