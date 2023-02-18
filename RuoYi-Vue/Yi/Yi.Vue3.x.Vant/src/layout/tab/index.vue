<template > 
<van-sticky :offset-top="0">
<van-row class="row"  >
  <van-col span="4" class="icon"><van-icon name="sign" size="1.6rem"/></van-col>
<van-col span="16">
  <van-tabs   v-model:active="active"  class="tabs" sticky swipeable color="#FF689B">
    <van-tab v-for="item in tabs" :title="item.title" :to="item.to" class="tab" :style="{fontSize: 0 + 'px'}" ></van-tab>
  </van-tabs>
</van-col>
<van-col span="4"  class="icon"><van-icon name="search" size="1.6rem" /></van-col>
</van-row>
</van-sticky>


<router-view />
<div style=""></div>
</template>
<script setup lang="ts">
import { ref } from "vue";
const active = ref(1);
const tabs=ref([
  {title:"关注",to:"/follow"},
  {title:"推荐",to:"/recommend"},
  {title:"广场",to:"/square"},
])
</script>
<style scoped>
.row{
  background-color: #FFFFFF;
  min-width: 24rem;
}
.tab{

}
.icon{
 padding-top: 0.6rem;
}
.tabs {
  width: 100%;
}
.icon .van-icon
{
  color:#FF689B;
}
</style>
