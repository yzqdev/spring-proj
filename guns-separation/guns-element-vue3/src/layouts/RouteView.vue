<template>
  <template v-if="showKeep">
    <router-view v-slot="{ Component }">
      <transition>
        <keep-alive>
          <component :is="Component" />
        </keep-alive>
      </transition>
    </router-view> </template
  ><template v-else><router-view /> </template>
</template>

<script lang="jsx">
import { KeepAlive } from 'vue'
export default {
  name: 'RouteView',
  props: {
    keepAlive: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {}
  },
  computed: {
    showKeep() {
      const {
        $route: { meta },
        $store: { getters },
      } = this
      return this.keepAlive || getters.multiTab || meta.keepAlive
    },
  },
   created() {
     // 这里增加了 multiTab 的判断，当开启了 multiTab 时
     // 应当全部组件皆缓存，否则会导致切换页面后页面还原成原始状态
     // 若确实不需要，可改为 return meta.keepAlive ? inKeep : notKeep
   }
}
</script>
