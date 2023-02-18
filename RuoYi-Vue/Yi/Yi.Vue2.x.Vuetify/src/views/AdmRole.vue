<template>
  <material-card color="primary" icon="mdi-account-tie">
    <template #title>
      角色管理 — <small class="text-body-1">角色可分配多个菜单</small>
    </template>
    <ccTable
      :defaultItem="defaultItem"
      :headers="headers"
      :axiosUrls="axiosUrls"
      :btnEnable="btnEnable"
    >
    </ccTable>
  </material-card>
</template>
<script>
import { getBtn } from "../util/btnHandle";
export default {
  created() {
    this.init();
  },
  methods: {
    init() {
            this.btnEnable = getBtn("role")
     this.axiosUrls = {
        get: "/role/GetList",
        update: "/role/Update",
        del: "/role/DeleteList",
        add: "/role/Add",
      };
    }
  },
  data: () => ({
    start: true,
    axiosUrls: {
    },
    btnEnable:{},
    headers: [
      // { text: "编号", align: "start", value: "id" },
           { text: "角色编号", value: "roleCode", sortable: false },
      { text: "角色名", value: "roleName", sortable: false },
      // { text: "云盘地址", value: "file_path", sortable: false },
      // { text: "简介", value: "introduce", sortable: false },
      { text: "操作", value: "actions", sortable: false },
    ],
    defaultItem: {
      role_name: "",
      introduce: "",
    },
  }),
};
</script>