<template>
  <b-container>
    <b-row>
      <b-col :md="9" :sm="12">
          <div class="my-3 p-3 bg-white rounded shadow-sm"><h6 class="border-bottom border-gray pb-2 mb-0">
            Q&amp;A 累计：
            <span>1</span>

          </h6>

            <div class="media text-muted pt-3">
              <!-- 头像 -->
              <img src="/images/avatar/avatar-1.jpg" style="border-radius: 5px " width="32" height="32">
              <p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray" style="margin-left: 5px">
                <!-- 标题 -->
                <a href="/question/read/fb850442eba54bcdb0111a8395e898db" class="text-dark font-weight-bold text-decoration-none d-block">舒服舒服</a>
                <!-- 状态 -->
                <span class="badge badge-info">已解决</span>
                <!-- 浏览量 -->
                <span>
                            <svg class="bi bi-eye-fill" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
  <path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0z"></path>
  <path fill-rule="evenodd" d="M0 8s3-5.5 8-5.5S16 8 16 8s-3 5.5-8 5.5S0 8 0 8zm8 3.5a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7z"></path>
</svg>
                            6
                        </span>
                <!-- 时间 -->
                <span class="badge badge-light float-right">2021-11-21</span>
              </p>
            </div>

            <!--分页-->
            <nav aria-label="Page navigation example" class="mt-4">
              <ul class="pagination justify-content-center pagination-sm">
                <li class="page-item disabled">
                  <a class="page-link" href="/question/0/10" tabindex="">Previous</a>
                </li>

                <li class="page-item active">
                  <a class="page-link" href="/question/1/10">1</a>
                </li>

                <li class="page-item disabled">
                  <a class="page-link" href="/question/2/10">Next</a>
                </li>
              </ul>
            </nav></div>
      </b-col>
      <b-col :md="3" :sm="12">

        <div class="my-3 rounded">
          <p class="mb-0">
            <a href="/question/write" class="btn btn-primary btn-sm btn-block" style="color: white">发布问题</a>
          </p>
        </div>

        <div class="p-4 my-3 bg-white rounded">
          <p class="mb-0"> 社区公约: <br> 提问内容描述一定要清楚，回答尽量靠近主题，禁止瞎水社区，违者暂停账号使用! 申请分类可以在Q群@小狂神 </p>
        </div>

        <div class="p-4 my-3 bg-white rounded">
          <a href="/question/category/1/1/10" class="badge badge-pill badge-primary mr-1">222</a>
        </div>

        <div class="p-4 my-3 bg-white rounded">
          <h4 class="font-italic">友情链接</h4>
          <ol class="list-unstyled">
            <li><a href="https://gitee.com/kuangstudy">Gitee</a></li>
            <li><a href="https://space.bilibili.com/95256449">Bilibili</a></li>
          </ol>
        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "HomeQuestion"
}
</script>

<style scoped>

</style>
