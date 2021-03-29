import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import '@/styles/index.scss' //全局css

import EventBus from '@/utils/eventBus'
import toast from '@/utils/toast'
import {deepClone,isEmpty, isNotEmpty} from "@/utils";

const app = createApp(App)


app.config.globalProperties.$bus = new EventBus();
app.config.globalProperties.$toast = toast;
app.config.globalProperties.$isEmpty = isEmpty;
app.config.globalProperties.$isNotEmpty = isNotEmpty;
app.config.globalProperties.$deepClone = deepClone;


import plugin from './plugin'
import componentLib from '@/views/visual-design/component-lib' // 注册自定义组件

app.use(store)
app.use(router)
app.use(componentLib);
app.use(plugin)

app.mount('#app')