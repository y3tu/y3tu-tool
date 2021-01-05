import {createRouter, createWebHashHistory} from 'vue-router';

const routes = [
    {
        path: '/',
        component: () => import('@/views/home/index.vue'),
    },
    {
        path: '/report',
        name: 'layout',
        redirect: '/report/list',
        component: () => import('@/views/layout/index.vue'),
        children: [
            {
                path: '/report/list',
                name: 'list',
                component: () => import('@/views/report/list/index.vue'),
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
            }
        ]
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

export default router