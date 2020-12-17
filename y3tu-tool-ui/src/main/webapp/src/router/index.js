import {createRouter, createWebHashHistory} from 'vue-router';

const routes = [
    {
        path: '/',
        component: () => import('@/views/cache/index.vue'),
    },
    {
        path: '/reportList',
        component: () => import('@/views/report/reportList/index.vue'),
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