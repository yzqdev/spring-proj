<template>
<main  >
  <b-form class="reg" action="/register" method="post">
    <img class="mb-4" src="/images/logo/logo7272.jpg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">注册</h1>

    <p style="color: red"></p>

   <b-form-group label="用户名">
     <b-form-input type="text" name="username" value="yzqdev" class="form-control mb-2" placeholder="用户名" required=""></b-form-input>
   </b-form-group>
    <b-form-group label="确认密码">
      <b-form-input type="password" name="password" value="123456" class="form-control" placeholder="密码" required=""></b-form-input>
    </b-form-group>
    <b-form-group label="确认密码"><b-form-input type="password" name="repassword" value="123456" class="form-control" placeholder="确认密码" required=""></b-form-input></b-form-group>
    <b-form-group label="社区邀请码"><b-form-input type="text" name="code" class="form-control" value="" placeholder="社区邀请码" required=""></b-form-input>
    </b-form-group>
    <b-btn class="btn btn-lg btn-primary btn-block mt-3" type="submit">注 册</b-btn>

    <p class="clearfix">
      <a href="/login" class="float-right">已有账号？去登录</a>
    </p>

    <p class="mt-5 mb-3 text-muted">© 遇见狂神说</p>
  </b-form>
</main>
</template>

<script>
export default {
  name: "Register"
}
</script>

<style scoped>
.reg{
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;

}
</style>
