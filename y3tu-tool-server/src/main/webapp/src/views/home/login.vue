<template>
  <div>
    <el-row type="flex" justify="center" :gutter="30">
      <el-col :span="12">
        <animation style="position: relative;top:30%"/>
      </el-col>
      <el-col :span="6">
        <el-card style="position: relative;top:50%">
          <template #header>
            <div class="card-header">
              <span class="title">Y3tu Tool UI</span>
            </div>
          </template>
          <el-form ref="loginForm"
                   :model="loginForm"
                   :rules="rules"
                   autocomplete="off"
                   label-position="left">
            <el-form-item prop="username">
              <el-input
                  ref="username"
                  v-model="loginForm.username"
                  placeholder="用户名"
                  prefix-icon="el-icon-user"
                  name="username"
                  type="text"
                  autocomplete="off"
                  @keyup.enter="handleLogin"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                  ref="password"
                  v-model="loginForm.password"
                  prefix-icon="el-icon-key"
                  type="password"
                  placeholder="请输入密码"
                  name="password"
                  autocomplete="off"
                  :show-password="true"
                  @keyup.enter="handleLogin"/>
            </el-form-item>
            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;"
                       @click.prevent="handleLogin">
              登录
            </el-button>
          </el-form>
        </el-card>

      </el-col>
    </el-row>


  </div>

</template>

<script>

import animation from '@/components/Lottie/Home'
import service from '@/plugin/axios'

export default {
  name: 'login',
  components: {animation},
  data() {
    return {
      rules: {
        username: {required: true, message: '用户名不能为空', trigger: 'blur'},
        password: {required: true, message: '用户名不能为空', trigger: 'blur'}
      },
      loginForm: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  mounted() {

  },
  methods: {
    /**
     * 登录
     */
    handleLogin() {
      let username_c = false;
      let password_c = false;
      this.$refs.loginForm.validateField('username', e => {
        if (!e) {
          username_c = true
        }
      });
      this.$refs.loginForm.validateField('password', e => {
        if (!e) {
          password_c = true
        }
      });

      if (username_c && password_c) {
        this.loading = true;
        const that = this;
        service({
          url: 'y3tu-tool-server/login',
          method: 'post',
          data: that.loginForm
        }).then((res) => {

        }).catch((error) => {
          console.error(error);
          that.loading = false;

        })
      }
    }
  }
}

</script>

<style lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center
}

.title{
  background-image:-webkit-linear-gradient(bottom,red,#fd8403,yellow);
  -webkit-background-clip:text;
  -webkit-text-fill-color:transparent;
  margin: auto;
  font-size: 25px;
  font-weight:bold;
}
</style>