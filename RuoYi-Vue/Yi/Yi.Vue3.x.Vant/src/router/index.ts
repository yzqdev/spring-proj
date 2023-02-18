import { createWebHistory, createRouter } from 'vue-router';
import Layout from '@/layout/index.vue';
import HeadLayout from '@/layout/head/index.vue'

export const constantRoutes = [

    {
      name:'Layout',
      path: '/',
      component: Layout,
      redirect:"/recommend",
      children: [
        {
          path: '/shopIndex',
          component: () => import('@/view/shop/shopIndex.vue'),
          name: 'ShopIndex',
        },
        {
          path: '/my',
          component: () => import('@/view/my.vue'),
          name: 'My',
        },
        {
          path: '/main',
          component: () => import('@/layout/main/index.vue'),
          name: 'Main',
          children:[
            {
              path: '/recommend',
              component: () => import('@/view/main/recommend.vue'),
              name: 'Recommend',
            },
            {
              path: '/follow',
              component: () => import('@/view/main/follow.vue'),
              name: 'Follow',
            },
            {
              path: '/square',
              component: () => import('@/view/main/square.vue'),
              name: 'Square',
            },
            
          ]
        }
      ]
    },
    {
      path: '/imageText',
      component: () => import('@/view/send/imageText.vue'),
      name: 'ImageText',
    },
    {
      path: '/login',
      component: () => import('@/view/login.vue'),
      name: 'Login',
    },

    {
      name:'Shop',
      path: '/shop',
      component: HeadLayout,
      redirect:"/shopIndex",
      children: [
        {
          path: '/shopDetails',
          component: () => import('@/view/shop/shopDetails.vue'),
          name: 'ShopDetails',
        },
        {
          path: '/shopSearch',
          component: () => import('@/view/shop/shopSearch.vue'),
          name: 'ShopSearch',
        },
      ]
    }
  ];

  const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes,
    scrollBehavior(to, from, savedPosition) {
      if (savedPosition) {
        return savedPosition
      } else {
        return { top: 0 }
      }
    },
  });
  
  export default router;
  