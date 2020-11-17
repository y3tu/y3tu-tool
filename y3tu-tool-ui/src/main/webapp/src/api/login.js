import request from '@/plugin/axios'


export const login=(params)=>{
    return request({
        url: 'login.json',
        method: 'post',
        params: params
    });
};


