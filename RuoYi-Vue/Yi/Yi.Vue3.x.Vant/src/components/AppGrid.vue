<template>
    <van-row>
        <van-col span="24">
          <p >  {{data.head}}</p>
        </van-col>
        <van-col class="col-body" span="6"  v-for="item in data.body"><van-icon :name="item.icon" size="2rem" /> <br>{{item.title}}</van-col>
    </van-row>

</template>
<script setup lang="ts">
import { ref } from 'vue'
import {AppGridData} from '@/type/class/AppGridData'
defineProps<{ data: AppGridData }>()
const count = ref(0)
</script>
<style scoped>
.col-body
{
text-align: center;
font-size: small;
}
.col-body .van-icon
{

  color: #FF689B;

    margin-bottom: 0.6rem;
}
p{
font-size: large;
font-weight:bold;
}
</style>

