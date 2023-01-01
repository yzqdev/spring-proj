 import axios from "./initHttp"

export function request(url, params, setTime) {
  // var vConsole = new VConsole();
  // console.log(vConsole);
return  axios.post(url,params)
//   //return instance.post(config.url,config.params);
//   // post方法：原理同get基本一样，但是要注意的是，post方法必须要使用对提交从参数对象进行序列化的操作，所以这里我们通过node的qs模块来序列化我们的参数。这个很重要，如果没有序列化操作，后台是拿不到你提交的数据的。这就是文章开头我们import QS from 'qs';的原因。
//   return new Promise((resolve, reject) => {
//     var t = "60000";
//     if (setTime != null) {
//       t = setTime + "";
//     }
//     // console.log("最终超时响应时间为："+t);
//     let timeout = parseInt(t);
//     // url = url + '?' + new Date().getTime() + Math.random() + Math.ceil(Math.random() * (10000 - 99999) + 99999);
//
//     app.config.globalProperties.$http
//       .post(url, params, { timeout: timeout })
//       .then((res) => {
//         resolve(res);
//       })
//       .catch((err) => {
//         reject(err);
//       });
//   });
}
