import {login} from '@/api/login'
import util from '@/utils/util'

const user = {
    state: {
        token: util.cookies.get("token"),
        name: '',
        avatar: '',
        roles: []
    },

    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_NAME: (state, name) => {
            state.name = name
        },
    },

    actions: {
        // 登录
        Login({commit}, userInfo) {
            const username = userInfo.username.trim()
            return new Promise((resolve, reject) => {

                let params = {
                    loginUsername: username,
                    loginPassword: userInfo.password
                };

                login(params).then(response => {
                    const tokenStr = response.data;
                    util.cookies.set("token", tokenStr);
                    commit('SET_TOKEN', tokenStr)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // 登出
        LogOut({commit, state}) {
            return new Promise((resolve, reject) => {
                util.cookies.remove("token");
                location.reload()
            })
        },

        // 前端 登出
        FedLogOut({commit}) {
            return new Promise(resolve => {
                commit('SET_TOKEN', '')
                util.cookies.remove("token");
                resolve()
            })
        }
    }
}

export default user
