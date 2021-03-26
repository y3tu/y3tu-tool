import {service, download,downloadPost} from '@/plugin/axios'

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
 * 根据id获取report
 */
export function getByName(name) {
    return service({
        url: `y3tu-tool-report/report/getByName/${name}`,
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
export function parseSqlForHeader(params) {
    return service({
        url: 'y3tu-tool-report/report/parseSqlForHeader',
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
 */
export function getAllDict() {
    return service({
        url: `y3tu-tool-report/dict/getAllDict`,
        method: 'get',
    })
}

/**
 * 查询报表数据
 */
export function reportHtml(params) {
    return service({
        url: `y3tu-tool-report/report/reportHtml`,
        method: 'post',
        data:params
    })
}

/**
 * 判断是否是大数据量报表
 */
export function isBigData(params){
    return service({
        url: `y3tu-tool-report/report/isBigData`,
        method: 'post',
        data:params
    })
}


/**
 * 导出报表数据excel
 */
export function exportExcel(params,fileName) {
    return downloadPost('y3tu-tool-report/report/exportExcel',params,fileName)
}