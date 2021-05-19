import service from '@/plugin/axios'

/**
 * 获取数据源列表
 */
export function page(params) {
    return service({
        url: 'y3tu-tool-lowcode/dataSource/page',
        method: 'post',
        data: params
    })
}

/**
 * 新增数据源
 */
export function create(params) {
    return service({
        url: 'y3tu-tool-lowcode/dataSource/create',
        method: 'post',
        data: params
    })
}

/**
 * 更新数据源
 */
export function update(params) {
    return service({
        url: 'y3tu-tool-lowcode/dataSource/update',
        method: 'post',
        data: params
    })
}

/**
 * 删除数据源
 */
export function del(id) {
    return service({
        url: `y3tu-tool-lowcode/dataSource/del/${id}`,
        method: 'get',
    })
}

/**
 * 测试数据源
 */
export function testConnect(id) {
    return service({
        url: `y3tu-tool-lowcode/dataSource/testConnect/${id}`,
        method: 'get',
    })
}




