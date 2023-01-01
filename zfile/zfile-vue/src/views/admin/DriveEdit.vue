<template>
  <div v-if="supportStrategy">
    <el-form
        v-loading="loading"
        element-loading-text="保存并初始化中."
        id="siteForm"
        ref="formRef"
        :model="driveItem"
        :rules="rules"
        label-width="auto"
        :status-icon="true"
    >
      <el-row :gutter="50" class="box animate__animated animate__fadeIn">
        <el-col :span="12">
          <el-form-item label="驱动器名称" prop="name">
            <el-input v-model="driveItem.name"/>
          </el-form-item>

          <el-form-item label="是否启用">
            <el-switch v-model="driveItem.enable"/>
            <el-tooltip placement="right">
              <template #content>如不启用，则不在首页展示。</template>
              <i class="el-icon-info zfile-info-tooltip"></i>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="是否默认打开图片模式">
            <el-switch v-model="driveItem.defaultSwitchToImgMode"/>
            <el-tooltip placement="right">
              <template #content>
                启用后，每次切换到此存储器，是否默认打开图片模式
              </template>
              <i class="el-icon-info zfile-info-tooltip"></i>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="开启缓存">
            <el-switch v-model="driveItem.enableCache" @change="cacheChange"/>
            <el-tooltip placement="right">
              <template #content>
                开启缓存后，N 秒内重复请求相同文件夹，不会重复调用 API。
                <br/>
                参数 N 在配置文件中设置 {zfile.cache.timeout}，默认为 1800 秒。
                <br/>
                <br/>
                注意：开启缓存后，可能会导致系统不稳定，如非必要或不了解缓存的，不建议开启此功能。
              </template>
              <i class="el-icon-info zfile-info-tooltip"></i>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="开启缓存自动刷新">
            <el-switch v-model="driveItem.autoRefreshCache"/>
            <el-tooltip placement="right">
              <template #content>
                每隔 N 秒检测到期的缓存, 对于过期缓存尝试调用 API, 重新写入缓存.
                <br/>
                参数 N 在配置文件中设置
                {{ zfile.cache.auto - refresh - interval }}，默认为 5 秒。
              </template>
              <i class="el-icon-info zfile-info-tooltip"></i>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="存储策略" prop="type">
            <el-select
                filterable
                v-model="driveItem.type"
                placeholder="请选择存储策略"
            >
              <el-option
                  :key="item.key"
                  v-for="item in supportStrategy"
                  :label="item.description"
                  :value="item.key"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
              v-for="item in storageStrategyForm"
              :label="item.title"
              :key="item.title"

              :prop="'storageStrategyConfig.' + item.key"
          >
            <el-col v-if="item.key !== 'siteType'" class="el-drive-form-col" :span="22">
              <!-- Endpoint -->
              <el-select
                  filterable
                  allow-create
                  default-first-option
                  v-if="
                  item.key === 'endPoint' &&
                  region.hasOwnProperty(driveItem.type)
                "
                  style="width: 100%"
                  v-model="driveItem.storageStrategyConfig.endPoint"
              >
                <el-option
                    v-for="endPoint in region[driveItem.type]"
                    :label="endPoint.name"
                    :value="endPoint.val"
                    :key="endPoint.name"
                >
                  <span style="float: left">{{ endPoint.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{
                      endPoint.val
                    }}</span>
                </el-option>
              </el-select>

              <!-- S3 PathStyle -->
              <div v-else-if="item.key === 'pathStyle'">
                <el-select v-model="driveItem.storageStrategyConfig.pathStyle">
                  <el-option
                      label="bucket-virtual-hosting"
                      value="bucket-virtual-hosting"
                  ></el-option>
                  <el-option label="path-style" value="path-style"></el-option>
                </el-select>
                <el-link
                    class="zfile-word-aux"
                    target="_blank"
                    icon="el-icon-document"
                    href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/dev/VirtualHosting.html#path-style-access"
                >查看 S3 API 说明文档
                </el-link
                >
              </div>

              <!-- 私有空间 -->
              <div v-else-if="item.key === 'isPrivate'">
                <el-switch
                    v-model="driveItem.storageStrategyConfig.isPrivate"
                />
                <el-tooltip placement="right">
                  <template #content>私有空间会生成带签名的下载链接</template>
                  <i class="el-icon-question zfile-info-tooltip"></i>
                </el-tooltip>
              </div>

              <div
                  v-else-if="
                  item.key === 'siteName' &&
                  (driveItem.type === 'sharepoint' ||
                    driveItem.type === 'sharepoint-china')
                "
              >
                <el-input
                    placeholder="请输入站点名称"
                    v-model="driveItem.storageStrategyConfig.siteName"
                    class="input-with-select"
                >
                  <el-select
                      class="zfile-site-id-input-site-type-select"
                      v-model="driveItem.storageStrategyConfig.siteType"
                      @input="change($event)"
                      #prepend
                  >
                    <el-option label="/sites/" value="/sites/"></el-option>
                    <el-option label="/teams/" value="/teams/"></el-option>
                  </el-select>
                </el-input>
              </div>

              <el-input
                  v-else
                  placeholder=""
                  @input="change($event)"
                  v-model="driveItem.storageStrategyConfig[item.key]"
              />
            </el-col>

            <div v-if="item.key === 'endPoint'">
              <el-tooltip placement="bottom">
                <template #content>
                  如下拉列表中没有的区域, 或想使用内网地址, 可直接输入后回车, 如
                  oss-cn-beijing-internal.aliyuncs.com
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div v-if="item.key === 'basePath'">
              <el-tooltip placement="bottom">
                <template #content>
                  基路径表示仅读取的文件夹, 不填写表示允许读取所有。如： '/',
                  '/文件夹1'
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div v-if="item.key === 'filePath'">
              <el-tooltip placement="bottom">
                <template #content>
                  Linux 或对象存储等需以 / 开头.
                  <br/>
                  Windows 支持 C:/ 类的盘符开头.
                  <br/>
                  结尾不需要加 <b>/</b>.
                  <br/>
                  <br/>
                  Docker 部署需提前映射宿主机路径！
                  <span

                      style="cursor: pointer; color: #4d9ff8"
                      @click="
                      openUrl(
                        'https://docs.docker.com/engine/reference/run/#volume-shared-filesystems'
                      )
                    "
                  >
                    (配置文档)
                  </span>
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div v-if="item.key === 'proxyDomain'">
              <el-tooltip placement="bottom">
                <template
                    #content
                    style="cursor: pointer"
                    @click="
                    openUrl(
                      'http://docs.zhaojun.im/zfile/#/advanced?id=onedrive-cf'
                    )
                  "
                >
                  配置&使用文档（点击进入）
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div v-if="item.key === 'domain' && driveItem.type === 'ftp'">
              <el-tooltip placement="bottom">
                <template #content>
                  如次 ftp 目录同时也有 http 访问权限，则这里填写 http
                  访问域名，这样就会用 http 协议下载。防止 ftp
                  协议导致的密码泄露
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div
                v-if="
                item.key === 'siteName' &&
                (driveItem.type === 'sharepoint' ||
                  driveItem.type === 'sharepoint-china')
              "
            >
              <el-tooltip placement="bottom">
                <template #content>
                  /sites/xxx 或 /teams/xxx ，xxx 为你的站点名称
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>

            <div
                v-if="
                item.key === 'siteId' &&
                (driveItem.type === 'sharepoint' ||
                  driveItem.type === 'sharepoint-china')
              "
            >
              <el-tooltip placement="bottom">
                <template #content>
                  输入 "访问令牌" 和 "站点名称" 后，可以自动获取 SiteId。
                  <br/>
                  如无法自动获取，请点击下方的 "前往获取 Site Id"。
                </template>
                <i class="el-icon-question zfile-info-tooltip"></i>
              </el-tooltip>
            </div>
          </el-form-item>

          <el-form-item
              v-if="
              driveItem.type === 'onedrive' || driveItem.type === 'sharepoint'
            "
          >
            <el-link
                target="_blank"
                icon="el-icon-link"
                :href="$http.defaults.baseURL + '/onedrive/authorize'"
            >前往获取令牌
            </el-link
            >
            <br/>
            <el-link
                target="_blank"
                v-show="driveItem.type === 'sharepoint'"
                icon="el-icon-link"
                :href="'/#/sharepoint-util'"
            >前往获取 Site Id
            </el-link
            >
          </el-form-item>

          <el-form-item
              v-if="
              driveItem.type === 'onedrive-china' ||
              driveItem.type === 'sharepoint-china'
            "
          >
            <el-link
                target="_blank"
                icon="el-icon-link"
                :href="$http.defaults.baseURL + '/onedrive/china-authorize'"
            >前往获取令牌
            </el-link
            >
            <br/>
            <el-link
                target="_blank"
                v-show="driveItem.type === 'sharepoint-china'"
                icon="el-icon-link"
                :href="'/#/sharepoint-util'"
            >前往获取 Site Id
            </el-link
            >
          </el-form-item>

          <el-form-item v-if="driveItem.type === 'ftp'">
            <div class="zfile-word-aux zfile-margin-left-unset">
              注意: FTP 协议，如果不填写加速域名 (HTTP 下载地址)，则会使用 FTP
              协议进行下载
              <br/>
              FTP 协议会在 URL 中暴露用户名密码，如：<b
            >ftp://用户名:密码@IP:端口/文件路径/文件名</b
            >
              <br/>
              如为 FTP 提供了加速域名 (HTTP 下载地址)，则会使用 HTTP
              协议，如：<b>http(s)://加速域名/文件路径/文件名</b>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="zfile-text-align-center">
      <el-button type="primary" :disabled="loading" @click="submitForm "
      >保 存
      </el-button
      >
      <el-button @click="close">取 消</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {regionObj} from "../../region";
import {getSiteApi, getStorageForm} from "../../utils/apis";
import {onMounted, reactive, ref, toRefs, watch} from "vue";
import http from "../../utils/http";
import {ElMessage} from "element-plus";

interface DriveItem {

}

const props: any = defineProps({
  close: {
    type: Function,
  },
  driveItem: Object,
  supportStrategy: Object,
})
let state: any = reactive({
  region: regionObj,
  loading: false,
  storageStrategyForm: [],
  rules: {
    'storageStrategyConfig.proxyDomain': [
      {required: false, type: 'url', message: '请输入正确的域名，需以 http:// 或 https:// 开头', trigger: 'change'},
    ],
    name: [
      {required: true, message: '请输入驱动器名称', trigger: 'change'},
    ],
    siteName: [
      {required: true, message: '请输入站点名称', trigger: 'change'},
    ],
    username: [
      {required: true, message: '请输入管理员账号', trigger: 'change'},
    ],
    password: [
      {required: true, message: '请输入管理员密码', trigger: 'change'},
    ],
    domain: [
      {required: true, type: 'url', message: '请输入正确的域名，需以 http:// 或 https:// 开头', trigger: 'change'},
    ],
    type: [
      {required: true, message: '存储策略不能为空', trigger: 'change'},
    ],
    'storageStrategyConfig.domain': [
      {
        required: false,
        validator: (rule, value, callback) => {
          let domainCheck = /(http|https):\/\/([\w.]+\/?)\S*/

          if ((value === undefined || value === '') && this.driveItem.type === 'ftp') {
            callback();
            return;
          }

          if (value === undefined || value === '') {
            callback(new Error('域名不能为空'));
            return;
          }
          if (!domainCheck.test(value)) {
            callback(new Error('请输入正确的域名，需以 http:// 或 https:// 开头'));
          } else if (value.lastIndexOf("/") === value.length - 1) {
            callback(new Error('请输入正确的域名，不能以 / 结尾'));
          } else {
            callback();
          }
        },
        type: 'url',
        trigger: 'change'
      }
    ],
    'storageStrategyConfig.username': [
      {
        validator: (rule, value, callback) => {
          if ((props.driveItem.type === 'upyun' || props.driveItem.type === 'ufile') && (value === undefined || value === '')) {
            callback(new Error('操作员名称不能为空'));
          } else {
            callback();
          }
        }
      }
    ],
    'storageStrategyConfig.password': [
      {
        validator: (rule, value, callback) => {
          if ((props.driveItem.type === 'upyun' || props.driveItem.type === 'ufile') && (value === undefined || value === '')) {
            callback(new Error('操作员密码不能为空'));
          } else {
            callback();
          }
        }
      }
    ],
    'storageStrategyConfig.endPoint': [{required: true, message: '区域不能为空'}],
    'storageStrategyConfig.accessKey': [{required: true, message: 'AccessKey 不能为空'}],
    'storageStrategyConfig.filePath': [{required: true, message: '文件路径不能为空'}],
    'storageStrategyConfig.secretKey': [{required: true, message: 'SecretKey 不能为空'}],
    'storageStrategyConfig.bucketName': [{required: true, message: '此项不能为空'}],
    'storageStrategyConfig.host': [{required: true, message: "域名或 IP 不能为空"}],
    'storageStrategyConfig.port': [{required: true, message: "端口不能为空"}],
    'storageStrategyConfig.accessToken': [{required: true, message: "访问令牌不能为空"}],
    'storageStrategyConfig.refreshToken': [{required: true, message: "刷新令牌不能为空"}],
    'storageStrategyConfig.secretId': [{required: true, message: "SecretId 不能为空"}],
    'storageStrategyConfig.siteId': [{required: true, message: 'SiteId 不能为空'}]
  }
})
let {region, loading, storageStrategyForm, rules} = toRefs(state)
let formRef = ref()

function openUrl(url: string) {
  window.open(url);
}

function cacheChange(newVal: any) {
  if (!newVal) {
    props.driveItem.autoRefreshCache = false;
  }
}

watch(props.driveItem, (newVal) => {
  if (newVal) {
    loadStrategyForm(newVal.type);
  } else {
    state.storageStrategyForm = null;
  }
}, {deep: true, })


function loadStrategyForm(val: any) {
  getStorageForm(val).then((response) => {
    state.storageStrategyForm = response.data.data;
    if (val === "sharepoint" || val === "sharepoint-china") {
      state.driveItem.storageStrategyConfig.siteType = "/sites/";
    }

    if (val !== "ftp") {
      state.rules["storageStrategyConfig.domain"][0].required = true;
    }
    console.log(`%cstore form`, `color:red;font-size:16px;background:transparent`)
    console.log(state.storageStrategyForm)
  });
}

function change() {
  // forceUpdate();
}

function submitForm() {
  formRef.value.validate((valid: boolean) => {
    if (valid) {
      state.loading = true;
      http
          .post("/admin/drive", props.driveItem)
          .then((response) => {
            let data = response.data;
            ElMessage({
              message: data.msg,
              type: data.code === 0 ? "success" : "error",
              duration: 1500,
            });
            props.close()
            state.loading = false;
          })
          .catch(() => {
            state.loading = false;
          });
    } else {
      return false;
    }
  });
}

onMounted(() => {
  if (props.driveItem.type) {
    loadStrategyForm(props.driveItem.type);
  }
})
</script>

<style scoped>
.el-drive-form-col {
  padding-left: 0 !important;
}

.zfile-site-id-input-site-type-select {
  width: 100px;
}
</style>
