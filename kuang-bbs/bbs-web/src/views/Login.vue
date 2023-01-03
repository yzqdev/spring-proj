<template>
  <b-form class="form-signin">

    <img class="mb-4" src="/images/logo/logo7272.jpg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">登录</h1>

    <b-form-input name="username" v-model="form.username" placeholder="用户名" required=""></b-form-input>
    <b-form-input type="password" name="password" v-model="form.password" placeholder="密码" required=""></b-form-input>

    <div class="checkbox mb-3">
      <label>
        <b-form-checkbox name="remember" value="remember-me"> 记住密码</b-form-checkbox>
      </label>
    </div>

    <b-btn class="btn btn-lg btn-primary btn-block mt-3" @click="loginAjax">登 录</b-btn>

    <p class="clearfix">
      <a href="/register" class="float-right">没有账号？去注册</a>
    </p>

    <p class="mt-5 mb-3 text-muted">© 遇见狂神说</p>
  </b-form>
</template>

<script>
import {loginPost} from "@/utils/apis";

export default {
  name: "Login",
  data() {
    return {
      form: {
        username: 'yzqdev',
        password: '123456'
      }
    }
  }, methods: {
    loginAjax() {
      loginPost(this.form.username,this.form.password).then((data) => {
        console.log(data)
      }).catch((err) => {
        console.log(err)
      })
    }
  }
}
</script>

<style scoped>
.form-signin {
  text-align: center;
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
}
</style>
