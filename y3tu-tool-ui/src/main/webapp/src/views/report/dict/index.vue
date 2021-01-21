<template>
    <div class="main-container">
        <el-row :gutter="10">
            <el-col :xs="24" :sm="24" :md="10" :lg="10" :xl="10">
                <el-card>
                    <template v-slot:header>
                        <span>字典列表</span>
                        <el-button class="form-item" style="float: right;" size="mini" type="primary"
                                   icon="el-icon-plus" circle @click="doAdd"/>
                        <el-button class="form-item" size="mini" type="success" icon="el-icon-search" plain
                                   @click="query">查询
                        </el-button>
                        <el-button class="form-item" size="mini" type="warning" icon="el-icon-refresh-left" plain
                                   @click="reset">重置
                        </el-button>
                    </template>

                    <div class="head-container">
                        <el-form :inline="true">
                            <el-form-item label="字典名">
                                <el-input
                                        v-model="pageInfo.entity.name"
                                        clearable
                                        style="width: 100px">
                                </el-input>
                            </el-form-item>

                            <el-form-item label="字典编码">
                                <el-input
                                        v-model="pageInfo.entity.code"
                                        clearable
                                        style="width: 80px">
                                </el-input>
                            </el-form-item>

                            <el-form-item label="类型">
                                <el-select
                                        v-model="pageInfo.entity.type"
                                        clearable
                                        placeholder="请选择"
                                        style="width: 120px">
                                    <el-option
                                            v-for="item in typeArr"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </div>

                    <!--表格渲染-->
                    <el-table v-loading="pageInfo.pageLoading" :data="pageInfo.records" size="small"
                              highlight-current-row
                              style="width: 100%;"
                              @current-change="handleCurrentChange">
                        <el-table-column :show-overflow-tooltip="true" prop="name" label="名称"/>
                        <el-table-column :show-overflow-tooltip="true" prop="code" label="编码"/>
                        <el-table-column prop="type" label="类型">
                            <template #default="scope">
                                <span>{{ scope.row.type==='common'?'普通字典':'SQL字典' }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column :show-overflow-tooltip="true" prop="remarks" label="描述"/>
                        <el-table-column label="操作" width="130px" align="center">
                            <template #default="scope">
                                <i class="el-icon-setting table-operation" style="color: #2db7f5;"
                                   @click="edit(scope.row)"/>
                                <i class="el-icon-delete table-operation" style="color: #f50;"
                                   @click="del(scope.row.id)"/>
                            </template>
                        </el-table-column>
                    </el-table>
                    <!--分页组件-->
                    <el-pagination
                            :total="pageInfo.total"
                            style="margin-top: 8px;"
                            layout="total, prev, pager, next, sizes"
                            @size-change="sizeChange"
                            @current-change="pageChange">
                    </el-pagination>
                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="14" :lg="14" :xl="14">
                <dict-sql v-bind:dict-sql="dictSql" v-if="isDictSql"/>
                <dict-data ref="dictData" v-show="!isDictSql"/>
            </el-col>
        </el-row>

        <el-dialog
                :title="isAdd ? '新增字典' : '编辑字典'"
                v-model="dialog">
            <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
                <el-form-item label="字典名称" prop="name">
                    <el-input v-model="form.name" clearable/>
                </el-form-item>
                <el-form-item label="字典编码" prop="code">
                    <el-input v-model="form.code"/>
                </el-form-item>
                <el-form-item label="字典类型" prop="type">
                    <el-select v-model="form.type"
                               placeholder="请选择">
                        <el-option
                                v-for="item in typeArr"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述" prop="remarks">
                    <el-input v-model="form.remarks"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button type="text" @click="cancel">取消</el-button>
                <el-button :loading="submitLoading" type="primary" @click="doSubmit">确认</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {dictPage, createDict, updateDict, deleteDict, getDictSql} from './api.js'

    import dictData from './dictData'
    import dictSql from './dictSql'

    export default {
        name: 'dict',
        components: {dictData, dictSql},
        data() {
            return {
                typeArr: [{
                    value: 'common',
                    label: '普通字典'
                }, {
                    value: 'sql',
                    label: 'SQL字典'
                }],
                pageInfo: {
                    entity: {
                        name: '',
                        code: '',
                        type: ''
                    },
                    pageLoading: false,
                    current: 1,
                    total: 0,
                    pageSize: 10,
                    records: [],
                },
                dialog: false,
                isAdd: true,
                form: {
                    name: '',
                    code: '',
                    type: '',
                    remarks: ''
                },
                rules: {
                    name: [{required: true, message: '请输入字典名称', trigger: 'blur'}],
                    code: [{required: true, message: '请输入字典编码', trigger: 'blur'}],
                    type: [{required: true, message: '请选择字典类型', trigger: 'blur'}],
                },
                submitLoading: false,
                delLoading: false,
                dictId: '',
                dictName: '',
                isDictSql: false,
                dictSql: {}
            }
        },
        created() {
            this.$nextTick(() => {
                this.query();
            })
        },
        methods: {
            query() {
                this.$refs.dictData.dictName = '';
                this.$refs.dictData.dictId = '';
                this.pageInfo.pageLoading = true
                dictPage(this.pageInfo).then(res => {
                    this.pageInfo.pageLoading = false
                    this.pageInfo = res.data;
                }).catch(() => {
                    this.pageInfo.pageLoading = false
                })
            },
            del(id) {
                this.delLoading = true;
                deleteDict([id]).then(() => {
                    this.delLoading = false;
                    this.$notify({
                        title: '删除成功',
                        type: 'success',
                        duration: 2500
                    });
                    this.query();
                }).catch(() => {
                    this.delLoading = false;
                })
            },
            handleCurrentChange(val) {
                if (val) {
                    if (val.type === 'common') {
                        this.isDictSql = false;
                        this.$refs.dictData.dictName = val.name;
                        this.$refs.dictData.pageInfo.entity.dictId = val.id;
                        this.$refs.dictData.init()
                    } else {
                        this.isDictSql = false;
                        getDictSql(val.id).then(res => {
                            if (res.data) {
                                this.dictSql = res.data;
                            } else {
                                this.dictSql = {
                                    dictId: val.id,
                                    dsId: '',
                                    querySql: '',
                                    whereColumn: '',
                                    remarks: '',
                                    status: ''
                                }
                            }
                            this.isDictSql = true;
                        })
                    }
                }
            },
            edit(row) {
                this.form = {};
                this.dialog = true;
                this.isAdd = false;
                this.form = this.$deepClone(row);
            },
            doAdd() {
                this.isAdd = true;
                this.dialog = true;
            },
            cancel() {
                this.resetForm();
            },
            doSubmit() {
                this.submitLoading = true;
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        if (this.isAdd) {
                            //新增
                            createDict(this.form).then(() => {
                                this.$message({
                                    message: '新增成功!',
                                    type: 'success'
                                });
                                this.submitLoading = false;
                                this.cancel();
                                this.query();
                            }).catch(() => {
                                this.submitLoading = false;
                            })
                        } else {
                            //编辑
                            this.form.createTime = this.form.updateTime = null;
                            updateDict(this.form).then(() => {
                                this.$message({
                                    message: '编辑成功!',
                                    type: 'success'
                                });
                                this.submitLoading = false;
                                this.cancel();
                                this.query();
                            }).catch(() => {
                                this.submitLoading = false;
                            })
                        }
                    } else {
                        this.submitLoading = false;
                    }
                })

            },
            reset() {
                this.pageInfo.current = 1;
                this.pageInfo.entity =
                    this.search()
            },
            resetForm() {
                this.dialog = false;
                this.form = {};
                this.$refs.form.resetFields();
            },
            sizeChange(e) {
                this.pageInfo.current = 1;
                this.pageInfo.size = e;
                this.search()
            },
            pageChange(e) {
                this.pageInfo.current = e;
                this.search()
            }
        }
    }
</script>

<style scoped>

</style>
