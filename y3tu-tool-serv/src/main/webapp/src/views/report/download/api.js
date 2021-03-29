import {service, download} from '@/plugin/axios'

/**
 * 获取报表生成列表
 */
export function page(params) {
    return service({
        url: 'y3tu-tool-report/reportDownload/page',
        method: 'post',
        data: params
    })
}

export function downloadFile(id, fileName) {
    download(`y3tu-tool-report/reportDownload/download/${id}`, fileName);
}






