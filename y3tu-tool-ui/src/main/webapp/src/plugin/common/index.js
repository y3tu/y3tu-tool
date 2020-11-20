// 功能插件
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


