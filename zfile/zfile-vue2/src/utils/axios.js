import axios from 'axios'
axios.interceptors.request.use(
    config => {
        let url = config.url;
        // get参数编码
        if (config.method === 'get' && config.params) {
            url += '?';
            let keys = Object.keys(config.params);
            for (let key of keys) {
                url += `${key}=${encodeURIComponent(decodeURI(config.params[key]))}&`
            }
            url = url.substring(0, url.length - 1);
            config.params = {}
        }
        config.url = url;
        return config
    },
);
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    console.log('responseData==',response.data)
    return response;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});
axios.defaults.baseURL= 'http://192.168.0.105:9101'
export default axios
