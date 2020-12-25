import {ElMessage} from 'element-plus'

export default function toast(msg = '', type, duration) {
    const data = {
        message: msg,
    }
    if (type) data.type = type
    if (duration) data.duration = duration
    ElMessage(data)
}