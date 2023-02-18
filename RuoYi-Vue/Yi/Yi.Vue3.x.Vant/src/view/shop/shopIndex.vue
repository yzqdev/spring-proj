<template >
<div class="back">

  <van-row class="top-div"> <van-col  span="3"><van-icon name="circle" size="2rem" /></van-col>
    <van-col span="18">  <router-link to="/shopSearch"><van-cell-group inset>
  <van-field  label="搜索" placeholder="搜索" />
</van-cell-group>  </router-link></van-col>
    <van-col span="3"><van-icon name="circle" size="2rem" /></van-col></van-row>

  <div class="head-div">
    <van-swipe
      height="100"
      class="my-swipe"
      :autoplay="3000"
      indicator-color="white"
    >
      <van-swipe-item>1</van-swipe-item>
      <van-swipe-item>2</van-swipe-item>
      <van-swipe-item>3</van-swipe-item>
      <van-swipe-item>4</van-swipe-item>
    </van-swipe>
  </div>

  <van-row class="body-row">
    <van-col span="24">
      <van-grid :column-num="4">
        <van-grid-item
          v-for="value in 8"
          :key="value"
          icon="photo-o"
          text="文字"
        />
      </van-grid>
    </van-col>
    <van-col span="24"> <AppCard /></van-col>
    <van-col span="24"> <AppCard /></van-col>
    <van-col span="24"> <AppCard /></van-col>
  </van-row>
  </div>
</template>
<script  setup lang="ts">
import AppCard from "@/components/AppCard.vue";
</script>
<style>

.my-swipe .van-swipe-item {
  color: #fff;
  font-size: 20px;
  line-height: 150px;
  text-align: center;
  background-color: #39a9ed;
}
.body-row {
  padding: 1rem;
  padding-top: 0.2rem;
  margin-top: 1rem;
}
.van-swipe {
  /* margin-top: 10rem;
  margin-left: 1rem;
  margin-right: 1rem; */
}
.head-div {
  padding: 1rem;
  padding-bottom: 0;
}
.body-row .van-col {
  margin-bottom: 2rem;
}
.top-div
{
  padding: 1rem;
  padding-bottom: 0;

}
.back{
  background-color: #f4f4f4;
}
</style>