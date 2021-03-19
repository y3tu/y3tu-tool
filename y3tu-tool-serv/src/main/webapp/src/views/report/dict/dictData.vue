<template>
    <el-card>
        <template v-slot:header>
            <span>字典详情</span>
            <el-button
                    class="form-item"
                    size="mini"
                    style="float: right;padding: 4px 10px"
                    type="primary"
                    icon="el-icon-plus"
                    @click="doAdd">新增
            </el-button>
        </template>

        <div v-if="dictName === ''">
            <div class="my-code">点击字典查看详情</div>
        </div>
        <div v-else>
            <!--表格渲染-->
            <el-table v-loading="pageInfo.pageLoading" :data="pageInfo.records" size="small" style="width: 100%;">
                <el-table-column label="所属字典">
                    <template #default>
                        <span>{{ dictName }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="字典名称"/>
                <el-table-column prop="value" label="字典值"/>
                <el-table-column prop="seq" label="排序"/>
                <el-table-column prop="status" label="状态">
                    <template #default="scope">
                        <span>{{ scope.row.status==='00A'?'启用':'禁用' }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="130px" align="center">
                    <template #default="scope">
                        <i class="el-icon-setting table-operation" style="color: #2db7f5;" @click="edit(scope.row)"/>
                        <i class="el-icon-delete table-operation" style="color: #f50;" @click="del(scope.row.id)"/>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页组件-->
            <el-pagination
                    :total="pageInfo.total"
                    :current-page="pageInfo.current"
                    style="margin-top: 8px;"
                    layout="total, prev, pager, next, sizes"
                    @size-change="sizeChange"
                    @current-change="pageChange"/>
        </div>

        <el-dialog v-model="dialog" :title="isAdd ? '新增字典' : '编辑字典'" append-to-body width="550px" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
                <el-form-item label="字典名称" prop="name">
                    <el-input v-model="form.name" clearable/>
                </el-form-item>

                <el-form-item label="字典值" prop="value">
                    <el-input v-model="form.value"/>
                </el-form-item>

                <el-form-item label="排序" prop="seq">
                    <el-input v-model.number="form.seq"/>
                </el-form-item>

                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status"
                               placeholder="请选择">
                        <el-option
                                v-for="item in statusArr"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <template v-slot:footer>
                <el-button type="text" @click="cancel">取消</el-button>
                <el-button :loading="submitLoading" type="primary" @click="doSubmit">确认</el-button>
            </template>
        </el-dialog>
    </el-card>

</template>

<script>
    import {dictDataPage, createDictData, updateDictData, deleteDictData} from './api'

    export default {

        created() {
            this.$nextTick(() => {
                this.init();
            })
        },
        data() {
            return {
                statusArr: [{
                    value: '00A',
                    label: '正常'
                }, {
                    value: '00X',
                    label: '禁用'
                }],

                pageInfo: {
                    entity: {
                        dictId: ''
                    },
                    pageLoading: false,
                    current: 1,
                    total: 0,
                    pageSize: 10,
                    records: [],
                    asc: ['seq']
                },
                dialog: false,
                isAdd: true,
                form: {
                    name: '',
                    value: '',
                    seq: '',
                    status: ''
                },
                rules: {
                    name: [{required: true, message: '请输入字典名称', trigger: 'blur'}],
                    value: [{required: true, message: '请输入字典值', trigger: 'blur'}],
                    seq: [{required: true, message: '请输入字典排序', trigger: 'blur'}],
                    status: [{required: true, message: '请选择字典状态', trigger: 'blur'}]
                },
                submitLoading: false,
                delLoading: false,
                dictName: '',
            }
        },
        methods: {
            init() {
                this.query();
            },
            query() {
                this.pageInfo.pageLoading = true
                dictDataPage(this.pageInfo).then(res => {
                    this.pageInfo.pageLoading = false
                    this.pageInfo = res.data;
                }).catch(() => {
                    this.pageInfo.pageLoading = false
                })
            },
            del(id) {
                this.delLoading = true;
                deleteDictData([id]).then(() => {
                    this.$message({
                        message: '删除成功!',
                        type: 'success'
                    });
                    this.delLoading = false;
                    this.query();
                }).catch(() => {
                    this.delLoading = false;
                })
            },
            edit(row) {
                this.dialog = true;
                this.isAdd = false;
                this.form.dictId = this.dictId;
                this.form = this.$deepClone(row);
            },
            doAdd() {
                this.isAdd = true;
                this.dialog = true;
                this.form.dictId = this.dictId;
            },
            cancel() {
                this.resetForm();
            },
            doSubmit() {
                this.submitLoading = true;
                if (this.$isEmpty(this.pageInfo.entity.dictId)) {
                    this.$message({
                        message: '请选择字典!',
                        type: 'warning'
                    });
                    this.submitLoading = false;
                    return
                } else {
                    this.form.dictId = this.pageInfo.entity.dictId;
                }
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        if (this.isAdd) {
                            //新增

                            createDictData(this.form).then(() => {
                                this.$message({
                                    message: '新增成功!',
                                    type: 'success'
                                });
                                this.submitLoading = false;
                                this.cancel();
                                this.init();
                            }).catch(() => {
                                this.submitLoading = false;
                            })
                        } else {
                            //编辑
                            updateDictData(this.form).then(() => {
                                this.$message({
                                    message: '编辑成功!',
                                    type: 'success'
                                });
                                this.submitLoading = false;
                                this.cancel();
                                this.init();
                            }).catch(() => {
                                this.submitLoading = false;
                            })
                        }
                    } else {
                        this.submitLoading = false;
                    }
                })

            },
            resetForm() {
                this.dialog = false;
                this.form = {};
                this.$refs['form'].resetFields();
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
    .my-code {
        padding: 15px;
        line-height: 20px;
        border-left: 3px solid #ddd;
        color: #333;
        font-family: Courier New;
        font-size: 12px
    }
</style>