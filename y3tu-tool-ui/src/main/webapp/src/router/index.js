import Vue from 'vue';
import Router from 'vue-router'

Vue.use(Router);

/* Layout */
import Layout from '../views/layout/Layout'

export const constantRouterMap = [
    {path: '/login', component: () => import('@/views/login/index'), hidden: true},
    {path: '/404', component: () => import('@/views/error-page/404'), hidden: true},
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
        redirect: '/monitor',
        name: 'monitor',
        meta: {title: '监控', icon: 'product'},
        children: [
            {
                path: 'monitor',
                name: 'monitor',
                //component: () => import('@/views/login/index'),
                meta: {title: 'redis监控', icon: 'product-list'}
            },


        ]
    },

    {path: '*', redirect: '/404', hidden: true}
];

export default new Router({
    routes: constantRouterMap
})

