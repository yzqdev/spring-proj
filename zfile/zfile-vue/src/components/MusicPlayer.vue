<template>
<div id="player" style="width: 20rem; "></div>
</template>

<script setup>
import 'aplayer/dist/APlayer.min.css';
import APlayer from 'aplayer';
import {nextTick, onBeforeMount, onMounted, watch} from "vue";
import {useRoute} from "vue-router";
let props=defineProps({
  audioList:{
    type:Array
  },current:{
    type:Number
  }
})
let route=useRoute()
let audios=$ref([]);
console.log('audio==.',audios)

watch(props.current,(val,oldValue) => {
  console.log(props.current)
  nextTick(() => {

console.log("current",window.ap)
    window.ap.list.switch(props.current)
  })
}, {deep:true,immediate:true})
watch(props.audioList.length ,(val,oldVal)=>{
    console.log(`%c路由切换,显示英语`,`color:red;font-size:16px;background:transparent`)
    console.log('props=>',val)
    props.audioList.map((item) => {
      item. url=encodeURI(item.src)

      audios.push({
        name: item.name,
        artist: 'artist1',
        url: encodeURI(item.src),
        theme: '#ebd0c2'
      })
    })
  console.log(audios)

},{immediate:true})
onMounted(() => {
nextTick(() => {
  window.ap = new APlayer({
    container: document.getElementById('player'),

    theme: '#FADFA3',

    fixed: true,
    preload: 'auto',
    volume: 0.7,

    audio:audios
  });
  console.log("nextick ap=>",window.ap)

})

})
</script>

<style scoped>

</style>
