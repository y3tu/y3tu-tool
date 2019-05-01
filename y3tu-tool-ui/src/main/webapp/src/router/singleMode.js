import Vue from 'vue';
import Router from 'vue-router'

Vue.use(Router);

export const constantRouterMap = [
    {path: '/404', component: () => import('@/views/error-page/404'), hidden: true},
    {path: '/401', component: () => import('@/views/error-page/401'), hidden: true},
    {path:'/cacheManager',component: () => import('@/views/cache/cacheManager'), hidden: true},
    {path:'/redisTerminal',component: () => import('@/views/monitor/redisTerminal'), hidden: true},

    {path: '*', redirect: '/404', hidden: true}
];

export default new Router({
    routes: constantRouterMap
})

