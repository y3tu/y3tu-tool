import Vue from 'vue';
import Router from 'vue-router'

Vue.use(Router);

/* Layout */
import Layout from '../views/layout/Layout'

export const constantRouterMap = [
    {path: '/login', component: () => import('@/views/login/index'), hidden: true},
    {path: '/404', component: () => import('@/views/error-page/404'), hidden: true},
    {path: '/401', component: () => import('@/views/error-page/401'), hidden: true},
    {
        path: '',
        component: Layout,
        redirect: '/home',
        children: [{
            path: 'home',
            name: 'home',
            component: () => import('@/views/home/index'),
            meta: {title: '首页', icon: 'home'}
        }]
    },
    {
        path: '/monitor',
        component: Layout,
        redirect: '/monitor/home',
        name: 'monitor',
        meta: {title: '监控', icon: 'eye'},
        children: [
            {
                path: 'redisMonitor',
                name: 'redisMonitor',
                component: () => import('@/views/monitor/redisMonitor'),
                meta: {title: 'redis监控', icon: 'random'}
            },
            {
                path: 'memoryMonitor',
                name: 'memoryMonitor',
                component: () => import('@/views/monitor/memoryMonitor'),
                meta: {title: '内存监控', icon: 'camera'}
            },
        ]
    },
    {
        path: '/cache',
        component: Layout,
        redirect: '/cache/cacheManager',
        name: 'cache',
        meta: {title: '缓存', icon: 'database'},
        children: [
            {
                path: 'cacheManager',
                name: 'cacheManager',
                component: () => import('@/views/cache/cacheManager'),
                meta: {title: '缓存管理', icon: 'database'}
            },
        ]
    },


    {path: '*', redirect: '/404', hidden: true}
];

export default new Router({
    routes: constantRouterMap
})

