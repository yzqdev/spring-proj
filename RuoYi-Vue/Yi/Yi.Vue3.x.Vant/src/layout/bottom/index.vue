<template>
    <van-tabbar v-model="active" active-color="#FF689B" inactive-color="#9C9C9C" @change="onChange" z-index="100">
  <van-tabbar-item  v-for="item in tabbar.slice(0,2)" :to="item.to" :icon="item.icon">{{item.title}}</van-tabbar-item>
  <van-tabbar-item @click="show = true">
    <template #icon="props">
        <van-icon class="add" name="add-square" color="#FF689B" size="3rem" />
      <!-- <img :src="props.active ? icon.active : icon.inactive" /> -->
    </template>
  </van-tabbar-item>

  <van-tabbar-item  v-for="item in tabbar.slice(3)" :to="item.to" :icon="item.icon">{{item.title}}</van-tabbar-item>
</van-tabbar>

<van-action-sheet v-model:show="show" >


  <router-link to="/imageText"> <van-button class="btn1 btn " style="background-color: #5FBC76;" >发图文</van-button></router-link>
  <van-button class="btn" style="background-color: #FF689B;">(暂未开放)发视频</van-button>
  <van-button class="btn" style="background-color: #F7A63A;">(暂未开放)发文章</van-button>
  <van-button class="btn" style="background-color: #6AB5EE;">(暂未开放)发二手</van-button>


  
  <van-icon  class="icon" name="cross" size="1.5rem" @click="show=false"/>


</van-action-sheet>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const active = ref(0);
const show = ref(false);

let tabbar=ref([
  {icon:"wap-home",to:"/",title:"主页"},
  {icon:"location-o",to:"",title:"发现"},
  {icon:"",to:"",title:""},
  {icon:"friends-o",to:"/shopIndex",title:"商城"},
  // {icon:"friends-o",to:"",title:"商城"},
  {icon:"setting-o",to:"/my",title:"我的"},
])
const onChange=(index:number)=>{
  tabbar.value=[
  {icon:"wap-home-o",to:"/",title:"主页"},
  {icon:"location-o",to:"",title:"发现"},
  {icon:"",to:"",title:""},
  {icon:"friends-o",to:"/shopIndex",title:"商城"},
  // {icon:"friends-o",to:"",title:"商城"},
  {icon:"setting-o",to:"/my",title:"我的"},
];
  tabbar.value[index].icon=tabbar.value[index].icon.replace("-o","")

}
</script>
<style>
.btn1
{
  margin-top: 3rem !important;
}
.btn{
  border-radius:0.5rem;
  height: 4rem;
  width:90%;
margin: 0.5rem 1rem 0.5rem 1rem;
color: #FFFFFF;
}
  .content {
    padding: 16px 16px 160px;
  }
  .icon{
    margin-bottom: 1.5rem;
  }
  .add{
    margin-top: 0.2rem;
  }
</style>
