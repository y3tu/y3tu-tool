import request from '@/plugin/axios'
import Qs from 'qs'

export function login(username, password) {

    let data = {
        loginUsername: username,
        loginPassword: password
    };

    return request({
        url: 'login.json',
        method: 'post',
        data: Qs.stringify(data)
    })
}

