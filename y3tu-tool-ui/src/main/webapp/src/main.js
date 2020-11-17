//polyfill 浏览器兼容性
import '@babel/polyfill'
//Vue
import Vue from 'vue'
import App from './App'

//加载配置
import setting from '@/setting'
//登录模式路由
import loginModeRouter from './router/loginMode' ;
//单个页面路由
import singleModeRouter from './router/singleMode'

let router = null;
if (setting.env.loginMode) {
    router = loginModeRouter;
} else {
    router = singleModeRouter;
}


//store
import store from './store/index'

// 核心插件
import common from '@/plugin/common'


Vue.use(common);

import '@/styles/index.scss' // global css

import '@/permission' // permission control


new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App),
});
