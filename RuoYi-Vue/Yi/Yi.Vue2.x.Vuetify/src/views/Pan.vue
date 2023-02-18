<template>
  <v-row>
    <v-col cols="12">
      <material-card color="primary" icon="mdi-account-outline">
        <template #title>
          企业云盘 — <small class="text-body-1">选择你的角色空间</small>
        </template>
      </material-card>
    </v-col>
    <v-col cols="6" xl="1" sm="3" v-for="item in roleNameList" :key="item"  class="text-center" @click="toInfo(item.id,item.file_path)">
    
      <v-card>
        <v-icon size="100">mdi-briefcase-account</v-icon>
      </v-card>
      <v-chip class="ma-2" color="primary" text-color="white">
        {{item.role_name}}
      </v-chip>
    </v-col>
    <v-col cols="6">
      <v-card> </v-card>
    </v-col>
  </v-row>
</template>

<script>
import userApi from "../api/userApi";
export default {
  created() {
      this.init();
  },

  methods: {
      toInfo(roleId,filePath)
      {
   this.$store.dispatch("Set_BasePath", filePath);
        this.$router.push(`/paninfo?roleId=${roleId}`);
      },
      init(){
         userApi.GetUserInRolesByHttpUser().then(response=>{
             const resp=response.data;
             this.roleNameList=resp.roles
         })
      }
  },
  data: () => ({
      roleNameList:[],
    selected: [],
  }),
};
</script>

