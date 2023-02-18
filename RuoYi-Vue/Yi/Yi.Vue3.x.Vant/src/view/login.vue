<template>
  <div class="div-top">
    <span class="title">意框架</span>
    <br />
    <span class="subtitle">有幸相遇、不负未来</span>
  </div>
  <div class="div-bottom">
    <h5>密码登录</h5>
    <van-field
      class="van-field-username"
      v-model="loginForm.username"
      label="用户"
      placeholder="请输入用户名"
    />
    <van-field
      class="van-field-password"
      v-model="loginForm.password"
      label="密码"
      type="password"
      placeholder="请输入密码"
    />
    <van-button type="primary" @click="login">进入意框架</van-button>
    <p>其他方式登录<van-icon name="arrow" /></p>

    <van-row class="row-bottom" style="margin-top: 6rem">
      <van-col span="24"><p>第三方登录</p></van-col>
      <van-col span="2"></van-col>
      <van-col span="5"><van-icon name="smile-o" size="2rem" /></van-col>
      <van-col span="5"><van-icon name="smile-o" size="2rem" /></van-col>
      <van-col span="5"><van-icon name="smile-o" size="2rem" /></van-col>
      <van-col span="5"><van-icon name="smile-o" size="2rem" /></van-col>
      <van-col span="2"></van-col>
    </van-row>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import useUserStore from "@/store/modules/user";
import { Toast } from "vant";

const router = useRouter();
const redirect = ref(undefined);
const loginForm = ref({
  username: "",
  password: "",
  rememberMe: false,
  code: "",
  uuid: "",
});
const userStore = useUserStore();
const login = () => {
  // 调用action的登录方法
  userStore
    .login(loginForm.value)
    .then((response: any) => {
      Toast({
        message: response.message,
        position: "bottom",
      });
      router.push({ path: redirect.value || "/" });
    })
    .catch((response:any) => {
      loginForm.value.password="";
      Toast({
        message: response.message,
        position: "bottom",
      });
      // loading.value = false;
      // // 重新获取验证码
      // if (captchaEnabled.value) {
      //   getCode();
      // }
    });
};
</script>
<style scoped>
.div-top {
  background-color: #FF689B;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 60%;
}
.div-bottom {
  background-color: #FFFFFF;
  position: absolute;
  top: 25%;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 3rem 3rem 0rem 0rem;
  padding: 1rem 2rem 2rem 2rem;

  /* min-height: 70%; */
}
.title {
  position: absolute;
  top: 15%;
  transform: translateX(-50%);
  font-size: 1.8rem;
  font-weight: bolder;
  color: #FFFFFF;
}
.subtitle {
  transform: translateX(-50%);
  position: absolute;
  top: 30%;
  font-weight: lighter;
  color: #FFFFFF;
}
.van-field-username {
  margin-top: 2rem;
}
.van-field-password {
  margin-top: 1rem;
}
h5 {
  text-align: left;
  font-size: 1.1rem;
  font-weight: bolder;
}
.div-bottom .van-button {
  margin-top: 1rem;
  width: 100%;
  border-radius: 0.4rem;
  background-color: #FF689B;
  border: 0;
}
.div-bottom p {
  text-align: center;
  color: #666666;
}
.row-bottom {
  color:  #FF689B;
}
</style>