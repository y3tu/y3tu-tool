const setting = {

    //基础配置
    env: {
        //后端url
        apiURL: '',
        //apiURL: 'http://127.0.0.1:8081/y3tu-tool-ui/',
        // 前端web前缀
        baseURL: '',
        //开发模式
        development: true,
        //是否使用登录加侧导航模式 (默认false)
        loginMode: false
    },

    // 侧边栏默认折叠状态
    menu: {
        asideCollapse: false
    },
    //版本
    releases: {
        version: '2.0',
        api: 'https://github.com/y3tu/y3tu-tool',
        name: 'y3tu-tool'
    },
    //是否默认开启页面切换动画
    transition: {
        active: true
    },
    //在读取持久化数据失败时默认用户信息
    user: {
        info: {
            username: 'Ghost'
        }
    }
};

export default setting