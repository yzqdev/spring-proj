<template>
  <el-card>
    <div>
      <el-button
          type="primary"


          @click="addDrive"
      ><el-icon><plus /></el-icon>新增
      </el-button
      >
      <span class="zfile-word-aux">提示：拖拽行可以进行排序</span>
    </div>

    <el-table
        :data="driveList"
        ref="driveTable"
        row-key="id"
        highlight-current-row
    >
      <el-table-column prop="id" align="center" width="100" label="驱动器ID">
        <template v-slot="scope">
          <span>{{ scope.row.id }}</span>

          <el-icon class=" table-edit-icon" @click="editDriveId(scope.row.id)">
            <edit></edit>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column
          prop="name"
          :show-overflow-tooltip="true"
          label="驱动器名称"
      >
      </el-table-column>
      <el-table-column
          prop="type"
          :formatter="typeFormatter"
          :show-overflow-tooltip="true"
          width="160"
          label="所属策略"
      >
        <template v-slot="{row}">
          {{ row.type.description }}
        </template>
      </el-table-column>
      <el-table-column prop="enable" width="100" label="是否启用">
        <template v-slot="scope">
          <el-switch
              @change="switchEnableStatus(scope.row)"
              v-model="scope.row.enable"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="enableCache" width="100" label="缓存开启">
        <template v-slot="scope">
          <el-switch
              @change="switchCacheEnableStatus(scope.row)"
              v-model="scope.row.enableCache"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="autoRefreshCache" width="120" label="缓存自动刷新">
        <template v-slot="scope">
          <el-switch
              @change="switchAutoRefreshStatus(scope.row)"
              v-model="scope.row.autoRefreshCache"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template v-slot="scope">


          <el-tooltip
              class="item"
              effect="dark"
              content="编辑"
              placement="left"
          >
            <el-button


                type="primary"
                @click="editDrive(scope.row)"
            > <el-icon><edit/></el-icon>
            </el-button>
          </el-tooltip>

          <el-tooltip
              class="item"
              effect="dark"
              content="缓存管理"
              placement="top"
          >
            <el-button

                :disabled="!scope.row.enableCache"

                type="primary"
                @click="cacheManage(scope.row)"
            > <el-icon><operation /></el-icon>
            </el-button>
          </el-tooltip>

          <el-tooltip
              class="item"
              effect="dark"
              content="文件过滤"
              placement="bottom"
          >
            <el-button


                type="primary"
                @click="showFilterDialog(scope.row)"
            > <el-icon><View /></el-icon>
            </el-button>
          </el-tooltip>

          <el-tooltip
              class="item"
              effect="dark"
              content="删除"
              placement="right"
          >
            <el-button
                @click="deleteDrive(scope.row)"


                type="danger"
            > <el-icon><delete/></el-icon>
            </el-button>
          </el-tooltip>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        width="80%"
        title="驱动器设置"
        :before-close="handleClose"


        v-model="driveEditDialogVisible"
        top="10vh"
        :destroy-on-close="true"
    >
      <drive-edit
          :drive-item="driveItem"
          :close="closeDriveEdit"
          :support-strategy="supportStrategy"
      ></drive-edit>
    </el-dialog>

    <el-dialog
        width="40%"
        title="过滤规则"

        v-if="filterDialogVisible"
        v-model="filterDialogVisible"
        top="10vh"
        :destroy-on-close="true"
    >
      <filter-pattern
          :drive-id="currentCacheManageId"
          :close="closeFilterDialog"
      ></filter-pattern>
    </el-dialog>

    <el-dialog
        id="cacheDialog"
        width="70%"
        title="缓存管理"
        v-if="cacheManageVisible"
        :visible.sync="cacheManageVisible"
        top="10vh"
        :destroy-on-close="true"
    >
      <cache-manager
          :current-cache-manage-id="this.currentCacheManageId"
      ></cache-manager>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import Sortable from "sortablejs";
import qs from "qs";
import DriveEdit from "./DriveEdit.vue";
import CacheManager from "./CacheManager.vue";
import FilterPattern from "./FilterPattern.vue";
import {
  CacheApi,
  cacheCleanApi,
  CacheFreshApi,
  delDriveApi,
  driveDragApi,
  driveEnableApi,
  driveUpdateIdApi,
  getDriveApi,
  strategyApi,
} from "../../utils/cache";
import {getDrivesApi} from "../../utils/apis";
import {defineComponent, onMounted, reactive, toRefs} from "vue";
import {Delete, Edit, Operation, Plus, View} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";
import common from "../../common";

let state = reactive({
  loading: false,
  driveList: [],
  supportStrategy: [],
  driveItem: {
    orderNum: null,
    name: "",
    type: null,
    enable: true,
    defaultSwitchToImgMode: false,
    searchEnable: false,
    searchIgnoreCase: false,
    searchContainEncryptedFile: false,
    enableCache: false,
    autoRefreshCache: false,
    storageStrategyConfig: {
      endPoint: "",
      pathStyle: "",
      isPrivate: false,
      accessKey: null,
      secretKey: null,
      bucketName: null,
      host: null,
      port: null,
      filePath: null,
      accessToken: null,
      refreshToken: null,
      secretId: null,
      username: null,
      password: null,
      basePath: "",
      domain: "",
    },
  },
  sortable: null,
  driveEditDialogVisible: false,
  filterDialogVisible: false,
  cacheManageVisible: false,
  currentCacheManageId: null,

})
let {loading,driveEditDialogVisible,driveList,driveItem,sortable,filterDialogVisible,cacheManageVisible,currentCacheManageId,supportStrategy}=toRefs(state)
function handleClose(done) {
 ElMessageBox. confirm("是否确认关闭？关闭后填写的数据不会保留。", {
    type: "warning",
  })
      .then((_) => {
        done();
      })
      .catch((_) => {
      });
}

function
editDriveId(id) {
  ElMessageBox.prompt("请输入要修改为的 ID，需为大于零的整数。", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /^[1-9]\d{0,9}$/,
    inputErrorMessage: "ID 只能为大于零的整数，最大支持 9 位数字。",
  }).then(({value}) => {
    driveUpdateIdApi({updateId: id, newId: value}).then(() => {
     ElMessage({
        message: "修改成功",
        type: "success",
      });
      init();
    });
  });
}

function
setSort() {
  const tbody = document.querySelector(".el-table__body-wrapper tbody");
  Sortable.create(tbody, {
    onEnd: (e) => {
      if (e.oldIndex === e.newIndex) {
        return;
      }

      const currRow = state.driveList.splice(e.oldIndex, 1)[0];
      state.driveList.splice(e.newIndex, 0, currRow);

      driveDragApi(state.driveList).then(() => {
        ElMessage({
          message: "调整排序成功",
          type: "success",
        });
      });
    },
  });
}

function clearCache() {
  cacheCleanApi(state.currentCacheManageId).then(() => {
    loadCacheManageData();
    ElMessage({
      message: "清理成功",
      type: "success",
    });
  });
}

function cacheManage(row) {
  state.currentCacheManageId = row.id;
  state.cacheManageVisible = true;
}

function showFilterDialog(row) {
  state.currentCacheManageId = row.id;
  state.filterDialogVisible = true;
}

function switchEnableStatus(row) {
  let action = row.enable ? "enable" : "disable";
  driveEnableApi(row.id, action).then(() => {
    ElMessage({
      type:'success',message:'修改成功'
    })
  });
}

function switchCacheEnableStatus(row) {
  let action = row.enableCache ? "enable" : "disable";
  CacheApi(row.id, action).then(() => {
    ElMessage({
      type:'success',message:'修改成功'
    })
  });
}

function switchAutoRefreshStatus(row) {
  let action = row.autoRefreshCache ? "start" : "stop";
  CacheFreshApi(row.id, action).then(() => {
    ElMessage({
      type:'success',message:'修改成功'
    })
  });
}

function typeFormatter(row, column, val) {
  for (let index in state.supportStrategy) {
    if (state.supportStrategy[index].key === val) {
      return state.supportStrategy[index].description;
    }
  }
}

function editDrive(row) {
  getDriveApi(row.id).then((response) => {
    let data = response.data.data;
    data.type = data.type.key;
    state.driveItem = data;
    state.driveEditDialogVisible = true;
  });
}

function deleteDrive(row) {
  ElMessageBox.confirm("是否确认删除？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
    callback: (action) => {
      if (action === "confirm") {
        delDriveApi(row.id).then((response) => {
          if (response.data.code ===  common.responseCode.SUCCESS) {
            ElMessage({
              type:'success',message:'删除成功'
            })
            init();
          } else {
            ElMessage({
              type:'error',message:'删除失败'
            })
          }
        });
      }
    },
  });
}

function closeDriveEdit() {
  state.driveEditDialogVisible = false;
 init();
}

function closeFilterDialog() {
  state.filterDialogVisible = false;
}

function addDrive() {
  // Object.assign(state.driveItem, state.$options.data().driveItem);
  state.driveItem.id = null;
  state.driveEditDialogVisible = true;
  console.log(state.supportStrategy);
}

function init() {
  strategyApi().then((response) => {
    state.supportStrategy = response.data.data;
  });

  getDrivesApi().then((response) => {
    let data = response.data.data;
    let drives = []
    // for (let drive in data.driveList) {
    //   console.log()
    //   data[drive].type = data[drive].type.key;
    // }

    state.driveList = response.data.data.driveList;
  });

}

onMounted(() => {
  init();
  setSort();
})


</script>

<style scoped>
.el-row {
  padding: 20px;
}

.el-form-item {
  margin-right: 50px;
}

.card-title {
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
}

.card-content {
  color: rgba(0, 0, 0, 0.85);
  font-size: 25px;
  line-height: 30px;
}

.card-title-button {
  float: right;
  padding: 3px 0;
}

.table-search-input {
  width: 300px;
  float: right;
}

#filterForm .el-row {
  padding: 0;
}

#cacheDialog >>> .el-dialog__body {
  padding: 20px;
}

.table-edit-icon {
  margin-left: 5px;
  color: #409eff;
  cursor: pointer;
}
</style>
