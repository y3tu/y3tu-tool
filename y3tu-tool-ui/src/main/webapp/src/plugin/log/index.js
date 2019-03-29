import store from '@/store'
import util from '@/utils/util'
import setting from '@/setting'

export default {
  install (Vue, options) {
    // 快速打印 log
    Vue.prototype.$log = util.log
    // 快速记录日志
    Vue.prototype.$logAdd = function (info, show = true) {
      // store 赋值
      store.dispatch('log/add', {
        type: 'log',
        info
      })
      // 显示在控制台
      if (show && setting.env.development === true) {
        util.log.default(info)
      }
    }
  }
}
