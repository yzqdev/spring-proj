<template>
  <div>
    <b-navbar type="light" toggleable="lg"  style="background-color: #ffffff;box-shadow: 0px -2px 10px #d0cccc;">
      <b-container>
      <b-navbar-brand href="#">
        KuangStudy
      </b-navbar-brand>


      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item href="/home/home">首页</b-nav-item>
          <b-nav-item href="/home/question"  >问答</b-nav-item>
          <b-nav-item href="/home/blog"  >博客</b-nav-item>
          <b-nav-item href="/home/resource"  >资源</b-nav-item>
          <b-nav-item href="/home/notice"  >公告</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">




          <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <img src="http://localhost:8301/images/avatar/avatar-1.jpg" style="width: 30px;height:30px;border-radius: 50%;" alt=""><em>yzqdev</em>
            </template>
            <b-dropdown-item href="/home/user-center">个人中心</b-dropdown-item>
            <b-dropdown-item href="/home/user-info">更新资料</b-dropdown-item>
            <b-dropdown-item href="#">头像上传</b-dropdown-item>
            <b-dropdown-item href="#">注销</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse></b-container>
    </b-navbar>

    <router-view />
  </div>
</template>

<script>

export default {
  name: "Home",

};
</script>

<style scoped></style>
