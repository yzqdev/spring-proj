// with polyfills
import 'core-js/stable'
import 'regenerator-runtime/runtime'
import   { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/'
import { VueAxios } from './utils/request'
// WARNING: `mockjs` NOT SUPPORT `IE` PLEASE DO NOT USE IN `production` ENV.

import 'ant-design-vue/dist/antd.css';
import Antd from 'ant-design-vue';
import lazyUse from  './core/lazy_use'
import './permission' // permission control
import './utils/filter' // global filter
import './components/global.less'
import { Dialog } from '@/components'
import { hasBtnPermission } from './utils/permissions' // button permission
import { sysApplication } from './utils/applocation'
import createProto from '@/utils/filter'
import createAction from '@/core/directives/action'
const app=createApp(App)
//创建全局函数
app.use(Antd)
createAction(app)
lazyUse(app)
createProto(app)
app.use(VueAxios)
app.use(Dialog)
app.config.globalProperties.hasPerm = hasBtnPermission
app.config.globalProperties.applocation = sysApplication
app.config.productionTip = false
app.use(router)
app.use(store)



app.mount('#app')
