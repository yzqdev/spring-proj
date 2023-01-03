import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/Home.vue";
import HomePage from "@views/HomePage.vue";
import HomeQuestion from "@views/HomeQuestion.vue";
import UserCenter from "@views/UserCenter.vue";
import UserInfo from "@views/UserInfo.vue";
import Login from "@views/Login.vue";
import Register from "@views/Register.vue";

Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        redirect: '/home/home'
    },
    {
        path: '/home',
        name: 'home',
        component: Home,
        children: [
            {
                path: 'home',
                name: 'HomePage',
                component: HomePage
            }, {
                path: 'question',
                name: 'HomeQuestion',
                component: HomeQuestion
            }, {
                path: 'blog',
                name: 'HomeBlog',
                component: HomePage
            }, {
                path: 'resource',
                name: 'HomeResource',
                component: HomePage
            }, {
                path: 'notice',
                name: 'HomeNotice',
                component: HomePage
            }, {
                path: 'user-center',
                name: 'UserCenter',
                component: UserCenter
            }, {
                path: 'user-info',
                name: 'UserInfo',
                component: UserInfo
            }
        ]
    },
    {
        path: "/about",
        name: "About",
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () =>
            import(/* webpackChunkName: "about" */ "@/components/About.vue")
    }, {
        path: '/login',
        name: 'Login',
        component: Login
    },{
        path: '/register',
        name: 'Login',
        component: Register
    },
];

const router = new VueRouter({
    mode: 'history',
    routes
});

export default router;
