import Vue from 'vue'
import Vuex from 'vuex'
import home from './modules/home'
import user from './modules/user'
import theme from './modules/theme'
import loader from './modules/loader'
import pan from './modules/pan'
Vue.use(Vuex);

//实例化
const store = new Vuex.Store({
    modules: {
        home,
        user,
        theme,
        loader,
        pan
    }
})
export default store