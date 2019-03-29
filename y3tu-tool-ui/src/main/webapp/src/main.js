//polyfill 浏览器兼容性
import '@babel/polyfill'
//Vue
import Vue from 'vue'
import App from './App'
//路由
import router from './router'
//store
import store from './store/index'

// 核心插件
import Admin from '@/plugin/admin'

import '@/icons' // icon

Vue.use(Admin);

import '@/styles/index.scss' // global css

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App),
});
