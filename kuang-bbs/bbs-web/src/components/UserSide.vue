<template>
  <div class="p-4 my-3 bg-white rounded">
    <div class="mb-0">
      <p class="text-center">
        <img src="/images/avatar/avatar-1.jpg" style="border-radius: 50%" width="128" height="128">
      </p>
      <div class="text-center">
        <span>yzqdev</span>
        <br>
        <small>
          <!--<span class="badge badge-primary">普通用户</span>-->
          <span class="badge badge-warning">Vip会员</span>
        </small>
        <p class="text-center">
          <small></small>
        </p>
        <p>

        </p>
        <p>
          <button type="button" onclick="donate()" class="btn btn-outline-primary btn-sm btn-block">赞助服务器</button>
        </p>
        <p>
        </p>
        <table class="table">
          <thead id="table-title">
          <tr>
            <th scope="col">博客</th>
            <th scope="col">问题</th>
            <th scope="col">回复</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>1</td>
            <td>1</td>
            <td>1</td>
          </tr>
          </tbody>
        </table>
        <p></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserSide"
}
</script>

<style scoped>

</style>
