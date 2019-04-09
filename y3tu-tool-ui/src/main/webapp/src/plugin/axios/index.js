import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import store from '@/store'
import setting from '@/setting'
import util from '@/utils/util'

// 创建axios实例
const service = axios.create({
    baseURL: setting.env.apiURL,
    withCredentials: true, // 跨域请求，允许保存cookie
    timeout: 15000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
    if (store.getters.token) {

    }
    return config
}, error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
    response => {
        const data = response.data;
        switch (data.errorCode) {
            case '401':
                // 未登录 清除已登录状态
                MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    store.dispatch('FedLogOut').then(() => {
                        location.reload()// 为了重新实例化vue-router对象 避免bug
                    })
                });
                break;
            case '403':
                if (data.errorMessage !== null) {
                    Message.error(data.errorMessage);
                } else {
                    Message.error("未知错误");
                }
                break;
            case '500':
                // 错误
                if (data.errorMessage !== null) {
                    Message.error(data.errorMessage);
                } else {
                    Message.error("未知错误");
                }
                break;
            default:
                return data;
        }

    },
    error => {
        console.log('err' + error)// for debug
        Message({
            message: error.message,
            type: 'error',
            duration: 3 * 1000
        })
        return Promise.reject(error)
    }
)

export default service
