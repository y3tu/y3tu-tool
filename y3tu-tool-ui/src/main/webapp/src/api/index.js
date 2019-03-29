import request from '@/plugin/axios'

// IP天气信息
export const ipInfo = (params) => {
    return request({
        url: '/common/ip/info',
        method: 'get',
        params: params
    });
};


// 文件上传接口
export const uploadFile = "/xboot/upload/file"
// 验证码渲染图片接口
export const drawCodeImage = "/xboot/common/captcha/draw/"