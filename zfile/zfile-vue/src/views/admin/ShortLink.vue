<template>
  <el-card class="zfile-admin-short-card">
    <el-form :inline="true"  :model="searchParam">
      <el-form-item label="直链 Key">
        <el-input   v-model="searchParam.key"></el-input>
      </el-form-item>
      <el-form-item label="路径/文件名">
        <el-input    v-model="searchParam.url"></el-input>
      </el-form-item>
      <el-form-item label="创建日期">
        <el-date-picker
            v-model="date"
            type="daterange"

            unlink-panels
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="dateChange"
            :picker-options="pickerOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            @click="init"


        >
          <el-icon>
            <search/>
          </el-icon>
          查询
        </el-button
        >
      </el-form-item>
    </el-form>

    <el-button
        type="primary"

        @click="openAddLinkItemDialog"
    >
      <el-icon>
        <plus/>
      </el-icon>
      新增直链
    </el-button
    >
    <el-button
        type="danger"

        @click="batchDeleteLinkItem"
    >
      <el-icon>
        <delete/>
      </el-icon>
      删除所选
    </el-button
    >
    <el-table
        :data="linkLog"
        @row-click="handleRowClick"
        @sort-change="sortMethod"
        :default-sort="{ prop: 'createDate', order: 'descending' }"
        ref="shortLinkTable"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" label="序号"></el-table-column>
      <el-table-column type="index" label="序号"></el-table-column>
      <el-table-column
          prop="key"
          :show-overflow-tooltip="true"
          width="100"
          sortable="custom"
          :sort-orders="['ascending', 'descending']"
          label="Key"
      >
        <template v-slot="scope">
          <a target="_blank" :href="siteDomain + '/s/' + scope.row.key">{{
              scope.row.key
            }}</a>
          <el-icon
              class="  table-edit-icon"
              @click="editKey(scope.row.id)"
          >
            <el-icon>
              <edit/>
            </el-icon>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column
          prop="url"
          :show-overflow-tooltip="true"
          sortable="custom"
          :sort-orders="['ascending', 'descending']"
          label="路径"
      >
      </el-table-column>
      <el-table-column
          prop="createDate"
          width="150"
          sortable="custom"
          :sort-orders="['ascending', 'descending']"
          label="创建日期"
      >
      </el-table-column>
      <el-table-column width="120" label="操作">
        <template v-slot="scope">
          <el-popconfirm
              @confirm.stop="deleteLink(scope.row.id)"
              confirmButtonText="确定"
              cancelButtonText="取消"
              icon="el-icon-info"
              iconColor="red"
              title="确定删除此直链"
          >
            <template #reference>
              <el-button

                  class="el-icon-delete"

                  type="danger"
              >删除
                <el-icon>
                  <delete/>
                </el-icon>
              </el-button
              >
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="searchParam.page"
        :page-sizes="[10, 50, 100, 200, 999999]"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="searchParam.limit"
        :total="searchParam.total"
    >
    </el-pagination>

    <el-dialog
        id="cacheDialog"
        width="50%"
        title="直链管理"
        :modal-append-to-body="false"
        v-if="addLinkVisible"
        v-model="addLinkVisible"
    >
      <el-form :model="addLinkModel" label-width="150px">
        <el-form-item label="驱动器 ID">
          <el-select v-model="addLinkModel.driveId" placeholder="请选择驱动器">
            <el-option
                v-for="item in driveList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                  item.id
                }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="路径">
          <el-input
              v-model="addLinkModel.path"
              placeholder="请输入路径地址"
              class="input-with-select"
          >
            <template #prepend>/directlink/</template>
            {{ addLinkModel.driveId }}
          </el-input>
        </el-form-item>
      </el-form><template #footer>
      <div class="dialog-footer">
        <el-button
            type="primary"

            @click="addLinkItemAction"
        >
          <el-icon>
            <check/>
          </el-icon>
          保存
        </el-button
        >
        <el-button

            @click="addLinkVisible = false"
        >
          <el-icon>
            <Close/>
          </el-icon>
          关闭
        </el-button
        >
      </div></template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import {
  delLinkApi,
  delShortLinkApi,
  postShortLinkApi,
  shortLinkKeyApi,
} from "../../utils/shortlink";
import {getAdminConfigApi, getDrivesApi, linkListApi} from "../../utils/apis";
import {ElMessage, ElMessageBox} from "element-plus";
import {onMounted, reactive, ref, toRefs} from "vue";
import {useStore} from "vuex";
import {Check, Close, Delete, Edit, Plus, Search} from "@element-plus/icons-vue";
import common from "../../common";

let shortLinkTable = ref()
let state = reactive({
  addLinkModel: {
    driveId: null,
    path: "",
  }, multipleSelection: [],
  addLinkVisible: false,
  linkUrl: "",
  linkLog: [],
  searchParam: {
    page: 1,
    limit: 10,
    total: 0,
    url: "",
    key: "",
    orderBy: "",
    orderDirection: "",
  },
  pickerOptions: {
    shortcuts: [
      {
        text: "最近一周",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          picker.$emit("pick", [start, end]);
        },
      },
      {
        text: "最近一个月",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
          picker.$emit("pick", [start, end]);
        },
      },
      {
        text: "最近三个月",
        onClick(picker) {
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
          picker.$emit("pick", [start, end]);
        },
      },
    ],
  },
  date: ["", ""],
  driveList: [],
  siteDomain: "",
})
let {
  addLinkModel,
  addLinkVisible,
  linkLog,
  linkUrl,
  searchParam,
  pickerOptions,
  date, multipleSelection,
  driveList,
  siteDomain
} = toRefs(state)
let store = useStore()

function handleSelectionChange(val: any) {

  state.multipleSelection = val
  console.log(state.multipleSelection)
}

function editKey(id) {
  ElMessageBox.prompt("请输入要修改为的 Key。", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
  }).then(({value}) => {
    shortLinkKeyApi({params: {id: id, newKey: value}}).then(() => {
      ElMessage({
        message: "修改成功",
        type: "success",
      });
      init();
    });
  });
}

//点击行触发，选中或不选中复选框
function handleRowClick(row) {
  shortLinkTable.value.toggleRowSelection(row);
}

function batchDeleteLinkItem() {
  let selection = state.multipleSelection
  if (state.multipleSelection.length === 0) {
    ElMessage({
      type: 'warning', message: "请至少选中一行数据"
    })
    return;
  }

  ElMessageBox.confirm("删除后不可恢复, 是否确认删除", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
    callback: (action) => {
      if (action === "confirm") {
        let arr: any[] = [];
        for (let index in selection) {
          arr[index] = selection[index].id;
        }

        delShortLinkApi({ids: arr}).then((response) => {
          if (response.data.code === 0) {
            ElMessage({
              type: "success"
              , message: '删除成功'
            })
            init();
          } else {
            ElMessage({type: 'error', message: response.data.msg})

          }
        });
      }
    },
  });
}

function addLinkItemAction() {
  state.addLinkModel.path = common.removeDuplicateSeparator(
      "/" + state.addLinkModel.path
  );
  postShortLinkApi(  state.addLinkModel ).then(() => {
    ElMessage({
      type: 'success'
      , message: '添加成功'
    })
    init();
    state.addLinkVisible = false;
  });
}

function openAddLinkItemDialog() {
  state.addLinkVisible = true;
}

function dateChange() {
  if (state.date) {
    state.searchParam.dateFrom = state.date[0];
    state.searchParam.dateTo = state.date[1];
  } else {
    state.searchParam.dateFrom = "";
    state.searchParam.dateTo = "";
  }
}

function sortMethod({prop, order}) {
  state.searchParam.orderBy = prop;
  state.searchParam.orderDirection = order === "descending" ? "desc" : "asc";
  init();
}

function handleSizeChange(val) {
  state.searchParam.limit = val;
  state.searchParam.page = 1;
  init();
}

function handleCurrentChange(val) {
  state.searchParam.page = val;
  init();
}

function deleteLink(id) {
  delLinkApi(id).then((response) => {
    if (response.data.code === common.responseCode.SUCCESS) {
      ElMessage({
        type: 'success', message: '删除成功'
      })
      init();
    } else {
      ElMessage({
        type: 'error', message: response.data.msg
      })
    }
  });
}

function init() {
  linkListApi(state.searchParam).then((response) => {
    state.linkLog = response.data.data.content;
    state.searchParam.total = response.data.data.totalElements;
    state.searchParam.limit = response.data.data.size;
  });
}

function loadDriveList() {
  getDrivesApi().then((response) => {
    state.driveList = response.data.data.driveList;
  });
}

function getDomainId() {
  getAdminConfigApi().then((response) => {
    state.siteDomain = response.data.data.domain;
  });

}

onMounted(() => {
  init();
  loadDriveList();
  getDomainId();
})


</script>

<style lang="scss"  scoped>
.zfile-admin-short-card{
  :deep(.el-form){
    .el-form-item{
      margin-right: 20px;
    }
  }
}

.el-pagination {
  margin-top: 15px;
}

.table-edit-icon {
  margin-left: 10px;
  cursor: pointer;
}
</style>
