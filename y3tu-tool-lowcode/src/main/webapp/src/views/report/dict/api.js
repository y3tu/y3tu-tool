import service from '@/plugin/axios'

/**
 * 获取字典列表
 */
export function dictPage(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/dictPage',
        method: 'post',
        data: params
    })
}

/**
 * 获取字典列表
 */
export function dictDataPage(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/dictDataPage',
        method: 'post',
        data: params
    })
}

/**
 * 根据字典code获取字典数据
 */
export function getDictDataByCode(code) {
    return service({
        url: `y3tu-tool-lowcode/dict/getDictDataByCode/${code}`,
        method: 'get',
    })
}


/**
 * 新增字典
 * @param params
 * @returns {*}
 */
export function createDict(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/createDict',
        method: 'post',
        data: params
    })
}

/**
 * 更新字典
 * @param data
 * @returns {*}
 */
export function updateDict(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/updateDict',
        method: 'post',
        data: params
    })
}

/**
 * 删除字典
 * @param id
 */
export function deleteDict(id) {
    return service({
        url: `y3tu-tool-lowcode/dict/deleteDict/${id}`,
        method: 'get',
    })
}

/**
 * 新增字典数据
 * @param params
 */
export function createDictData(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/createDictData',
        method: 'post',
        data: params
    })
}

/**
 * 更新字典数据
 * @param params
 * @returns {*}
 */
export function updateDictData(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/updateDictData',
        method: 'post',
        data: params
    })
}

/**
 * 删除字典数据
 * @param id
 */
export function deleteDictData(id) {
    return service({
        url: `y3tu-tool-lowcode/dict/deleteDictData/${id}`,
        method: 'get',
    })
}

export function saveDictSql(params) {
    return service({
        url: 'y3tu-tool-lowcode/dict/saveDictSql',
        method: 'post',
        data: params
    })
}

/**
 * 根据dictId获取字典sql数据
 * @param dictId
 */
export function getDictSql(dictId) {
    return service({
        url: `y3tu-tool-lowcode/dict/getDictSql/${dictId}`,
        method: 'get',
    })
}

/**
 * 获取所有数据源
 */
export function getAllDataSource() {
    return service({
        url: `y3tu-tool-lowcode/dataSource/getAll`,
        method: 'get',
    })
}



