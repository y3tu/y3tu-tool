import {service, download} from '@/plugin/axios'

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
 * 根据id获取report
 */
export function get(id) {
    return service({
        url: `y3tu-tool-report/report/get/${id}`,
        method: 'get',
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
 * 删除报表
 */
export function del(id) {
    return service({
        url: `y3tu-tool-report/report/delete/${id}`,
        method: 'get',
    })
}

/**
 * 下载
 */
export function downloadFile(id, fileName) {
    download(`y3tu-tool-report/report/download/${id}`, fileName);
}

/**
 * 解析SQL语句
 */
export function parseSql(params) {
    return service({
        url: 'y3tu-tool-report/report/parseSql',
        method: 'post',
        data: params
    })
}

/**
 * 获取所有数据源
 */
export function getAllDataSource() {
    return service({
        url: `y3tu-tool-report/dataSource/getAll`,
        method: 'get',
    })
}

/**
 * 获取所有字典
 * @param data
 */
export function getAllDict() {
    return service({
        url: `y3tu-tool-report/dict/getAllDict`,
        method: 'get',
    })
}