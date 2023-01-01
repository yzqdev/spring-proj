<template>
  <div>
    <el-form
      id="filterForm"
      ref="filterForm"
      :model="filterForm"
      label-width="120px"
    >
      <div v-for="(item, index) in filterForm.filterList" :key="index">
        <el-row>
          <el-col :span="20">
            <el-form-item
              :prop="'filterList.' + index + '.expression'"
              :rules="{
                required: true,
                message: '此项不能为空',
                trigger: 'blur',
              }"
              :label="'表达式 ' + (index + 1)"
            >
              <el-input  v-model="item.expression"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-button
              class="zfile-filter-delete-btn"
              type="danger"

              @click="deleteFilterItem(index)"

              >删除<el-icon><delete/></el-icon>
            </el-button>
          </el-col>
        </el-row>
      </div>
      <el-form-item>
        <el-button
          type="primary"


          @click="addFilterItem"
          ><el-icon><plus/></el-icon>添加更多</el-button
        >
      </el-form-item>
      <el-form-item>
        <span class="zfile-word-aux zfile-margin-left-unset">
          表达式规则:
          <br />
          *: 单级路径通配符，如表达式 /*.jpg，可以匹配根路径下所有的 jpg
          后缀的文件
          <br />
          **: 多级路径通配符，如表达式 **.jpg，可以匹配所有路径下的 jpg
          后缀的文件
          <br />
          注意：<b>/a.png</b> 表示根路径下的 a.png。 <b>/a/b/c.png</b>，表示
          /a/b/ 路径下的 c.png。 <b>a.png</b>，什么都不表示，因为未标注路径。
        </span>
      </el-form-item>
    </el-form>

    <div slot="footer" class="zfile-filter-save-btn">
      <el-button
        type="primary"


        @click="saveFilterForm"
        >保存<el-icon><check/></el-icon></el-button
      >
    </div>
  </div>
</template>

<script>
import { getDriveFilterApi, postDriveFilterApi } from "../../utils/cache";
import {Check, Delete, Plus} from "@element-plus/icons-vue";

export default {
  name: "FilterPattern",
  components: {Delete, Plus, Check},
  props: {
    close: {
      type: Function,
    },
    driveId: null,
  },
  data() {
    return {
      filterForm: {
        filterList: [],
      },
    };
  },
  methods: {
    loadFilterData() {
      getDriveFilterApi(this.driveId).then((response) => {
        this.filterForm.filterList = response.data.data;
      });
    },
    addFilterItem() {
      this.filterForm.filterList.push({
        expression: "",
        driveId: this.driveId,
      });
    },
    deleteFilterItem(index) {
      this.filterForm.filterList.splice(index, 1);
    },
    saveFilterForm() {
      postDriveFilterApi(this.driveId, this.filterForm.filterList).then(() => {
        this.$message({
          message: "保存成功",
          type: "success",
        });
        this.close();
      });
    },
  },
  mounted() {
    this.loadFilterData();
  },
};
</script>

<style scoped>
.zfile-filter-delete-btn {
  margin-left: 10px;
  margin-top: 5px;
}

.zfile-filter-save-btn {
  text-align: right;
  margin-top: 15px;
}
</style>
