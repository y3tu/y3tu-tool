<template>
    <el-form ref="form" :model="report" :rules="rules" label-position="right" label-width="120px">
        <el-form-item label="报表名称" prop="name">
            <el-input v-model="report.name"/>
        </el-form-item>
        <el-form-item label="报表类型" prop="type">
            <el-select v-model="report.type" placeholder="请选择">
                <el-option value="1" label="通用报表"/>
                <el-option value="2" label="Jasper报表"/>
            </el-select>
        </el-form-item>
        <el-form-item v-show="report.type==='1'" label="列头" prop="columnHeader">
            <el-input v-model="report.columnHeader"/>
        </el-form-item>
        <el-form-item v-show="report.type==='1'" label="查询SQL" prop="querySql">
            <code-editor :value="report.querySql" height="250px" code-type="text/x-sql" @change="querySqlChange"/>
        </el-form-item>
        <el-form-item v-show="report.type==='2'" label="上传模板">
            <el-select v-model="report.templateType" placeholder="请选择">
                <el-option value="1" label="主数据模板"/>
                <el-option value="2" label="子数据模板"/>
            </el-select>
            <el-upload
                    ref="upload"
                    action="https://jsonplaceholder.typicode.com/posts/"
                    :auto-upload="false">
                <el-button size="small" type="primary">选取文件</el-button>
            </el-upload>
        </el-form-item>
        <el-form-item label="数据源" prop="dsId">
            <el-select v-model="report.dsId"
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
        <el-form-item label="报表状态" prop="status">
            <el-select v-model="report.status" placeholder="请选择">
                <el-option value="0" label="启用"/>
                <el-option value="1" label="禁用"/>
            </el-select>
        </el-form-item>

        <el-form-item label="参数配置" prop="params">
            <query-param ref="params" :params="report.params"/>
        </el-form-item>


        <el-row type="flex" justify="end">
            <el-col :span="3">
                <el-tooltip effect="dark" content="保存" placement="top">
                    <el-button :loading="buttonLoading" @click="submitForm" type="primary" icon="el-icon-check">保存</el-button>
                </el-tooltip>
            </el-col>
        </el-row>

    </el-form>


</template>

<script>

    import CodeEditor from '@/components/CodeEditor'
    import QueryParam from './queryParam'
    import {create, update, getDataSourceByName} from "./api";

    export default {
        name: 'editor',
        props: {
            value: {
                type: Object
            },
        },
        components: {CodeEditor, QueryParam},
        data() {
            return {
                dataSourceList: [],
                rules: {
                    name: {required: true, message: "报表名称不能为空", trigger: 'blur'},
                    type: {required: true, message: "报表类型不能为空", trigger: 'blur'}
                },
                selectLoading: false,
                buttonLoading: false,
            }
        },
        computed: {
            report() {
                if (this.value) {
                    return this.value;
                } else {
                    return this.initReport();
                }
            }
        },
        created() {
        },
        methods: {
            initReport() {
                return {
                    id: '',
                    name: '',
                    type: "1",
                    columnHeader: '',
                    querySql: 'select',
                    dsId: {},
                    status: {},
                    templateType: {},
                    params: [],
                }
            },
            setReport(val) {
                this.report = {...val}
            },
            querySqlChange(val) {
                this.report.querySql = val;
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
            submitForm() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.buttonLoading = true;

                        //获取参数列表的值
                        this.report.params = this.$refs.params.getParams();

                        if (!this.report.id) {
                            // create
                            create(this.report).then(() => {
                                this.buttonLoading = false;
                                this.$toast('创建成功', 'success', 3000);
                                this.$emit('success')
                            }).catch(() => {
                                this.buttonLoading = false;
                            });
                        } else {
                            // update
                            update(this.report).then(() => {
                                this.buttonLoading = false;
                                this.$toast('更新成功', 'success', 3000);
                                this.$emit('success')
                            }).catch(() => {
                                this.buttonLoading = false;
                            });
                        }
                    } else {
                        return false;
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .el-form-item__content {
        width: 50px;
    }
</style>