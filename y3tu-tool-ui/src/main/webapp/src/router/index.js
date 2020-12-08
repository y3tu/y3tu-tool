import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        path: '/cache',
        hidden: true,
        component: () => import('@/views/cache/index.vue'),
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
    history: createWebHistory(),
    routes,
    strict: true,
});

export default router