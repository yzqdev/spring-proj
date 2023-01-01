<template>
  <!--  -->
  <div class="app" id="app">
    <div v-if=" !auth">
      <div
        style="
          width: 100%;
          height: 100px;
          text-align: center;
          margin-top: 150px;
        "
      >
        <h1>{{  authInfo }}</h1>
      </div>
    </div>
    <router-view v-else></router-view>
  </div>
</template>

<script setup lang="ts">
import VConsole from "vconsole";
import {useGlobalStore} from "@/store/g";
import {onBeforeMount, onMounted} from "vue";
import {request} from "@/network/request";
import {checkStatus} from "@/network/apis";
import {useRoute, useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import axios from "axios";
let gStore=useGlobalStore()
let router=useRouter()
let route=useRoute()
function loadScriptString(code) {
  let script = document.createElement("script");
  script.type = "text/javascript";
  try {
    // firefox、safari、chrome和Opera
    script.appendChild(document.createTextNode(code));
  } catch (ex) {
    // IE早期的浏览器 ,需要使用script的text属性来指定javascript代码。
    script.text = code;
  }
  document.getElementsByTagName("head")[0].appendChild(script);
}
let auth=computed(() => {
  return gStore.state.auth
})
let authInfo=computed(() => {
  return gStore.state.authInfo
})
function getWebInfo() {
  return new Promise((resolve, reject) => {
    axios
        .get("/webInfo")
        .then((data) => {
          let json = data.data.data;
          if (json) {
            json.splitline = "-";
            loadScriptString(json.baidu);
            gStore.changeMetaInfo(json)
          }
          resolve();
        })
        .catch((error) => {
          console.log(error);
          reject();
        });
  });
}
 function checkLogin() {
   checkStatus()
       .then((res) => {
         if (res.status == 200) {
           var json = res.data;
           if (json.code == "200") {
             gStore.state.loginStatus = true;
             // this.$store.state.RoleLevel = json.data.RoleLevel;
             localStorage.setItem("RoleLevel", json.data.RoleLevel);
             localStorage.setItem("userName", json.data.userName);
             gStore.setUserName(json.data.userName)
             if (
                route.path== "/login" ||
                  route.path == "/register"
             ) {
               router.replace("/");
             }
           } else {
            gStore.state.loginStatus = false;
             ElMessage({type:'info',message:'退出登录'})
             localStorage.removeItem("Authorization");
             localStorage.removeItem("RoleLevel");
            router.replace("");
           }
         } else {
           ElMessage({type:'error',message:"请求时出现错误"})
         }
       })
       .catch((err) => {
         console.log(err);
         // this.reloadCode();
         ElMessage({type:"error",message:"服务器请求错误"})
       });
 }
 onBeforeMount(() => {
   getWebInfo()
 })
 onMounted(() => {
   if (gStore.state.auth) {
     // var vConsole = new VConsole();
     // console.log(vConsole);
     // this.getWebInfo();
     checkLogin();
   }

 })


</script>

<style lang="less">
html,
body,
.app {
  height: 100%;
  /*overflow: hidden;*/
}
.QRCodestyle {
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    height: 160px;
    width: 160px;
    opacity: 0.7;
  }
}
.app {
  font-family: JetBrainsMono, Noto Sans SC, "Helvetica Neue", Helvetica,
    "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial,
    sans-serif;
  font-style: normal;
  font-weight: 400;
}
</style>
