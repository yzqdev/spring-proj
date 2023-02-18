<template>
    这里是商品搜索页
    <br>
   
    <br>
    <router-link to="/shopIndex">返回商品首页</router-link>
这个是spu:

<div v-for="item in spuList" :key="item.id">商品名称：{{item.spuName}}
  <router-link :to="`/shopDetails?spuId=${item.id}`">点击进入该商品详情</router-link>
   <div v-for="spec in item.specsSpuAllInfo" :key="spec">规格组： {{spec.specsGroupName}}
    <div v-for="name in spec.specsNames" :key="name">规格值： {{name}}
   
</div>

</div>
<hr>
</div>

</template>

<script setup lang="ts">
import {ref,reactive,toRefs} from 'vue'
import spuApi from "@/api/spuApi";
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    // dictName: undefined,
    // dictType: undefined,
    isDeleted: false,
  },
});
const spuList = ref<any[]>([]);
const { queryParams } = toRefs(data);
spuApi.pageList(queryParams.value).then((response:any)=>{
    spuList.value=response.data.data;
})
</script>