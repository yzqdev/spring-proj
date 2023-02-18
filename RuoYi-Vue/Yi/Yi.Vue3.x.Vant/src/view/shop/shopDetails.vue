<template>
    这里是商品详情页，当所有规格组全部选择完后，就会匹配sku列表，匹配成功才显示价格，即绑定好的sku

    <div >商品名称：{{spuItem.spuName}}</div>
   <div v-for="spec in spuItem.specsSpuAllInfo" :key="spec">规格组： {{spec.specsGroupName}}
    <div v-for="name in spec.specsNames" :key="name">规格值： {{name}}</div>
   
</div>
<hr>
 <div v-for="sku in spuItem.skus" :key="sku"> 价格：{{sku.price}}<br>Sku：{{sku}}
    <hr>
</div>


<br>
    <router-link to="/shopIndex">返回商品首页</router-link>

    <van-action-bar>
  <van-action-bar-icon icon="chat-o" text="客服" dot />
  <van-action-bar-icon icon="cart-o" text="购物车" badge="5" />
  <van-action-bar-icon icon="shop-o" text="店铺" badge="12" />
  <van-action-bar-button type="warning" text="加入购物车" />
  <van-action-bar-button type="danger" text="立即购买" />
</van-action-bar>
</template>
<script lang="ts" setup>
import {ref,onMounted} from 'vue'
import { useRouter } from 'vue-router'
import spuApi from "@/api/spuApi";
const router = useRouter();

const spuItem=ref<any>({});

  onMounted(() => {
    const spuId=router.currentRoute.value.query.spuId;
    // 打印
    spuApi.getById(spuId).then((response:any)=>{
        spuItem.value=response.data

    })


  })
</script>>