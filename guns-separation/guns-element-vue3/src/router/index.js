
import   { createRouter, createWebHistory } from 'vue-router'
import { constantRouterMap } from '@/config/router.config'

// hack router push callback
// const originalPush = Router.prototype.push
// Router.prototype.push = function push (location, onResolve, onReject) {
//   if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
//   return originalPush.call(this, location).catch(err => err)
// }



export default createRouter({
 history:createWebHistory(),
  // base: process.env.BASE_URL,
  routes: constantRouterMap,
  scrollBehavior: () => ({ y: 0 }),

})
