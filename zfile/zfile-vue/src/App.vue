<script setup lang="ts">
import {onBeforeMount, onBeforeUnmount, onMounted, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useCookies} from "@vueuse/integrations/useCookies";

const {
  get,
  getAll,
  set,
  remove,
  addChangeListener,
  removeChangeListener
} = useCookies(['satoken'], {doNotParse: false, autoUpdateDependencies: false})

const router=useRouter()
const route=useRoute()
function unloadHandler() {
  localStorage.removeItem("aplayer-setting");
}

onBeforeMount(() => {
  unloadHandler();
});
onMounted(() => {

  window.addEventListener("unload", (e) => unloadHandler(e));
});
onBeforeUnmount(() => {
  window.removeEventListener("unload", (e) => unloadHandler(e));
});
watch(route,(newVal) => {
  if (newVal.path.includes("login")&&get("satoken")) {
    router.push({name:'adminIndex'})
  }
},{immediate:true})
</script>

<template>
  <router-view />
</template>

<style>
#app {
  font-family: "Lato", "PingFang SC", "Microsoft YaHei", sans-serif !important;
  font-size: 16px;
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #555;
  overflow-x: hidden;
  height: 100%;
}

html,
body {
  height: 100%;
  margin: unset;
  overflow: hidden;
}

.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

/* ----- 滚动条样式 ----- */

::-webkit-scrollbar {
  width: 6px;
  height: 8px;
  background: rgba(144, 147, 153, 0.3);
}
::-webkit-scrollbar-button:vertical {
  display: none;
}
::-webkit-scrollbar-track,
::-webkit-scrollbar-corner {
  background-color: #e2e2e2;
}
::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: #a6a6a6;
}
::-webkit-scrollbar-thumb:vertical:hover {
  background-color: #7f7f7f;
}
::-webkit-scrollbar-thumb:vertical:active {
  background-color: rgba(0, 0, 0, 0.38);
}

/* ----- 滚动条样式 ----- */
</style>
