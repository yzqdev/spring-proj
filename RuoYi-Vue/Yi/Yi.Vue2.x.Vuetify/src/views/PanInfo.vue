<template>
  <v-row>
    <v-col cols="12">
      <material-card color="primary" icon="mdi-account-outline">
        <template #title>
          XX-云盘空间 — <small class="text-body-1">浏览属于你的空间</small>
        </template>

        <v-divider></v-divider>
        <router-link to="/pan">
          <app-btn dark class="ma-4">返回 </app-btn></router-link
        >
        <app-btn class="my-4 mr-4">全部选中</app-btn>
        <app-btn class="my-4 mr-4">清空选中</app-btn>

        <app-btn class="my-4" color="secondary" @click="download"
          >批量下载</app-btn
        ></material-card
      >
    </v-col>

    <v-col
      cols="6"
      xl="1"
      sm="3"
      v-for="i in dirList"
      :key="i"
      class="text-center"
    >
      <v-card>
        <v-icon size="100" @click="enter(i)">mdi-briefcase-account</v-icon>
      </v-card>
      <v-chip class="ma-2" text-color="white">
        <v-checkbox
          v-model="selected"
          color="primary"
          :label="i"
          :value="i"
        ></v-checkbox>
      </v-chip>
    </v-col>

    <v-col
      cols="6"
      xl="1"
      sm="3"
      v-for="i in fileList"
      :key="i"
      class="text-center"
    >
      <v-card>
        <v-icon size="100">mdi-badge-account-outline</v-icon>
      </v-card>
      <v-chip class="ma-2" text-color="white">
        <v-checkbox
          v-model="selected"
          color="primary"
          :label="i"
          :value="i"
        ></v-checkbox>
      </v-chip>
    </v-col>
  </v-row>
</template>
<script>
import panApi from "../api/panApi";
export default {
  data: () => ({
    dirList: [],
    fileList: [],
    selected: [],
    baseurl:""
  }),
  created() {
    this.init();
  },
destroyed(){
this.$store.dispatch("Set_ExtendPath","");
},
        mounted() {
    this.baseurl = process.env.VUE_APP_BASE_API;
  },
  methods: {
    download()
    {
      panApi.Download(this.$store.getters.path,this.selected) .then(res => {
  console.log(res.data)
  //这里linux有问题，可能是nginx的问题，先直接写网关
  window.open("http://ccnetcore.com:19000/api/file/get/"+res.data);
      })
    },
    enter(enterName) {
      this.$store.dispatch("Add_ExtendPath", enterName);
      // console.log(this.$store.state.pan.extendPath);
      this.init();
    },

    init() {
      this.selected=[];
      //  this.$store.dispatch("Set_ExtendPath","");
      // console.log(this.$store.state.pan.basePath);
      // console.log(this.$store.state.pan.extendPath);
      // console.log(this.$store.getters.path);

      panApi.GetPanFiles(this.$store.getters.path).then((response) => {
        const resp = response.data;

        this.dirList = resp.dirList;
        this.fileList = resp.fileList;
      });
    },
  },
};
</script>

