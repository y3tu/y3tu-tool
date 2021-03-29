import axios from 'axios'
import toast from '@/utils/toast'

// 请求超时时间，10s
const requestTimeOut = 30 * 1000;
// 成功状态
const success = 200;
// 提示信息显示时长 3秒
const messageDuration = 3 * 1000;

export const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: requestTimeOut,
    // 跨域请求，允许保存cookie
    withCredentials: true,
    responseType: 'json',
    validateStatus(status) {
        return status === success
    }
});

//请求拦截
service.interceptors.request.use(
    config => {
        let _config = config;
        try {
            //请求拦截处理
        } catch (e) {
            console.error(e)
        }
        return _config
    },
    error => {
        console.log(error);
        return Promise.reject(error)
    }
);

//响应拦截
service.interceptors.response.use(response => {
    if (response.data !== undefined && response.data !== null && response.data !== '') {
        let status = response.data.status;
        if (status !== undefined && status === "ERROR") {
            toast(response.data.message, 'error', 5 * 1000)
            throw new Error(response.data.message);
        }

        if (status !== undefined && status === 'WARN') {
            toast(response.data.message, 'warning', 5 * 1000)
            throw new Error(response.data.message);
        }
    }
    return response.data;
}, (error) => {
    if (error) {
        if (error.toString().indexOf('Error: timeout') !== -1) {

            toast('网络请求超时', 'error', 5 * 1000)
            return Promise.reject(error)
        }
        if (error.toString().indexOf('Error: Network Error') !== -1) {

            toast('网络请求错误', 'error', 5 * 1000)
            return Promise.reject(error)
        }

        if (error.toString().indexOf('503') !== -1) {

            toast('服务暂时不可用，请稍后再试!', 'error', 5 * 1000)
            return Promise.reject(error)
        }

        if (error.response.data instanceof Blob) {
            return Promise.reject(error)
        }

        const errorMessage = error.response.data === null ? '系统内部异常，请联系网站管理员' : error.response.data.message;
        switch (error.response.status) {
            case 404:
                toast('很抱歉，资源未找到!', 'error', 5 * 1000)
                break;
            case 403:
                toast('很抱歉，您暂无该操作权限!', 'error', 5 * 1000)
                break;
            case 401:
                toast('很抱歉，您没有权限!', 'error', 5 * 1000)
                break;
            default:
                toast(errorMessage, 'error', messageDuration)
                break
        }
    }
    return Promise.reject(error)
});

export function downloadPost(url,data,fileName){
    return service.post(url, data,{
        responseType: 'blob',
        timeout: 600000,
    }).then((response) => {
        //处理返回的文件流
        let blob = new Blob([response], {type: 'application/octet-stream'});
        if (window.navigator.msSaveOrOpenBlob) {// 兼容IE10
            navigator.msSaveBlob(blob, fileName);
        } else {// 其他非IE内核支持H5的浏览器
            let url = window.URL.createObjectURL(blob);
            let link = document.createElement('a');
            link.style.display = 'none';
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click()
        }
    }).catch((r) => {
        console.error(r);
        this.$message({
            message: '下载失败,文件已删除或移动到其他位置，请检查！',
            type: 'error',
            duration: messageDuration
        })
    })
}

export function download(url, fileName) {
    return service.get(url, {
        responseType: 'blob',
        timeout: 600000,
    }).then((response) => {
        //处理返回的文件流
        let blob = new Blob([response], {type: 'application/octet-stream'});
        if (window.navigator.msSaveOrOpenBlob) {// 兼容IE10
            navigator.msSaveBlob(blob, fileName);
        } else {// 其他非IE内核支持H5的浏览器
            let url = window.URL.createObjectURL(blob);
            let link = document.createElement('a');
            link.style.display = 'none';
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click()
        }
    }).catch((r) => {
        console.error(r);
        this.$message({
            message: '下载失败,文件已删除或移动到其他位置，请检查！',
            type: 'error',
            duration: messageDuration
        })
    })
}

export default service