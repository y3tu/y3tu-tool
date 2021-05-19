import {service, download} from '@/plugin/axios'

/**
 * 获取报表生成列表
 */
export function page(params) {
    return service({
        url: 'y3tu-tool-lowcode/reportDownload/page',
        method: 'post',
        data: params
    })
}

export function handleAgain(id) {
    return service({
        url: `y3tu-tool-lowcode/reportDownload/handleAgain/${id}`,
        method: 'get',
    })
}

export function downloadFile(id, fileName) {
    download(`y3tu-tool-lowcode/reportDownload/download/${id}`, fileName);
}







