<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view/>
    </div>
  </a-config-provider>
</template>

<script>
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
import { AppDeviceEnquire } from '@/utils/mixin'
import bootstrap from './core/bootstrap'
export default {
  mixins: [AppDeviceEnquire],
  data () {
    return {
      locale: zhCN
    }
  }, created: bootstrap,
  mounted () {

  }
}
</script>
