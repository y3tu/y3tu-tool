<template>
  <div>
    <!--背景粒子效果-->
    <particles-bg type="circle" num=12 :bg="true"/>

    <el-row type="flex" justify="center" :gutter="30">
      <el-col :span="12">
        <animation style="position: relative;top:30%"/>
      </el-col>
      <el-col :span="6">
        <el-card style="position: relative;top:50%">
          <template #header>
            <div class="card-header">
              <span class="title">Y3tu Tool Low Code</span>
            </div>
            <div class="card-header">
              <span class="title">低代码工具平台</span>
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

import animation from './animation'
import service from '@/plugin/axios'
import util from "@/utils";

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
          url: 'y3tu-tool-lowcode/ui/login',
          method: 'post',
          data: that.loginForm
        }).then((res) => {
          util.cookies.set('ACCESS_TOKEN', res.data, {expires: 1});
          this.$router.push({path: '/'})
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

.title {
  background-image: -webkit-linear-gradient(bottom, red, #fd8403, yellow);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: auto;
  font-size: 25px;
  font-weight: bold;
}
</style>