import router from './router'
// import { ElMessage } from 'element-plus'
// import NProgress from 'nprogress'
// import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
// import { isHttp } from '@/utils/validate'
import useUserStore from '@/store/modules/user'
import { isRelogin } from '@/utils/myaxios'
// import useSettingsStore from '@/store/modules/settings'
// import usePermissionStore from '@/store/modules/permission'

// NProgress.configure({ showSpinner: false });

const whiteList = ['/login', '/auth-redirect', '/bind', '/register'];

router.beforeEach((to, from, next) => {
  // NProgress.start()
  if (getToken()) {
    // to.meta.title && useSettingsStore().setTitle(to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      // NProgress.done()
    } else {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true
        useUserStore().getInfo().then((response: any) => {
          next()
        });

      }
      else
      {
        next()
      }
    }
  } else {

    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      // NProgress.done()
    }
  }
})

router.afterEach(() => {
  // NProgress.done()
})
