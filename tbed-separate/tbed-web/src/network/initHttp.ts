import axios from "axios";
import md5 from "js-md5";
import {useGlobalStore} from "@/store/g";
axios.defaults.withCredentials = false;
axios.defaults.timeout = 60000;
axios.defaults.method = "POST";
axios.defaults.headers = {
    usersOrigin: md5(window.location.protocol + "//" + window.location.host),
};

//注册拦截器
axios.interceptors.request.use(
    (config) => {
        config.headers.usersOrigin = md5(
            window.location.protocol + "//" + window.location.host
        );
        let gStore=useGlobalStore()
        if (gStore.Authorization) {
            config.headers.Authorization = localStorage.getItem("Authorization");
        } else if (localStorage.getItem("Authorization")) {
            config.headers.Authorization = localStorage.getItem("Authorization");
        }
        return config;
    },
    (error) => {
        console.log("拦截器-请求错误：" + error);
    }
);

axios.interceptors.response.use(
    (config) => {
        let gStore=useGlobalStore()
        gStore.state.auth = true;
        gStore.state.authInfo = "";
        if (config.data.code == 406 || config.data.code == "406") {
            console.log("前端域名配置错误");
            gStore.state.authInfo = "前端域名配置错误";
            gStore.state.authInfo = "";
            gStore.state.auth = false;
        } else if (config.data.code == 403) {
            location.replace("/login");
        }
        return config;
    },
    (error) => {
        console.log("拦截器-相应错误：" + error);
    }
);
axios
    .get(
        "/hellohao/config.json" +
        "?" +
        new Date().getTime() +
        Math.random() +
        Math.ceil(Math.random() * (10000 - 99999) + 99999)
    )
    .then(async (data) => {
        var json = data.data;
        gStore.setServerHost(json.serverHost)
        axios.defaults.baseURL = json.serverHost;

    });
export default axios
