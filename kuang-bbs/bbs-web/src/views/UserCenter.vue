<template>
  <b-container>
    <b-row>
      <b-col :md="3" :sm="12">
      <user-side ></user-side>
      </b-col>
      <b-col :md="9" :sm="12">
        <div class="my-3 p-3 bg-white rounded shadow-sm">

          <h6 class="border-bottom border-gray pb-2 mb-0">
            <nav class="nav nav-pills">
              <a class="nav-link active" href="/user/blog/464a084dd56a45a69a735a4a2e3f0db3/1/10">
                <svg class="bi bi-receipt-cutoff" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor"
                     xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                        d="M1.92.506a.5.5 0 0 1 .434.14L3 1.293l.646-.647a.5.5 0 0 1 .708 0L5 1.293l.646-.647a.5.5 0 0 1 .708 0L7 1.293l.646-.647a.5.5 0 0 1 .708 0L9 1.293l.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .801.13l.5 1A.5.5 0 0 1 15 2v13h-1V2.118l-.137-.274-.51.51a.5.5 0 0 1-.707 0L12 1.707l-.646.647a.5.5 0 0 1-.708 0L10 1.707l-.646.647a.5.5 0 0 1-.708 0L8 1.707l-.646.647a.5.5 0 0 1-.708 0L6 1.707l-.646.647a.5.5 0 0 1-.708 0L4 1.707l-.646.647a.5.5 0 0 1-.708 0l-.509-.51L2 2.118V15H1V2a.5.5 0 0 1 .053-.224l.5-1a.5.5 0 0 1 .367-.27zM0 15.5a.5.5 0 0 1 .5-.5h15a.5.5 0 0 1 0 1H.5a.5.5 0 0 1-.5-.5z"></path>
                  <path fill-rule="evenodd"
                        d="M3 4.5a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5zm8-8a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5zm0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5z"></path>
                </svg>
                博客
              </a>
              <a class="nav-link" href="/user/question/464a084dd56a45a69a735a4a2e3f0db3/1/10">
                <svg class="bi bi-question-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor"
                     xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                        d="M14 1H2a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"></path>
                  <path
                      d="M5.25 6.033h1.32c0-.781.458-1.384 1.36-1.384.685 0 1.313.343 1.313 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.007.463h1.307v-.355c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.326 0-2.786.647-2.754 2.533zm1.562 5.516c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z"></path>
                </svg>
                问答
              </a>
              <a class="nav-link" href="/user/comment/464a084dd56a45a69a735a4a2e3f0db3/1/10">
                <svg class="bi bi-chat-dots" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor"
                     xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                        d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z"></path>
                  <path
                      d="M5 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"></path>
                </svg>
                回复
              </a>
            </nav>
          </h6>

          <div class="media text-muted pt-3">
            <!-- 头像 -->
            <img src="/images/avatar/avatar-1.jpg" style="border-radius: 5px " width="32" height="32">
            <p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray" style="margin-left: 5px">
              <!-- 标题 -->
              <a href="/blog/read/9b04d32210d24b1397e944fe87453157"
                 class="text-dark font-weight-bold text-decoration-none d-block">沙发的说法</a>
              <!-- 浏览量 -->
              <span>
                            <svg class="bi bi-eye-fill" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor"
                                 xmlns="http://www.w3.org/2000/svg">
                              <path d="M10.5 8a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0z"></path>
                              <path fill-rule="evenodd"
                                    d="M0 8s3-5.5 8-5.5S16 8 16 8s-3 5.5-8 5.5S0 8 0 8zm8 3.5a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7z"></path>
                            </svg>
                            5
                        </span>
              <!-- 时间 -->
              <span class="badge badge-light float-right">2021-11-21</span>
            </p>
          </div>

          <!--分页-->
          <nav aria-label="Page navigation example" class="mt-4">
            <ul class="pagination justify-content-center pagination-sm">
              <li class="page-item disabled">
                <a class="page-link" href="/user/blog/464a084dd56a45a69a735a4a2e3f0db3/0/10" tabindex="">Previous</a>
              </li>

              <li class="page-item active">
                <a class="page-link" href="/user/blog/464a084dd56a45a69a735a4a2e3f0db3/1/10">1</a>
              </li>

              <li class="page-item disabled">
                <a class="page-link" href="/user/blog/464a084dd56a45a69a735a4a2e3f0db3/2/10">Next</a>
              </li>
            </ul>
          </nav>

        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import UserSide from "@/components/UserSide.vue";
export default {
  name: "UserCenter",
  components: {UserSide}
}
</script>

<style scoped>

</style>
