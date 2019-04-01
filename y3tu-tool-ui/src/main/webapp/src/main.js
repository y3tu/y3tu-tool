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
