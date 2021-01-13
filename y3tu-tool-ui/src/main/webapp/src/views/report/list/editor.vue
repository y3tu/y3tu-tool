<template>
    <el-form ref="form" :model="report" :rules="rules" label-position="right" label-width="120px">
        <el-form-item label="报表名称" prop="name">
            <el-input v-model="report.name"/>
        </el-form-item>
        <el-form-item label="报表类型" prop="type">
            <el-select v-model="report.type" placeholder="请选择">
                <el-option value="common" label="通用报表"/>
                <el-option value="jasper" label="Jasper报表"/>
            </el-select>
        </el-form-item>
        <el-form-item v-show="report.type==='common'" label="列头" prop="columnHeader">
            <el-input v-model="report.columnHeader"/>
        </el-form-item>
        <el-form-item v-if="report.type==='common'" label="查询SQL" prop="querySql">
            <code-editor :value="report.querySql" height="250px" code-type="text/x-sql" @change="querySqlChange"/>
        </el-form-item>
        <el-form-item v-show="report.type==='jasper'" label="上传模板">
            <el-select v-model="report.templateType" placeholder="请选择">
                <el-option :value="1" label="主数据模板"/>
                <el-option :value="2" label="子数据模板"/>
            </el-select>
            <el-upload
                    ref="upload"
                    accept=".jrxml"
                    :action="uploadAction"
                    :file-list="fileList"
                    :multiple="false"
                    :limit="1"
                    :on-preview="download"
                    :on-error="uploadError"
                    :before-upload="beforeUpload">
                <el-button size="small" type="primary">选取文件</el-button>
            </el-upload>
        </el-form-item>
        <el-form-item label="数据源" prop="dsId">
            <el-select v-model="report.dsId" filterable>
                <el-option v-for="item in dataSourceList"
                           :key="item.id"
                           :label="item.name"
                           :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="报表状态" prop="status">
            <el-select v-model="report.status" placeholder="请选择">
                <el-option value="00A" label="启用"/>
                <el-option value="00X" label="禁用"/>
            </el-select>
        </el-form-item>

        <el-form-item label="参数配置" prop="params">
            <query-param ref="params" :params="report.params"/>
        </el-form-item>

        <el-row type="flex" justify="end">
            <el-col :span="3">
                <el-tooltip effect="dark" content="保存" placement="top">
                    <el-button :loading="buttonLoading" @click="submitForm" type="primary" icon="el-icon-check">保存
                    </el-button>
                </el-tooltip>
            </el-col>
        </el-row>
    </el-form>

</template>

<script>

    import CodeEditor from '@/components/CodeEditor'
    import QueryParam from './queryParam'
    import {createUUID} from '@/utils'
    import {create, update, getAllDataSource, downloadFile} from "./api";

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
                fileList: [],
                uploadAction: process.env.VUE_APP_BASE_API + '/y3tu-tool-report/report/upload',
                dataSourceList: [],
                rules: {
                    name: {required: true, message: "报表名称不能为空", trigger: 'blur'},
                    type: {required: true, message: "报表类型不能为空", trigger: 'blur'},
                    dsId: {required: true, message: "数据源不能为空", trigger: 'blur'},
                    status: {required: true, message: "状态不能为空", trigger: 'blur'},
                },
                buttonLoading: false,
            }
        },
        computed: {
            report() {
                return this.value;
            }
        },
        created() {
            this.$nextTick(() => {

                if (this.report.fileName) {
                    this.fileList.push({name: this.report.fileName});
                }

                getAllDataSource().then(res => {
                    if (res.data && res.data.length > 0) {
                        this.dataSourceList = res.data;
                    } else {
                        this.dataSourceList = [];
                    }
                })
            })
        },
        methods: {
            setReport(val) {
                this.report = {...val}
            },
            querySqlChange(val) {
                this.report.querySql = val;
            },
            beforeUpload(file) {
                let limit = true;
                limit = file.size / 1024 / 1024 < 100;
                if (!limit) {
                    this.loading = false;
                    this.$message.error('上传文件大小不能超过 100MB!')
                }
                new Promise((resolve) => {
                    this.$nextTick(() => {
                        //给文件创建随机uuid文件名前缀
                        let fileTempPrefix = createUUID();
                        this.report.fileTempPrefix = fileTempPrefix
                        this.report.fileName = file.name;
                        this.uploadAction = this.uploadAction + '?fileTempPrefix=' + fileTempPrefix
                        resolve()
                    });
                });
                return limit
            },
            uploadError(err) {
                this.$toast(JSON.parse(err.message).message, 'error', 3000);
            },
            download(file) {
                downloadFile(this.report.id, file.name);
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