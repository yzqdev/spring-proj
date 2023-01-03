<template>
  <b-container>
    <b-row>
      <b-col :md="3" :sm="12">
        <user-side></user-side>
      </b-col>
      <b-col :md="9" :sm="12">
        <div class="my-3 p-3 bg-white rounded shadow-sm">

          <form class="needs-validation" novalidate="" action="/userinfo/update/464a084dd56a45a69a735a4a2e3f0db3" method="post">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="nickname">用户昵称</label>
                <input type="text" value="" name="nickname" class="form-control" id="nickname" placeholder="遇见狂神说" required="">
              </div>
              <div class="col-md-6 mb-3">
                <label for="realname">真实姓名</label>
                <input type="text" value="" name="realname" class="form-control" id="realname" placeholder="xxxx" required="">
              </div>
            </div>

            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="qq">QQ</label>
                <input type="text" value="" name="qq" class="form-control" id="qq" placeholder="遇见狂神说" required="">
              </div>
              <div class="col-md-6 mb-3">
                <label for="wechat">WeChat</label>
                <input type="text" value="" name="wechat" class="form-control" id="wechat" placeholder="xxxx" required="">
              </div>
            </div>

            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="email">邮箱</label>
                <input type="email" value="" name="email" class="form-control" id="email" placeholder="kuangshen@qq.com">
              </div>

              <div class="col-md-6 mb-3">
                <label for="phone">手机</label>
                <input type="text" value="" name="phone" class="form-control" id="phone" placeholder="123 3333 2222">
              </div>
            </div>

            <div class="mb-3">
              <label for="work">工作</label>
              <input type="text" value="" name="work" class="form-control" id="work" placeholder="Java高级程序员">
            </div>

            <div class="mb-3">
              <label for="address">地址</label>
              <input type="text" value="" name="address" class="form-control" id="address" placeholder="中国 重庆">
            </div>

            <div class="mb-3">
              <label for="hobby">爱好(使用英文逗号隔开)</label>
              <input type="text" value="" name="hobby" class="form-control" id="hobby" placeholder="代码,唱歌,游戏">
            </div>

            <div class="mb-3">
              <label>自我介绍</label>
              <textarea type="text" name="intro" class="form-control"></textarea>
            </div>

            <hr class="mb-4">

            <button class="btn btn-primary btn-lg btn-block" type="submit"> 更新资料 </button>

          </form>


        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>import UserSide from "@/components/UserSide.vue";

export default {
  name: "UserInfo",
  components: {UserSide}
}
</script>

<style scoped>

</style>
