<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script>
import zhCn from 'element-plus/lib/locale/lang/zh-cn'
import { AppDeviceEnquire } from '@/utils/mixin'
import bootstrap from './core/bootstrap'
export default {
  mixins: [AppDeviceEnquire],
  data() {
    return {
      locale: zhCn,
    }
  },
  created: bootstrap,
  watch: {
    $route: {
      handler: function (val) {
        console.log(this.$route)
      },
      immediate: true,
    },
  },
  mounted() {},
}
</script>
