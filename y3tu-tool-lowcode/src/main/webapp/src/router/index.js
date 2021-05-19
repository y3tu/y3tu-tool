import {createRouter, createWebHashHistory} from 'vue-router';
import util from "@/utils";

const routes = [
    {
        path: '/login',
        name: '登录页',
        component: () => import('@/views/home/login.vue'),
    },
    {
        path: '/',
        name: '首页',
        redirect: '/home',
        component: () => import('@/views/layout/index.vue'),
        children: [
            {
                path: '/home',
                name: 'home',
                component: () => import('@/views/home/index.vue'),
                meta: {
                    keepAlive: true,
                }
            }
        ]
    },
    {
        path: '/report',
        name: 'layout',
        redirect: '/report/home',
        component: () => import('@/views/layout/index.vue'),
        children: [
            {
                path: '/report/home',
                name: 'reportHome',
                component: () => import('@/views/report/home/index.vue'),
                meta: {
                    keepAlive: true,
                }
            },
            {
                path: '/report/dataSource',
                name: 'dataSource',
                component: () => import('@/views/report/dataSource/index.vue'),
                meta: {
                    keepAlive: true,
                }
            },
            {
                path: '/report/dict',
                name: 'dict',
                component: () => import('@/views/report/dict/index.vue'),
                meta: {
                    keepAlive: false,
                }
            },
            {
                path: '/report/reportDownload',
                name: 'reportDownload',
                component: () => import('@/views/report/download/index.vue'),
                meta: {
                    keepAlive: false,
                }
            }
        ]
    },
    {
        path: '/report/publish',
        name: 'publish',
        component: () => import('@/views/report/home/publish.vue'),
        meta: {
            keepAlive: false,
        }
    },
    {
        path: '/cache',
        name: 'cache',
        redirect: '/cache/home',
        component: () => import('@/views/layout/index.vue'),
        children: [
            {
                path: '/cache/home',
                name: 'cache',
                component: () => import('@/views/cache/index.vue'),
                meta: {
                    keepAlive: true,
                }
            }
        ]
    },
    {
        path: '/visual',
        component: () => import('@/views/visual-design/home/index.vue'),
    },
    {
        path: '/test',
        component: () => import('@/views/test/index.vue'),
    },
    {
        path: '/404',
        hidden: true,
        component: () => import('@/views/error-page/404'),
    },
    {
        path: '/401',
        hidden: true,
        component: () => import('@/views/error-page/401'),
    },
    {
        path: '/:catchAll(.*)',
        redirect: '/404',
        hidden: true,
    },
];


let router = createRouter({
    history: createWebHashHistory(process.env.VUE_APP_BASE_API),
    routes,
});

const whiteList = ['/login','/blog'];

// 导航守卫，渲染动态路由
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }

    let whiteFlag = false;

    whiteList.forEach(function (white) {
        if (to.path.indexOf(white) !== -1) {
            //白名单直接放行
            whiteFlag = true;
        }
    });

    if(whiteFlag){
        next()
    }else {
        const token = util.cookies.get('ACCESS_TOKEN');
        if (token&&token.length) {
            next()
        } else {
            if (to.path === '/login') {
                next()
            } else {
                next('/login')
            }
        }
    }
});

export default router