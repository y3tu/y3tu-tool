import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App)

import plugin from './plugin'

app.use(store)
app.use(router)
app.use(plugin)

app.mount('#app')