import router from './router'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import util from '@/utils/util'


const whiteList = ['/login', '/401', '/404'] // 不重定向白名单
router.beforeEach((to, from, next) => {
    NProgress.start()
    if (util.cookies.get('token')) {
        if (to.path === '/login') {
            next({path: '/'})
            NProgress.done();
        } else {
            next();
        }
    } else {
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            next('/login');
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    NProgress.done() // 结束Progress
});
