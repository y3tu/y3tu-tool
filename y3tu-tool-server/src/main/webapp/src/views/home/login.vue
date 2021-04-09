<template>
  <div class="login-container">
    <el-row>
      <el-col :span="16">
        <day></day>
      </el-col>
      <el-col :span="8">
        <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-form" autocomplete="off" label-position="left">
          <el-form-item prop="username">
            <el-input
                ref="username"
                v-model="loginForm.username"
                placeholder="用户名"
                prefix-icon="el-icon-user"
                name="username"
                type="text"
                autocomplete="off"
                @keyup.enter.native="handleLogin"/>
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
                @keyup.enter.native="handleLogin"/>
          </el-form-item>
          <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:14px;" @click.native.prevent="handleLogin">
            登录
          </el-button>
        </el-form>
      </el-col>
    </el-row>


  </div>

</template>

<script>

import day from './day'
import night from './night'

export default {
  name: 'login',
  components: {day, night},
  data(){
    return{
      loginForm:{
        username:'',
        password:''
      }
    }
  },
  mounted() {

  },
  methods:{
    handleLogin(){

    }
  }
}

</script>

<style lang="scss">
 body{
   background: #e3f1ff;
 }

 $bg: #283443;
 $light_gray: #fff;
 $cursor: #555;

 @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
   .login-container .el-input input {
     color: $cursor;
   }
 }

 .login-container {
 .el-input {
   display: inline-block;

 input {
   background: transparent;
   border: 0;
   -webkit-appearance: none;
   border-radius: 0;
   color: #000000;
   height: 28px;
   caret-color: $cursor;

 &:-webkit-autofill {
    box-shadow: 0 0 0 1000px $bg inset !important;
    -webkit-text-fill-color: $cursor !important;
  }
 }
 }

 .el-form-item {
 //border: 1px solid #dedede;
   border-radius: 2px;
   color: #454545;
   transition: all .3s;

 &:hover {
    border-color: #57a3f3;
  }
 }
 }
</style>