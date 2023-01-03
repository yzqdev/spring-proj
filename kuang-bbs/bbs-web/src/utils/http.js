

import axios from 'axios'
axios.interceptors.request.use((config) => {
    console.log('requestUrl==',config.url)
    console.log('requestData==', config.data)
    return config
},(error) => {
    return Promise.reject(error)
})
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    console.log('responseData==',response.data)
    return response;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});
axios.defaults.baseURL= 'http://localhost:8301'
export default axios
