import Vue from "vue";

import Cookies from "js-cookie";

import "normalize.css/normalize.css";

import Element from "element-ui";
//
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";

// 数据字典
import dict from "./components/Dict";

// 权限指令
import checkPer from "@/utils/permission";
import permission from "./components/Permission";
import "./assets/styles/element-variables.scss";
// global css
import "./assets/styles/index.scss";

// 代码高亮

import "highlight.js/styles/atom-one-dark.css";

import App from "./App";
import store from "./store";
import router from "./router/routers";
import hljs from "highlight.js/lib/core";
import javascript from "highlight.js/lib/languages/javascript";
import vuePlugin from "@highlightjs/vue-plugin";

hljs.registerLanguage("javascript", javascript);
import "./assets/icons"; // icon
import "./router/index"; // permission control
import "echarts-gl";

Vue.use(checkPer);
Vue.use(vuePlugin);
Vue.use(mavonEditor);
Vue.use(permission);
Vue.use(dict);
Vue.use(Element, {
  size: Cookies.get("size") || "small", // set element-ui default size
});

Vue.config.productionTip = false;

new Vue({
  el: "#app",
  router,
  store,
  render: (h) => h(App),
});
