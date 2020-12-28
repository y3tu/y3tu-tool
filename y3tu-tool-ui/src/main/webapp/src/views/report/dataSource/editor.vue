<template>
    <el-form ref="form" :model="dataSource" :rules="rules" label-position="right" label-width="120px">
        <el-form-item label="数据源名称" prop="name">
            <el-input v-model="dataSource.name"/>
        </el-form-item>
        <el-form-item label="数据源类型" prop="dbType">
            <el-select v-model="dataSource.dbType" placeholder="请选择">
                <el-option value="mysql" label="mysql"/>
                <el-option value="oracle" label="oracle"/>
            </el-select>
        </el-form-item>
        <el-form-item label="驱动" prop="dbDriver">
            <el-input v-model="dataSource.dbDriver"/>
        </el-form-item>
        <el-form-item label="数据源地址" prop="dbUrl">
            <el-input v-model="dataSource.dbUrl"/>
        </el-form-item>
        <el-form-item label="登录用户名" prop="dbUsername">
            <el-input v-model="dataSource.dbUsername"/>
        </el-form-item>
        <el-form-item label="登录密码" prop="dbPassword">
            <el-input v-model="dataSource.dbPassword" show-password/>
        </el-form-item>

        <el-row type="flex" justify="end">
            <el-col :span="3">
                <el-tooltip effect="dark" content="保存" placement="top">
                    <el-button type="success" icon="el-icon-check" circle></el-button>
                </el-tooltip>
            </el-col>
            <el-col :span="3">
                <el-tooltip effect="dark" content="清空" placement="top">
                    <el-button type="danger" icon="el-icon-delete" circle></el-button>
                </el-tooltip>
            </el-col>
        </el-row>

    </el-form>


</template>

<script>
    export default {
        data() {
            return {
                dataSource: this.initDataSource(),
                rules: {
                    name: {required: true, message: "数据源名称不能为空", trigger: 'blur'},
                    dbType: {required: true, message: "数据源类型不能为空", trigger: 'blur'},
                    dbDriver: {required: true, message: "驱动不能为空", trigger: 'blur'},
                    dbUrl: {required: true, message: "数据源地址不能为空", trigger: 'blur'},
                    dbUsername: {required: true, message: "用户名不能为空", trigger: 'blur'},
                    dbPassword: {required: true, message: "密码不能为空", trigger: 'blur'}
                },
                buttonLoading: false
            }
        },
        methods: {
            initDataSource() {
                return {
                    id: '',
                    name: '',
                    dbType: '',
                    dbDriver: '',
                    dbUrl: '',
                    dbUsername: '',
                    dbPassword: ''
                }
            },
            submitForm() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        this.buttonLoading = true;
                        if (!this.dataSource.id) {
                            // create
                            this.$post('base/dataSource/create', {...this.dataSource}).then(() => {
                                this.buttonLoading = false;
                                this.isVisible = false;
                                this.$message({
                                    message: this.$t('tips.createSuccess'),
                                    type: 'success'
                                });
                                this.$emit('success')
                            }).catch(() => {
                                this.buttonLoading = false;
                            });
                        } else {
                            // update
                            this.dataSource.createTime = this.dataSource.modifyTime = null;
                            this.$put('base/dataSource/update', {...this.dataSource}).then(() => {
                                this.buttonLoading = false;
                                this.isVisible = false;
                                this.$message({
                                    message: this.$t('tips.updateSuccess'),
                                    type: 'success'
                                });
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

<style>

</style>