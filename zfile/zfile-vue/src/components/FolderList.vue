<template>
  <article class="folder-wrapper" v-loading="initLoading">
    <div class="folder-list">
      <el-table
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(255,255,255,.5)"
          ref="fileTable"
          @sort-change="sortMethod"
          :data="tableData"
          @row-click="openFolder"
          :height="tableHeight"
          :size="tableSize"
          @row-contextmenu="showMenu"
      >
        <el-table-column
            prop="name"
            icon="el-icon-notebook-1"
            sortable="custom"
            class-name="zfile-table-col-name"
            label-class-name="table-header-left"
            min-width="100%"
        >
          <template #header>
            <el-icon class="hidden-xs-only">
              <document/>
            </el-icon>
            <span>文件名</span>
          </template>
          <template #default="scope">
            <template v-if="imgMode && scope.row.icon === 'el-icon-my-image'">
              <el-image
                  class="img-mode-img"
                  :src="scope.row.src"
                  :preview-src-list="imageList"
                  :initial-index="1"
                  fit="cover"
              >
              </el-image>
            </template>
            <template v-else>
              <svg class="icon" aria-hidden="true">
                <use :xlink:href="'#' + scope.row.icon"/>
              </svg>
              {{ scope.row.name }}
            </template>
          </template>
        </el-table-column>
        <el-table-column
            prop="time"
            label="修改时间"
            sortable="custom"
            v-if="!imgMode && !common.isMobile()"
            class-name="zfile-table-col-time hidden-xs-only"
            min-width="20%"
        >
          <template #header>
            <el-icon>
              <calendar/>
            </el-icon>
            <span>修改时间</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="size"
            label="大小"
            class-name="zfile-table-col-size hidden-xs-only"
            v-if="!imgMode && !common.isMobile()"
            sortable="custom"
            :formatter="common.fileSizeFilter"
            min-width="15%"
        >
          <template #header>
            <el-icon>
              <coin/>
            </el-icon>
            <span>大小</span>
          </template>
        </el-table-column>

        <el-table-column
            v-if="operater"
            label="操作"
            class-name="zfile-table-col-operator"
            :min-width="common.isMobile() ? '35%' : '15%'"
        >
          <template #header v-if="showLinkBtn">
            <el-icon>
              <operation/>
            </el-icon>
            <span>操作</span>
            <el-tooltip
                class="item"
                effect="dark"
                content="批量生成直链"
                placement="top"
            >
              <el-icon
                  @click.stop="openBatchCopyLinkDialog"
                  class="operator-btn hidden-xs-only zfile-margin-left-5"
              >
                <copy-document/>
              </el-icon>
            </el-tooltip>
          </template>
          <template #default="scope">
            <div v-if="scope.row.type === 'FILE'">
              <el-tooltip
                  class="item"
                  effect="dark"
                  content="下载"
                  placement="top"
              >
                <el-icon
                    @click.stop="downloadFile(scope.row)"
                    class="operator-btn"
                >
                  <download/>
                </el-icon>
              </el-tooltip>
              <el-tooltip
                  class="item"
                  effect="dark"
                  content="生成直链"
                  placement="top"
              >
                <el-icon
                    @click.stop="copyShortLink(scope.row)"
                    class="operator-btn"
                >
                  <copy-document/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
          id="textDialog"
          :destroy-on-close="true"
          :title="currentClickRow.name"
          v-model="dialogTextVisible"
          v-if="dialogTextVisible"
          :top="'5vh'"
          :width="'90%'"
      >
        <TextPreview :file="currentClickRow" ref="textDialog"/>
      </el-dialog>

      <el-dialog
          id="videoDialog"
          :destroy-on-close="true"
          top="5vh"
          width="80%"
          :title="currentClickRow.name"
          v-if="dialogVideoVisible"
          v-model="dialogVideoVisible"
      >
        <video-player
            v-if="dialogVideoVisible"
            ref="videoPlayer"
            :data="currentClickRow"
        />
      </el-dialog>

      <el-dialog
          id="copyLinkDialog"
          title="生成直链结果"
          custom-class="copy-link-dialog"
          :width="common.isMobile() ? '95%' : '50%'"
          v-model="dialogCopyLinkVisible"
          v-if="dialogCopyLinkVisible"
      >
        <el-row v-if="currentCopyLinkRow.row">
          <el-col :span="12" :xs="24" class="zfile-dialog-link-result-qrcode">
            <el-form>
              <el-form-item>
                <img :src="currentCopyLinkRow.img" alt="右键可另存为图片"/>
              </el-form-item>
              <el-form-item class="hidden-sm-and-down">
                <div class="zfile-word-aux zfile-margin-left-unset">
                  二维码可右键另存为图片
                </div>
              </el-form-item>
              <el-form-item class="hidden-sm-and-up">
                <div class="zfile-word-aux zfile-margin-left-unset">
                  二维码可长按另存为图片
                </div>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="12" :xs="24" class="zfile-dialog-link-result-info">
            <el-form>
              <el-form-item>
                <el-input disabled v-model="currentCopyLinkRow.row.name"
                >
                  <template #prefix
                  >
                    <el-icon class="el-input__icon"
                    >
                      <document/>
                    </el-icon>
                  </template
                  >
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-input disabled v-model="currentCopyLinkRow.row.time"
                >
                  <template #prefix
                  >
                    <el-icon class="el-input__icon"
                    >
                      <calendar/>
                    </el-icon>
                  </template
                  >
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-input disabled v-model="rowSize"
                >
                  <template #prefix
                  >
                    <el-icon class="el-input__icon"
                    >
                      <coin/>
                    </el-icon>
                  </template
                  >
                </el-input>
              </el-form-item>
              <el-form-item v-if="showLinkBtn && showPathLink">
                <el-input
                    :prefix-icon="Link"
                    type="small"
                    v-model="currentCopyLinkRow.directlink"
                >
                  <template #append>
                    <el-tooltip
                        class="item"
                        effect="dark"
                        content="复制"
                        placement="bottom"
                    >
                      <el-button
                          @click="copy(currentCopyLinkRow.directlink)"
                          type="small"
                      >
                        <el-icon class="el-input__icon">
                          <copy-document/>
                        </el-icon>
                      </el-button>
                    </el-tooltip
                    >
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item v-if="showLinkBtn && showShortLink">
                <el-input
                    :prefix-icon="Link"
                    type="small"
                    v-model="currentCopyLinkRow.link"
                >
                  <template #append>
                    <el-tooltip
                        class="item"
                        effect="dark"
                        content="复制"
                        placement="bottom"
                    >
                      <el-button
                          @click.stop="copy(currentCopyLinkRow.link)"
                          type="small"
                      >
                        <el-icon>
                          <copy-document/>
                        </el-icon>
                      </el-button>
                    </el-tooltip
                    >
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item>
                <div class="zfile-word-aux zfile-margin-left-unset">
                  直链域名取决与站点设置中的地址
                </div>
                <div class="zfile-word-aux zfile-margin-left-unset">
                  第一个链接为直链(带文件名)，第二个链接为短链接
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-dialog>

      <el-dialog
          id="batchCopyLinkDialog"
          title="批量生成直链"
          width="80%"
          :top="'80px'"
          v-model="dialogBatchCopyLinkVisible"
          v-if="dialogBatchCopyLinkVisible"
      >
        <el-table
            v-loading="batchCopyLinkLoading"
            element-loading-text="生成直链中..."
            :data="batchCopyLinkList"
            max-height="400"
        >
          <el-table-column label="文件名称" prop="name">
            <template #header>
              <span>文件名称</span>
              <el-tooltip
                  class="item"
                  effect="dark"
                  content="批量复制"
                  placement="top"
              >
                <el-icon
                    @click.stop="batchCopyLinkField('name')"
                    class="operator-btn zfile-margin-left-5"
                >
                  <copy-document/>
                </el-icon>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
              v-if="showLinkBtn && showShortLink"
              label="短链"
              width="250px"
              prop="link1"
          >
            <template #header>
              <span>短链</span>
              <el-tooltip
                  class="item"
                  effect="dark"
                  content="批量复制"
                  placement="top"
              >
                <el-icon
                    @click.stop="batchCopyLinkField('link1')"
                    class="operator-btn zfile-margin-left-5"
                >
                  <copy-document/>
                </el-icon>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
              v-if="showLinkBtn && showPathLink"
              label="直链"
              width="350px"
              show-overflow-tooltip
              prop="link2"
          >
            <template #header>
              <span>直链</span>
              <el-tooltip
                  class="item"
                  effect="dark"
                  content="批量复制"
                  placement="top"
              >
                <el-icon
                    class="operator-btn zfile-margin-left-5"
                    @click.stop="batchCopyLinkField('link2')"
                >
                  <copy-document/>
                </el-icon>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
      <div class="name">
        {{ currentAudioName }}
      </div>
      <music-player
          v-if="showMusicPlayer"
          ref="audioPlayer"
          :current="currentMusic"
          :audio-list="audioList"
      />

      <v-contextmenu ref="contextmenuRef">
        <v-contextmenu-item @click="openFolder(rightClickRow)">
          <el-icon>
            <view/>
          </el-icon>
          <label
              v-html="rightClickRow.type === 'FILE' ? '预览' : '打开'"
          ></label>
        </v-contextmenu-item>
        <v-contextmenu-item
            @click="downloadFile(rightClickRow)"
            v-show="rightClickRow.type === 'FILE'"
        >
          <el-icon>
            <download/>
          </el-icon>
          <label>下载</label>
        </v-contextmenu-item>
        <v-contextmenu-item
            @click="copyShortLink(rightClickRow)"
            v-show="rightClickRow.type === 'FILE'"
        >
          <el-icon>
            <copy-document/>
          </el-icon>
          <label>生成直链</label>
        </v-contextmenu-item>
      </v-contextmenu>

      <template>
        <el-backtop
            target=".el-table__body-wrapper"
            :bottom="haveDocument() ? 280 : 80"
            :right="30"
        >
          <el-tooltip placement="top" content="回到顶部">
            <transition name="fade">
              <div class="back-to-ceiling">
                <svg class="Icon Icon--backToTopArrow" aria-hidden="true">
                  <use xlink:href="#el-icon-my-to-top"></use>
                </svg>
              </div>
            </transition>
          </el-tooltip>
        </el-backtop>
      </template>
    </div>
  </article>
</template>

<script setup lang="ts">
import * as path from "path";
import {
  Coin,
  CopyDocument,
  Download,
  Operation,
  View,
  Calendar,
  Link,
  Document,
} from "@element-plus/icons-vue";
import VideoPlayer from "./VideoPlayer.vue";
import TextPreview from "./TextPreview.vue";
import MusicPlayer from "./MusicPlayer.vue";
import http from "../utils/http";

import {
  computed,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  toRefs,
  watch,
} from "vue";
import {qrcode, svg2url} from "pure-svg-code";
import {getShortLinkApi} from "../utils/shortlink";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";
import common from "../common";
import {ElMessage, ElMessageBox} from "element-plus";

let props = defineProps({
  // driveId: String,
});
let router = useRouter();
let route = useRoute();
let store = useStore();
let state = reactive({
  // 是否首次初始化完成
  initLoading: true,
  // 是否初始化加载完成
  loading: false,
  // 当前鼠标悬浮的行
  rightClickRow: {},
  // 是否打开文本浏览器弹出
  dialogTextVisible: false,
  // 是否打开视频播放器弹出
  dialogVideoVisible: false,
  // 是否打开生成直链页面
  dialogCopyLinkVisible: false,
  // 查询条件
  searchParam: {
    path: "",
    password: "",
    orderBy: "",
    orderDirection: "",
  },
  // 当前点击文件
  currentClickRow: {},
  contextMenuDataAxis: {
    x: null,
    y: null,
  },
  // 驱动器列表
  driveList: [],
  // 当前生成直链的行
  currentCopyLinkRow: {
    row: null,
    img: "",
    link: "",
    directlink: "",
  },
  dialogBatchCopyLinkVisible: false,
  batchCopyLinkList: [],
  batchCopyLinkLoading: false,
  imageList: [],
  currentAudioName: undefined,
});
import downloadjs from "downloadjs";
import {useClipboard} from "@vueuse/core";

let showMusicPlayer = $ref(false)
let audioList = $ref([])
let currentMusic=$ref(0)
const {text, copy, copied, isSupported} = useClipboard({
  source: state.currentCopyLinkRow.directlink,
});
let {
  batchCopyLinkList,
  batchCopyLinkLoading,
  initLoading,
  loading,
  rightClickRow,
  dialogBatchCopyLinkVisible,
  dialogCopyLinkVisible,
  dialogTextVisible,
  dialogVideoVisible,
  searchParam,
  currentClickRow,
  contextMenuDataAxis,
  driveList,
  currentAudioName,
  imageList,
  currentCopyLinkRow,
} = toRefs(state);
let contextmenuRef = ref();

let showLinkBtn = computed(() => {
  return store.getters["common/showLinkBtn"];
});
let showShortLink = computed(() => {
  return store.getters["common/showShortLink"];
});
let showPathLink = computed(() => {
  return store.getters["common/showPathLink"];
});


let audios: any = computed(() => {
  return store.getters["file/filterFileByType"]("audio");
});
let rowSize = computed(() => {
  return common.fileSizeFormat(state.currentCopyLinkRow.row.size);
});
console.log(`%c这是audio`, `color:red;font-size:16px;background:transparent`);

let tableData = computed(() => {
  return store.getters["file/tableData"];
});
let audioPlayer = ref();
// function handleBeforePlay(next) {
//   console.log(
//     `%chandlebefoerpaly`,
//     `color:red;font-size:16px;background:transparent`
//   );
//   console.log(audioPlayer.value.currentPlayIndex);
//   console.log(audios.value);
//   state.currentAudioName =
//     audios.value[audioPlayer.value.currentPlayIndex].name;
//
//   next(); // Start play
// }
let tableSize = computed(() => {
  return store.getters["common/tableSize"];
});
let showDocument = computed(() => {
  return store.getters["common/showDocument"];
});
let config = computed(() => {
  return common.config;
});
let imgMode = computed(() => {
  return store.getters["common/imgMode"];
});
let tableHeight = computed(() => {
  return "84vh";
  // return showDocument.value && config.readme !== null ? "50vh" : "84vh";
});
let operater = computed(() => {
  return store.getters["common/showOperator"] && !imgMode.value;
});

// 批量复制直链字段
function batchCopyLinkField(field) {
  let copyVal = "";
  state.batchCopyLinkList.forEach((item, index) => {
    copyVal += item[field] + "\n";
  });
  copy(copyVal);
  // if (copied) {
  //   ElMessage({
  //     type:'success'
  //     ,message:'复制成功'
  //   })
  // }
  // this.$copyText(copyVal).then(() => {
  //   this.$message.success('复制成功');
  // }, () => {
  //   this.$message.error('复制失败');
  // });
}

// 打开批量复制弹窗
function openBatchCopyLinkDialog() {
  state.batchCopyLinkList = [];
  state.batchCopyLinkLoading = true;
  loadLinkData(
      store.getters["file/tableData"][0],
      0,
      store.getters["file/tableData"]
  );
  state.dialogBatchCopyLinkVisible = true;
}

// 排序按钮
function sortMethod({prop, order}) {
  state.searchParam.orderBy = prop;
  state.searchParam.orderDirection = order === "descending" ? "desc" : "asc";
  loadFile();
}

// 工具方法
function getPwd() {
  let p = "/"; //+ route.query.folder.join("/");
  // console.log("p=", p);
  // state.searchParam.path = p ? p.substring(5, p.length) : "/";
  state.searchParam.path = route.query.path ? route.query.path : "/";
  return state.searchParam.path;
}

function updateTitle() {
  let basePath = path.basename(state.searchParam.path);

  let config = store.state.common.config;
  let siteName = "";
  if (config) {
    siteName = " | " + store.state.common.config.siteName;
  }

  if (basePath === "/" || basePath === "") {
    document.title = "首页" + siteName;
  } else {
    document.title = basePath + siteName;
  }
}

function loadLinkData(item, index, list) {
  if (item === null || index >= list.length) {
    state.batchCopyLinkLoading = false;
    return;
  }
  index++;
  if (item.type === "FILE") {
    let directlink = common.removeDuplicateSeparator(
        "/" + encodeURI(item.path) + "/" + encodeURI(item.name)
    );

    getShortLinkApi(route.params.driveId, directlink).then((response) => {
      let link1 = response.data.data;
      let link2 = common.removeDuplicateSeparator(
          store.getters["common/domain"] +
          "/" +
          store.getters["common/directLinkPrefix"] +
          "/" +
          route.params.driveId +
          "/" +
          encodeURI(item.path) +
          "/" +
          encodeURI(item.name)
      );
      const svgString = qrcode(response.data.data);
      let img = svg2url(svgString);

      state.batchCopyLinkList.push({
        name: item.name,
        link1: link1,
        link2: link2,
        img: img,
      });
      loadLinkData(list[index], index, list);
    });
  } else {
    loadLinkData(list[index], index, list);
  }
}

function getPathPwd() {
  let pwd = sessionStorage.getItem("zfile-pwd-" + state.searchParam.path);
  return pwd === null ? "" : encodeURI(pwd);
}

function putPathPwd(value) {
  sessionStorage.setItem("zfile-pwd-" + state.searchParam.path, value);
}

function haveDocument() {
  return (
      store.getters.showDocument && store.state.common.config.readme !== null
  );
}

function openVideo() {
  state.currentClickRow.url = common.removeDuplicateSeparator(
      store.getters.domain +
      "/" +
      store.getters.directLinkPrefix +
      "/" +
      route.params.driveId +
      "/" +
      encodeURI(state.currentClickRow.path) +
      "/" +
      encodeURI(state.currentClickRow.name)
  );
  console.log(state.currentClickRow);
  console.log(`%c哈哈哈哈`, `color:red;font-size:16px;background:transparent`);
  state.dialogVideoVisible = true;
}

// 右键菜单
function showMenu(row, column, event) {
  state.rightClickRow = row;
  event.preventDefault();
  contextmenuRef.value.show({
    top: event.clientY,
    left: event.clientX,
  });
  contextmenuRef.value.$el.hidden = false;
}

function copyShortLink(row) {
  let directlink = common.removeDuplicateSeparator(
      "/" + encodeURI(row.path) + "/" + encodeURI(row.name)
  );

  getShortLinkApi(route.params.driveId, directlink).then((response) => {
    state.currentCopyLinkRow.row = row;
    state.currentCopyLinkRow.link = response.data.data;
    let directlink = common.removeDuplicateSeparator(
        store.getters["common/domain"] +
        "/" +
        store.getters["common/directLinkPrefix"] +
        "/" +
        route.params.driveId +
        "/" +
        encodeURI(row.path) +
        "/" +
        encodeURI(row.name)
    );
    state.currentCopyLinkRow.directlink = directlink;
    const svgString = qrcode(response.data.data);
    state.currentCopyLinkRow.img = svg2url(svgString);
    state.dialogCopyLinkVisible = true;
  });
}

function downloadFile(row: any) {
  console.log(
      `%c这是downloadfile`,
      `color:red;font-size:16px;background:transparent`
  );
  console.log(row);
  downloadjs(row.src, row.name, row.mimetype);
  // window.location.href = row.src;
}

// 文件夹密码
function popPassword() {
  // 如果输入了密码, 则写入到 sessionStorage 缓存中, 并重新调用加载文件.
  ElMessageBox.prompt("请输入密码", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputType: "password",
    inputValidator(val) {
      return !!val;
    },
    inputErrorMessage: "密码不能为空.",
  })
      .then(({value}) => {
        let cachePassword = getPathPwd();
        if (value !== cachePassword) {
          putPathPwd(value);
        }
        loadFile();
      })
      .catch(() => {
        router.push({
          name: "home",
          params: {
            driveId: route.params.driveId,
          },
          query: {
            path: state.searchParam.path,
          },
        });
      });
}

// 数据加载
function loadFile() {
  // 未指定 driveId 时, 不执行任何操作.
  if (!route.params.driveId) {
    return;
  }
  state.loading = true;

  let url = "/api/list/" + route.params.driveId;
  console.log(getPwd());
  let param = {
    path: getPwd(),
    password: getPathPwd(),
    orderBy: state.searchParam.orderBy,
    orderDirection: state.searchParam.orderDirection,
  };

  let requestDriveId = route.params.driveId;
  http.get(url, {params: param}).then((response) => {
    let currentDriveId = route.params.driveId;
    if (requestDriveId !== currentDriveId) {
      return;
    }

    // 如果需要密码或密码错误进行提示, 并弹出输入密码的框.
    if (response.data.code === common.responseCode.INVALID_PASSWORD) {
      ElMessage("密码错误，请重新输入！");
      popPassword();
      return;
    }
    if (response.data.code === common.responseCode.REQUIRED_PASSWORD) {
      ElMessage({
        type: "warning",
        message: "此文件夹需要密码，请输入密码！",
      });
      popPassword();
      return;
    }

    if (response.data.code !== 0) {
      ElMessage({
        type: "warning",
        message: response.data.msg,
      });
      return;
    }
    console.log(response.data.data.config);
    console.log(`%c哈哈哈`, `color:red;font-size:16px;background:transparent`);
    store.commit("common/updateConfig", response.data.data.config);
    if (route.params.driveId !== store.getters.oldDriveId) {
      store.commit("common/updateOldDriveId", route.params.driveId);
      store.commit(
          "common/updateNewImgMode",
          response.data.data.config.defaultSwitchToImgMode
      );
    }
    let data = response.data.data.files;
    updateTitle();

    // 如果不是根路径, 则构建 back 上级路径的数据.
    let searchPath = state.searchParam.path;

    if (searchPath !== "" && searchPath !== "/") {
      let fullPath = route.params.pathMatch;
      fullPath = fullPath ? fullPath : "/";
      console.log(
          `%cfuullath`,
          `color:red;font-size:16px;background:transparent`
      );
      console.log(fullPath);
      let parentPathName = path.basename(path.resolve(fullPath, "../"));
      data.unshift({
        name: parentPathName ? parentPathName : "/",
        path: path.resolve(searchPath, "../"),
        type: "BACK",
      });
    }
    console.log(data);
    console.log(
        `%c上面是table`,
        `color:red;font-size:16px;background:transparent`
    );

    // store.commit('file/tableData',tmp)
    store.commit("file/tableData", data);
    state.loading = false;

    state.initLoading = false;
  });
}

// 文件预览
function openFolder(row: any) {
  console.log(row);
  console.log(
      `%copenfolder`,
      `color:red;font-size:16px;background:transparent`
  );
  state.currentClickRow = row;
  if (row.type === "FILE") {
    if (store.getters["common/currentStorageStrategyType"] === "ftp") {
      ElMessage({
        message: "FTP 模式，不支持预览功能，已自动调用下载",
        type: "warning",
      });
      downloadFile(row);
      return;
    }

    let fileType = common.getFileType(row.name);

    switch (fileType) {
      case "video":
        openVideo();
        break;
      case "image":
        openImage();
        break;
      case "text":
        openText();
        break;
      case "audio":
        openAudio(row);
        break;
      default:
        downloadFile(row);
    }
  } else {
    let path;
    if (row.type === "BACK") {
      path = row.path;
    } else {
      path = common.removeDuplicateSeparator(row.path + "/" + row.name);
    }

    if (path.indexOf("/") !== 0) {
      path = "/" + path;
    }

    router.push({
      name: "home",
      params: {
        driveId: route.params.driveId,
      },
      query: {
        path: path,
      },
    });
  }
}

function openImage() {
  console.log(store.getters["file/filterFileByType"]("image"));
  for (let image of store.getters["file/filterFileByType"]("image")) {
    state.imageList.push(image.src);
  }

  // this.layer.photos({
  //   photos: {
  //     "data": imageDate,
  //     "start": this.currentClickTypeIndex('image')
  //   }
  //   , anim: 5
  //   , shade: 0.5
  // });
}


function openAudio(row) {
  console.log(row)

  audioList = store.getters["file/filterFileByType"]("audio")
  showMusicPlayer = true
  currentMusic=1
  console.log(audioPlayer )
}

function openText() {
  state.dialogTextVisible = true;
}

onMounted(() => {
  console.log(route.params);
  loadFile();
});

let currentClickTypeIndex = computed(() => {
  return (fileType: string) => {
    let currentClickRow: any = state.currentClickRow;
    if (currentClickRow.type !== "FILE") {
      return -1;
    }

    if (JSON.stringify(currentClickRow) === "{}") {
      return 0;
    } else {
      fileType = fileType ? fileType : common.getFileType(currentClickRow.name);
      return store.getters["file/filterFileByType"](fileType).findIndex(
          (item) => {
            return item.name === currentClickRow.name;
          }
      );
    }
  };
});
watch(copied, (newVal) => {
  if (newVal) {
    ElMessage({type: "success", message: "复制成功"});
  }
});
watch(route, (newVal, preVal) => {
  showMusicPlayer=false

  loadFile();

});
</script>

<style lang="scss" scoped>
.folder-wrapper {
  :deep(.folder-list) {
    overflow: hidden;

    i {
      font-size: 2rem;
    }

    .copy-link-dialog {
      i {
        font-size: 1rem;
      }
    }

    .el-table__body-wrapper {
      overflow-x: hidden;
      overflow-y: auto;
    }

    .table-header-left {
      margin-left: 38px;
    }

    tr {
      cursor: pointer;
    }

    .el-dialog__header {
      /* 弹窗标题居中, 高度减少 */
      text-align: center;
      margin-bottom: -10px;
      padding: 5px 0 5px 0;
    }

    .img-mode-img {
      display: block;
      width: 80%;
      height: auto;
      margin: 0 auto;
    }

    .el-table {
      margin: 20px 0 0 20px;
      padding-right: 30px;
      overflow-y: hidden;

      .el-table__header {
        .cell {
          i {
            margin-right: 5px;
          }
        }
      }
    }
  }
}

@media screen and (max-device-width: 769px) {
  .el-table {
    margin: 0 0 0 0;
    padding-right: 0;
  }
}

.el-table::before {
  height: 0;
}

.el-table svg {
  font-size: 18px;
  margin-right: 15px;
}

.el-scrollbar {
  .el-scrollbar__wrap {
    overflow-x: hidden !important;
  }
}

/*视频弹窗样式 -- 去除内容边框*/

:deep(#videoDialog) {
  .el-dialog__body {
    padding: 10px 0 0 0;
  }

  .el-dialog__headerbtn {
    top: 10px;
  }
}

#textDialog {
  .el-dialog {
    margin-bottom: 0;
  }
}

.v-contextmenu-item {
  label {
    margin-left: 10px;
  }
}

@media screen and (max-device-width: 1920px) {
  #videoDialog {
    .el-dialog {
      margin-top: 5vh !important;
      width: 70% !important;
    }
  }
}

@media screen and (max-device-width: 769px) {
  #videoDialog {
    .el-dialog {
      margin-top: 10vh !important;
      width: 90% !important;
    }
  }
}

.operator-btn {
  color: #1e9fff;
  margin-right: 20px;
  font-size: 16px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.back-to-ceiling {
  right: 50px;
  bottom: 50px;
  width: 40px;
  height: 40px;
  border-radius: 4px;
  line-height: 45px;
  background: #e7eaf1;
  display: inline-block;
  text-align: center;
  cursor: pointer;
}

.back-to-ceiling:hover {
  background: #d5dbe7;
}

.back-to-ceiling .Icon {
  fill: #9aaabf;
  background: none;
}

.back-to-ceiling .Icon--backToTopArrow {
  height: 16px;
  width: 16px;
}

.zfile-dialog-link-result-qrcode {
  text-align: center;
}

.zfile-dialog-link-result-info .el-form-item {
  margin-bottom: 10px;
}

.name {
  text-align: center;
  line-height: 80px;
}

#batchCopyLinkDialog {
  thead {
    cursor: pointer;
  }
}
</style>
