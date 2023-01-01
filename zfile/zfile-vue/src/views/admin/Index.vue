<template>
  <div class="zfile-admin-index">
    <div class="zfile-admin-top">
      <div class="zfile-admin-top-content">
        <div
            class="zfile-admin-top-logo box animate__animated animate__fadeInLeft"
        >
          <el-popover placement="top-start" width="200" trigger="hover">
            <div v-if="latestInfo" class="zfile-admin-index-version-info">
              <div v-html="`最新版：v${latestInfo.tag_name}`"></div>
              <div
                  v-html="
                  `发布时间: ${common.dateFormat(latestInfo.published_at)}`
                "
              ></div>
              <br/>

              文档地址：
              <el-link
                  href="http://docs.zhaojun.im/zfile"
                  target="_blank"
                  class="zfile-word-aux zfile-margin-left-unset"
              >点击进入
              </el-link
              >
              <br/>
              后端源码地址：
              <el-link
                  href="https://github.com/zhaojun1998/zfile"
                  target="_blank"
                  class="zfile-word-aux zfile-margin-left-unset"
              >点击进入
              </el-link
              >
              <br/>
              前端源码地址：
              <el-link
                  href="https://github.com/zhaojun1998/zfile-vue"
                  target="_blank"
                  class="zfile-word-aux zfile-margin-left-unset"
              >点击进入
              </el-link
              >
            </div>

            <div
                slot="reference"
                @click="openZFileAdmin"
                v-html="`ZFile Admin v${common.version}`"
            ></div>
          </el-popover>
        </div>
        <el-menu
            mode="horizontal"
            background-color="#001529"
            text-color="hsla(0,0%,100%,.65)"
            active-text-color="#fff"
            :default-active="
             $route.path !== '/admin' ?  $route.path : '/admin/site'
          "
            class="zfile-admin-menu box animate__animated animate__fadeInDown"
            @select="rebuildTitle"

        >
          <el-menu-item v-for="(item,index) in menus" :index="item.path" @click="gotoRoute(item)">
           <el-icon> <component :is="item.class" class="tab"></component></el-icon>
            <span :id="item.path">{{ item.text }}</span>
          </el-menu-item>

        </el-menu>
      </div>
    </div>
    <div class="zfile-admin-content">
        <router-view
            class="zfile-admin-content-view box animate__animated animate__fadeIn"
        />
    </div>
  </div>
</template>

<script setup lang="ts">
import {logoutApi} from "../../utils/apis";
import axios from "axios";
import http from "../../utils/http";
import {onMounted, reactive, toRefs} from "vue";
import {useRouter} from "vue-router";
import {Setting,FolderOpened,View,Key,Link,Download,HomeFilled,SwitchButton} from '@element-plus/icons-vue';
let router = useRouter()
let state = reactive({
  latestInfo: {},
  active: "/admin/site",
  menus: [
    {id: 'site', path: '/admin/site', class: Setting, text: '基本设置'},
    {id: 'driveList', path: '/admin/drive-list', class: FolderOpened, text: '驱动器列表'},
    {id: 'view', path: '/admin/view', class: View, text: '显示设置'},
    {id: 'key', path: '/admin/password', class:Key, text: '修改密码'},
    {id: 'shortLink', path: '/admin/short-link', class: Link, text: '短链管理'},
    {id: 'download', path: '/admin/download', class: Download, text: '日志下载'},
    {id: 'home', path: '/admin/hoe', class: HomeFilled, text: '前往首页'},
    {id: 'logout', path: '/admin/logout', class: SwitchButton, text: '注销'},
  ]
})
let {latestInfo, active, menus} = toRefs(state)

function gotoRoute(item: any) {
  switch (item.id) {
    case 'download':
      logDownload();
      break
    case 'home':
      home();
      break
    case 'logout':
      logout();
      break
    default:
      router.push(item.path)
      break
  }

}

function logout() {
  logoutApi().then(() => {
    router.push("/login");
  });
}

function rebuildTitle(index: string) {
  // 根据指定的导航 Index, 获取标题
  //@ts-ignore
  document.title = document.getElementById(index).innerText + " | 后台管理";
}

function logDownload() {
  window.open(http.defaults.baseURL + "/admin/log", "_blank");
}

function home() {
  let r = router.resolve({
    path:'/'

  });
  window.open(r.href, "_blank");
}

function openZFileAdmin() {
  window.open("https://github.com/zhaojun1998/zfile");
}

onMounted(() => {
  // 获取当前选中的左侧导航的文字, 作为标题
  let currentIndex = document
      .getElementsByClassName("is-active")[0]
      .getElementsByTagName("span")[0].id;
  rebuildTitle(currentIndex);
  axios
      .get("https://api.github.com/repos/zhaojun1998/zfile/releases/latest", {
        withCredentials: false,

      })
      .then((response) => {
        state.latestInfo = response.data;
      });
})
</script>

<style scoped>
.zfile-admin-index {
  height: 100%;
  overflow-y: hidden;
}

.zfile-admin-top {
  background-color: #001529;
}

.zfile-admin-top-logo {
  cursor: pointer;
  height: 100%;
  line-height: 61px;
  color: #ffffff;
  padding-right: 20px;
}

.zfile-admin-top-logo:hover {
  color: #1890ff;
}

.zfile-admin-top-content {
  /*display: flex;*/
  margin: auto;
  max-width: 1200px;
}

.zfile-admin-content {
  background-color: #f0f2f5;
  height: 100%;
  overflow-y: auto;
}

.zfile-admin-content-view {
  margin: auto;
  max-width: 1200px;
  margin-top: 50px;
  margin-bottom: 100px;
}

.zfile-admin-menu {
}

.el-menu--horizontal > .el-menu-item.is-active {
  color: #1890ff !important;
}

.el-menu.el-menu--horizontal,
.el-menu--horizontal > .el-menu-item {
  border: none;
}

.zfile-admin-index-version-info {
  font-size: 13px;
}

.zfile-admin-index-version-info >>> .el-link--inner {
  font-size: 13px;
}
</style>
