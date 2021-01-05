<template>
    <el-card shadow="never">
        <template v-slot:header>
            <span>SQL字典</span>
            <el-button
                    :loading="saveLoading"
                    icon="el-icon-check"
                    size="mini"
                    style="float: right; padding: 6px 9px"
                    type="primary"
                    @click="doSave">保存
            </el-button>
        </template>
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
            <el-form-item label="数据源" prop="dsId">
                <el-select v-model="form.dsId"
                           filterable
                           remote
                           placeholder="请输入关键词"
                           :remote-method="remoteMethod"
                           :loading="selectLoading">
                    <el-option v-for="item in dataSourceList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="SQL" prop="querySql">
                <code-editor :value="form.querySql" height="250px" code-type="text/x-sql" @change="querySqlChange"/>
            </el-form-item>
            <el-form-item label="条件字段" prop="whereColumn">
                <el-input v-model="form.whereColumn" style="width: 40%"/>
                <span style="color: #C0C0C0;margin-left: 10px;">多个字段用逗号分隔</span>
            </el-form-item>
            <el-form-item label="描述" prop="description">
                <el-input v-model="form.remarks" style="width: 40%"/>
            </el-form-item>
            <el-form-item label="状态" prop="status" style="width: 40%">
                <el-select
                        v-model="form.status"
                        placeholder="请选择"
                        clearable
                        style="width: 110px">
                    <el-option :value="0" label="正常"/>
                    <el-option :value="1" label="禁用"/>
                </el-select>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
    import CodeEditor from '@/components/CodeEditor/index'

    import {getDictSql, saveDictSql} from './api'
    import {getDataSourceByName} from "./api";

    export default {
        // 数据字典
        components: {CodeEditor},
        data() {
            return {
                saveLoading: false,
                selectLoading: false,
                dataSourceList: [],
                dictId: '',
                form: {
                    dsId: '',
                    querySql: '',
                    whereColumn: '',
                    remarks: '',
                    status: ''
                },
                rules: {
                    dsId: [
                        {required: true, message: '数据源不能为空', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            created() {
                this.$nextTick(() => {
                    this.init();
                })
            },
            init() {
                if (this.$isNotEmpty(this.dictId)) {
                    getDictSql(this.dictId).then(res => {
                        this.form = res.data;
                    })
                }
            },
            doSave() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.saveLoading = true;
                        this.form.dictId = this.dictId;
                        saveDictSql(this.form).then(() => {
                            this.saveLoading = false;
                            this.$notify({
                                title: '保存成功',
                                type: 'success',
                                duration: 2500
                            });
                        }).catch(() => {
                            this.saveLoading = false;
                        })
                    }
                })
            },
            querySqlChange(value) {
                this.form.querySql = value;
            },
            remoteMethod(name) {
                if (name !== '') {
                    this.selectLoading = true;
                    getDataSourceByName(name).then(res => {
                        this.selectLoading = false;
                        if (res.data && res.data.length > 0) {
                            this.dataSourceList = res.data;
                        } else {
                            this.dataSourceList = [];
                        }
                    })
                } else {
                    this.dataSourceList = [];
                }
            },
        }
    }
</script>

<style>

</style>