// Vuetify Documentation https://vuetifyjs.com

import Vue from 'vue'
import Vuetify from 'vuetify/lib/framework'
import ripple from 'vuetify/lib/directives/ripple'
import zhHans from 'vuetify/es5/locale/zh-Hans'   // 引入中文语言包
import 'typeface-roboto/index.css'    // 引入本地的Roboto字体资源
import '@mdi/font/css/materialdesignicons.css'  // 引入本地的Material Design Icons资源


Vue.use(Vuetify, { directives: { ripple } })

const theme = {
    primary: '#E91E63',
    secondary: '#9C27b0',
    accent: '#e91e63',
    info: '#00CAE3',
    success: '#4CAF50',
    warning: '#FB8C00',
    error: '#FF5252',
}

export default new Vuetify({
    lang:{
        locales: {zhHans},
        current: 'zhHans'
      },
    breakpoint: { mobileBreakpoint: 960 },
    icons: {
        iconfont: 'mdi',
        values: { expand: 'mdi-menu-down' },
    },
    theme: {
        themes: {
            dark: theme,
            light: theme,
        },
    },
})