import request from '@/plugin/axios'

export function createDict(data) {
    return request.post('base/dict/createDict',data);
}

export function updateDict(data) {
    return request.put('base/dict/updateDict',data);
}

export function deleteDict(ids) {
    return request.deleteData('base/dict/deleteDict',ids)
}

export function createDictData(data) {
    return request.post('base/dict/createDictData',data);
}

export function deleteDictData(ids) {
    return request.deleteData('base/dict/deleteDictData',ids)
}

export function updateDictData(data) {
    return request.put('base/dict/updateDictData',data);
}

export function saveDictSql(data) {
    return request.post('base/dict/saveDictSql',data);
}

export function getDictDataByDictName(name) {
    return request.get('base/dict/getDictDataByDictName/'+name);
}

export function getDictData(code) {
    return request.get('base/dict/getDictData/'+code);
}

export function getDictSql(dictId) {
    return request.get('base/dict/getDictSql/'+dictId);
}




