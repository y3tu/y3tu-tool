
// A modern alternative to CSS resets
import 'normalize.css/normalize.css'

import ElementUI from 'element-ui'
// lang i18n
import locale from 'element-ui/lib/locale/lang/zh-CN'

//Element css
import 'element-ui/lib/theme-chalk/index.css'

//图表
import VCharts from 'v-charts'


//图标
import Icon from 'vue-awesome/components/Icon'

// 按需引入awesome图标
// import 'vue-awesome/icons/brands/qq'
// import 'vue-awesome/icons/brands/weixin'
// import 'vue-awesome/icons/brands/weibo'
// import 'vue-awesome/icons/brands/github'
//全局引入
import 'vue-awesome/icons'

// 功能插件
import pluginError from '@/plugin/error'
import pluginLog from '@/plugin/log'

import {setStore, getStore, removeStore} from '@/utils/storage'



export default {
    install(Vue, options) {
        //ElementUI
        Vue.use(ElementUI,{locale});
        Vue.use(VCharts);
        // 插件
        Vue.use(pluginError)
        Vue.use(pluginLog)

        Vue.component('icon', Icon);
        // 设置为 false 以阻止 vue 在启动时生成生产提示。
        // https://cn.vuejs.org/v2/api/#productionTip
        Vue.config.productionTip = false


        // 挂载全局使用的方法
        Vue.prototype.setStore = setStore;
        Vue.prototype.getStore = getStore;
        Vue.prototype.removeStore = removeStore;

    }
}


