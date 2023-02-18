<template>
  <span class="subtitle">{{ showTime }}</span>
</template>
<style scoped>
</style>

<script setup lang="ts">
import { ref, computed } from "vue";

const props = defineProps<{ time: string }>();
const showTime = computed(() => {
    var dataTime=new Date(Date.parse(props.time));
   const hour:number= getHour(dataTime,new Date())
   if(hour<=0)
   {
    return "刚刚"
   }
   if(hour<=6)
   {
    return hour+"小时前"
   }
    return props.time;

});
const  getHour=(s1:Date, s2:Date)=> {
    var ms = s2.getTime() - s1.getTime();
    if (ms < 0) return 0;
    return Math.floor(ms / 1000 / 60 / 60);  //小时
}
</script>
<style scoped>
.subtitle{
color: #CBCBCB;

}

</style>