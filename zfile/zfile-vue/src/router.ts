import {
  createRouter,
  createWebHashHistory,
  createWebHistory,
} from "vue-router";

const Install = () => import("./views/Install.vue");
const Main = () => import("./views/Main.vue");

const Admin = () => import("./views/admin/Index.vue");
const Login = () => import("./views/Login.vue");
const SiteSetting = () => import("./views/admin/SiteSetting.vue");
const ViewSetting = () => import("./views/admin/ViewSetting.vue");
const UpdatePassword = () => import("./views/admin/UpdatePassword.vue");
const API = () => import("./views/admin/API.vue");
const DriveList = () => import("./views/admin/DriveList.vue");
const ShortLink = () => import("./views/admin/ShortLink.vue");
const SharePointSiteId = () => import("./views/SharePointSiteId.vue");
const TestPage = () => import("./views/TestPage.vue");
export default createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect:{name:'home'}
      // redirect: to => {
      //   // 方法接收目标路由作为参数
      //   // return 重定向的字符串路径/路径对象
      //   return { path: '/home/:driveId?*',params: { driveId: to.params.driveId } }
      // },
    },

    {
      path: "/test",
      name: "test",
      component: TestPage,
    },
    {
      path: "/install",
      component: Install,
    },
    {
      path: "/home/:driveId?",
      component: Main,
      name: "home",
      // props: true,
    },

    {
      path: "/login",
      name:'login',
      component: Login,
    },
    {
      path: "/sharepoint-util",
      component: SharePointSiteId,
    },
    {
      path: "/admin",
      component: Admin,
      children: [
        {
          path: "",
          name: "adminIndex",

          component: SiteSetting,
        },
        {
          path: "site",
          name: "基本设置",
          component: SiteSetting,
        },
        {
          path: "view",
          name: "显示设置",
          component: ViewSetting,
        },
        {
          path: "password",
          name: "密码设置",
          component: UpdatePassword,
        },
        {
          path: "api",
          name: "API",
          component: API,
        },
        {
          path: "drive-list",
          name: "DriveList",
          component: DriveList,
        },
        {
          path: "short-link",
          name: "ShortLink",
          component: ShortLink,
        },
      ],
    },
  ],
});
