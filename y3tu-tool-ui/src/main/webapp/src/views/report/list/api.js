import service from '@/plugin/axios'

/**
 * 获取报表列表
 */
export function page(params) {
    return service({
        url: 'y3tu-tool-report/report/page',
        method: 'post',
        data: params
    })
}

/**
 * 新增报表
 */
export function create(params) {
    return service({
        url: 'y3tu-tool-report/report/create',
        method: 'post',
        data: params
    })
}

/**
 * 更新报表
 */
export function update(params) {
    return service({
        url: 'y3tu-tool-report/report/update',
        method: 'post',
        data: params
    })
}

/**
 * 根据数据源名称获取所有数据源
 * @param name
 */
export function getDataSourceByName(name) {
    return service({
        url: `y3tu-tool-report/dataSource/getByName/${name}`,
        method: 'get',
    })
}

/**
 * 根据名称和编码获取字典
 * @param data
 */
export function getDictByNameOrCode(data) {
    return service({
        url: `y3tu-tool-report/dict/getDictByNameOrCode/${data}`,
        method: 'get',
    })
}