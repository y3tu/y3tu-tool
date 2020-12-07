import {createApp} from 'vue'
import {ElButton, ElSelect} from 'element-plus';
import App from './App.vue';

const app = createApp(App);
app.use(ElButton);
app.use(ElSelect);
app.mount('#app');