<template>
  <a-dropdown>
    <span class="action global-lang">
      <a-icon type="global" style="font-size: 16px"/>
    </span>
    <el-menu slot="overlay" style="width: 150px;" @click="SwitchLang">
      <el-menu-item key="zh-CN">
        <a rel="noopener noreferrer">
          <span role="img" aria-label="简体中文">🇨🇳</span> 简体中文
        </a>
      </el-menu-item>
      <el-menu-item key="zh-TW">
        <a rel="noopener noreferrer">
          <span role="img" aria-label="繁体中文">🇭🇰</span> 繁体中文
        </a>
      </el-menu-item>
      <el-menu-item key="en-US">
        <a rel="noopener noreferrer">
          <span role="img" aria-label="English">🇬🇧</span> English
        </a>
      </el-menu-item>
      <el-menu-item key="pt-BR">
        <a rel="noopener noreferrer">
          <span role="img" aria-label="Português">🇧🇷</span> Português
        </a>
      </el-menu-item>
    </el-menu>
  </a-dropdown>
</template>

<script>
// import { mixin as langMixin } from '@/store/i18n-mixin'

export default {
  name: 'LangSelect',
  // mixins: [langMixin],
  data () {
    return {}
  },
  methods: {
    // SwitchLang (row) {
    //   this.setLang(row.key)
    // }
  }
}
</script>
