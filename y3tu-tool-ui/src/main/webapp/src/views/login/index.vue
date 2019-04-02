<template>
    <div>
        <el-card class="login-form-layout">
            <el-form autoComplete="on"
                     :model="loginForm"
                     :rules="loginRules"
                     ref="loginForm"
                     label-position="left">
                <div style="text-align: center">
                    <icon name="cloud" style="width: 56px;height: 56px;color: #409EFF"></icon>
                </div>
                <h2 class="login-title color-main">Y3tu Tool UI</h2>
                <el-form-item prop="username">
                    <el-input name="username"
                              type="text"
                              v-model="loginForm.username"
                              autoComplete="on"
                              placeholder="请输入用户名">
                        <span slot="prefix">
                            <icon name="address-book"></icon>
                        </span>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                            @keyup.enter.native="handleLogin"
                            placeholder="请输入密码"
                            v-model="loginForm.password"
                            show-password>
                      <span slot="prefix">
                          <icon name="lock"></icon>
                      </span>
                    </el-input>
                </el-form-item>
                <el-form-item style="margin-bottom: 60px">
                    <el-button style="width: 100%" type="primary" :loading="loading"
                               @click.native.prevent="handleLogin">
                        登录
                    </el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <img :src="login_center_bg" class="login-center-layout">
    </div>
</template>

<script>
    import {isvalidUsername} from '@/utils/validate';
    import login_center_bg from '@/assets/images/login_center_bg.png'

    export default {
        name: 'login',
        data() {
            const validateUsername = (rule, value, callback) => {
                if (!isvalidUsername(value)) {
                    callback(new Error('请输入正确的用户名'))
                } else {
                    callback()
                }
            };
            const validatePass = (rule, value, callback) => {
                if (value.length < 3) {
                    callback(new Error('密码不能小于3位'))
                } else {
                    callback()
                }
            };
            return {
                loginForm: {
                    username: 'admin',
                    password: '123456'
                },
                loginRules: {
                    username: [{required: true, trigger: 'blur', validator: validateUsername}],
                    password: [{required: true, trigger: 'blur', validator: validatePass}]
                },
                loading: false,
                login_center_bg
            }
        },
        methods: {

            handleLogin() {
                this.$refs.loginForm.validate(valid => {
                    if (valid) {
                        this.loading = true;
                        this.$store.dispatch('Login', this.loginForm).then(() => {
                            this.loading = false;
                            this.$router.push({path: '/'})
                        }).catch(() => {
                            this.loading = false
                        })
                    } else {
                        console.log('参数验证不合法！');
                        return false
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .login-form-layout {
        position: absolute;
        left: 0;
        right: 0;
        width: 360px;
        margin: 140px auto;
        border-top: 10px solid #409EFF;
    }

    .login-title {
        text-align: center;
    }

    .login-center-layout {
        background: #409EFF;
        width: auto;
        height: auto;
        max-width: 100%;
        max-height: 100%;
        margin-top: 200px;
    }
</style>
