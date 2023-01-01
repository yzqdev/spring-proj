// with polyfills



import App from './App.vue';
// import Antd from 'ant-design-vue';
// import 'ant-design-vue/dist/antd.min.css';
import ElementPlus from 'element-plus'
import "element-plus/dist/index.css"
import router from './router'
import store from './store/'
import { VueAxios } from './utils/request'
// WARNING: `mockjs` NOT SUPPORT `IE` PLEASE DO NOT USE IN `production` ENV.
import './mock'


import lazyUse from  './core/lazy_use'
import './permission' // permission control
import './utils/filter' // global filter
import '@/components/global.less'
import { Dialog } from '@/components/index'
import { hasBtnPermission } from './utils/permissions' // button permission
import { sysApplication } from './utils/applocation'
import { createApp } from 'vue'
import createAction from '@/core/directives/action'
import createProto from '@/utils/filter'

const app=createApp(App)
lazyUse(app)
createAction(app)
//添加ant-design-vue
app.use(ElementPlus)
//创建全局函数
createProto(app)
app.use(VueAxios)
app.use(Dialog)
app.config.globalProperties.hasPerm = hasBtnPermission
app.config.globalProperties.applocation = sysApplication
app.config.productionTip = false
app.use(router)
app.use(store)
// app.use(Antd)

// new Vue({
//   router,
//   store,
//   created: bootstrap,
//   render: h => h(App)
// }).$mount('#app')
app.mount('#app')