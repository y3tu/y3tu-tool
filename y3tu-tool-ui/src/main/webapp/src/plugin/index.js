import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
//默认中文
import locale from 'element-plus/lib/locale/lang/zh-cn'

export default {
    install(app) {
        app.use(ElementPlus, {locale})
    }
}
