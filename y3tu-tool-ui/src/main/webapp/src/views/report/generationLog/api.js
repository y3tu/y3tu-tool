import service from '@/plugin/axios'

/**
 * 获取报表生成列表
 */
export function page(params) {
    return service({
        url: 'y3tu-tool-report/generationLog/page',
        method: 'post',
        data: params
    })
}





