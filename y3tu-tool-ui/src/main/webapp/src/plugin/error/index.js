import store from '@/store'
import util from '@/utils/util'
import setting from '@/setting'

export default {
    install(Vue, options) {
        Vue.config.errorHandler = function (err, vm, info) {
            Vue.nextTick(() => {
                // 添加 log
                store.dispatch('log/add', {
                    type: 'error',
                    err,
                    vm,
                    info
                });
                // 只在开发模式下打印 log
                if (setting.env.development === true) {
                    util.log.capsule('Admin', 'ErrorHandler', 'danger')
                    util.log.danger('>>>>>> 错误信息 >>>>>>')
                    console.log(info)
                    util.log.danger('>>>>>> Vue 实例 >>>>>>')
                    console.log(vm)
                    util.log.danger('>>>>>> Error >>>>>>')
                    console.log(err)
                }
            })
        }
    }
}
