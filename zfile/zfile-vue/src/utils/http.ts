import axios from "axios";


export const baseConfig = (await axios.get("/config.json")).data;

axios.defaults.withCredentials = true;
import { useCookies } from "@vueuse/integrations/useCookies";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

let router = useRouter();
axios.defaults.baseURL = baseConfig.url;
axios.interceptors.request.use((config) => {
  let url: any = config.url;
  // get参数编码

  const { get, getAll, set, remove, addChangeListener, removeChangeListener } =
    useCookies(["satoken"], {
      doNotParse: false,
      autoUpdateDependencies: false,
    });

  let whiteList: Set<string> = new Set(["/is-installed", "/doLogin"]);
  let flat =
    config.url?.includes("api/drive/list") ||
    config.url?.includes("doLogin") ||
    config.url?.includes("installed") ||
    config.url?.includes("api/list");
  console.log(config.url);
  if (!flat) {
    if (!get("satoken")) {
      location.assign("/login");
      ElMessage({ type: "error", message: "登录过期!" });
    }
  }

  if (config.method === "get" && config.params) {
    url += "?";
    let keys = Object.keys(config.params);
    for (let key of keys) {
      url += `${key}=${encodeURIComponent(decodeURI(config.params[key]))}&`;
    }
    url = url.substring(0, url.length - 1);
    config.params = {};
  }
  config.url = url;
  return config;
});
// 添加响应拦截器
axios.interceptors.response.use(
  function (response) {
    // 对响应数据做点什么
    console.log("responseData==", response.data);
    return response;
  },
  function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);

export default axios;
