<template>
  <div class="setting-drawer">
    <el-drawer
      size="300px"


      direction="rtl"
      @close="onClose"

      :with-header='false'
      :show-close='false'
      append-to-body
      v-model="visible"
      custom-class="settings"
    >
      <div class="setting-drawer-index-content">
        <div :style="{ marginBottom: '24px' }">
          <h3 class="setting-drawer-index-title">整体风格设置</h3>

          <div class="setting-drawer-index-blockChecbox">
            <el-tooltip content="暗色菜单风格">
              <div class="setting-drawer-index-item" @click="handleMenuTheme('dark')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/LCkqqYNmvBEbokSDscrm.svg" alt="dark" />
                <div class="setting-drawer-index-selectIcon" v-if="navTheme === 'dark'">
                  <CheckOutlined />
                </div>
              </div>
            </el-tooltip>

            <el-tooltip content="亮色菜单风格">
              <div class="setting-drawer-index-item" @click="handleMenuTheme('light')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/jpRkZQMyYRryryPNtyIC.svg" alt="light" />
                <div class="setting-drawer-index-selectIcon" v-if="navTheme !== 'dark'">
                  <CheckOutlined />
                </div>
              </div>
            </el-tooltip>
          </div>
        </div>

        <div :style="{ marginBottom: '24px' }">
          <h3 class="setting-drawer-index-title">主题色</h3>

          <div style="height: 20px">
            <el-tooltip
              class="setting-drawer-theme-color-colorBlock"
              :content="item.key"
              v-for="(item, index) in colorList"
              :key="index"
            >

              <el-tag :color="item.color" @click="changeColor(item.color)">
                <CheckOutlined v-if='item.color==primaryColor' />

              </el-tag>
            </el-tooltip>
          </div>
        </div>
        <el-divider />

        <div :style="{ marginBottom: '24px' }">
          <h3 class="setting-drawer-index-title">导航模式</h3>

          <div class="setting-drawer-index-blockChecbox">
            <el-tooltip content='侧边栏导航'>
              <div class="setting-drawer-index-item" @click="handleLayout('sidemenu')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/JopDzEhOqwOjeNTXkoje.svg" alt="sidemenu" />
                <div class="setting-drawer-index-selectIcon" v-if="layoutMode === 'sidemenu'">
                 <CheckOutlined />
                </div>
              </div>
            </el-tooltip>

            <el-tooltip content='顶部栏导航 '>
              <div class="setting-drawer-index-item" @click="handleLayout('topmenu')">
                <img src="https://gw.alipayobjects.com/zos/rmsportal/KDNDBbriJhLwuqMoxcAr.svg" alt="topmenu" />
                <div class="setting-drawer-index-selectIcon" v-if="layoutMode !== 'sidemenu'">
                   <CheckOutlined />
                </div>
              </div>
            </el-tooltip>
          </div>
          <div :style="{ marginTop: '24px' }">

              <el-list>
                <el-tooltip content='该设定仅 [顶部栏导航] 时有效' >

                  <el-select
                    size="small"
                    style="width: 80px"
                    :defaultValue="contentWidth"
                    @change="handleContentWidthChange"
                  >
                    <el-option value="Fixed">固定</el-option>
                    <el-option value="Fluid" v-if="layoutMode !== 'sidemenu'">流式</el-option>
                  </el-select>
                </el-tooltip>
                <el-list>
                  <div slot="title">内容区域宽度</div>
                </el-list>
              </el-list>
              <el-list>
                <el-switch slot="actions" size="small" :defaultChecked="fixedHeader" @change="handleFixedHeader" />
                <el-list>
                  <div slot="title">固定 Header</div>
                </el-list>
              </el-list>
              <el-list>
                <el-switch
                  slot="actions"
                  size="small"
                  :disabled="!fixedHeader"
                  :defaultChecked="autoHideHeader"
                  @change="handleFixedHeaderHidden"
                />
                <el-list>
                  <el-tooltip  content='固定 Header 时可配置' placement="left">
                    <div :style="{ opacity: !fixedHeader ? '0.5' : '1' }">下滑时隐藏 Header</div>
                  </el-tooltip>
                </el-list>
              </el-list>
              <el-list>
                <el-switch
                  slot="actions"
                  size="small"
                  :disabled="layoutMode === 'topmenu'"
                  :defaultChecked="fixSiderbar"
                  @change="handleFixSiderbar"
                />
                <el-list>
                  <div slot="title" :style="{ textDecoration: layoutMode === 'topmenu' ? 'line-through' : 'unset' }">
                    固定侧边菜单
                  </div>
                </el-list>
              </el-list>

          </div>
        </div>
        <el-divider />

        <div :style="{ marginBottom: '24px' }">
          <h3 class="setting-drawer-index-title">其他设置</h3>
          <div>

                <el-switch slot="actions" size="small" :defaultChecked="colorWeak" @change="onColorWeak" />
                <el-list>
                  <div slot="title">色弱模式</div>
                </el-list>

                <el-switch slot="actions" size="small" :defaultChecked="multiTab" @change="onMultiTab" />
                <el-list>
                  <div slot="title">多页签模式</div>
                </el-list>

          </div>
        </div>
        <el-divider />
        <div :style="{ marginBottom: '24px' }">
          <el-button @click="doCopy" icon="copy" block>拷贝设置</el-button>
          <el-alert type="warning" :style="{ marginTop: '24px' }">
            <span  >
              配置栏只在开发环境用于预览，生产环境不会展现，请手动修改配置文件。修改配置文件后，需要清空本地缓存和LocalStorage
              <a
                href="https://github.com/sendya/ant-design-pro-vue/blob/master/src/config/defaultSettings.js"
                target="_blank"
                >src/config/defaultSettings.js</a
              >
            </span>
          </el-alert>
        </div>
      </div>
    </el-drawer>
    <div class="setting-drawer-index-handle" :style="{ right: visible ? `300px` : `0` }" @click="toggle">
      <SettingOutlined v-if="!visible" />
      <CloseOutlined v-else />
    </div>
  </div>
</template>

<script>
/* import { DetailList } from '@/components'
import SettingItem from './SettingItem' */
import config from '@/config/defaultSettings'
import { updateTheme, updateColorWeak, colorList } from './settingConfig'
import { mixin, mixinDevice } from '@/utils/mixin'
import { SettingOutlined, CloseOutlined, CheckOutlined } from '@ant-design/icons-vue'
import ElList from '@/components/ElList'
export default {
  name: 'SettingDrawer',
  components: {
    SettingOutlined,
    CloseOutlined,
    CheckOutlined,ElList
    // DetailList,
    // SettingItem
  },
  mixins: [mixin, mixinDevice],
  data() {
    return {
      visible: false,
      colorList,
    }
  },
  watch: {},
  mounted() {
    updateTheme(this.primaryColor)
    if (this.colorWeak !== config.colorWeak) {
      updateColorWeak(this.colorWeak)
    }
  },
  methods: {
    showDrawer() {
      this.visible = true
    },
    onClose() {
      this.visible = false
    },
    toggle() {
      this.visible = !this.visible
    },
    onColorWeak(checked) {
      this.$store.dispatch('ToggleWeak', checked)
      updateColorWeak(checked)
    },
    onMultiTab(checked) {
      this.$store.dispatch('ToggleMultiTab', checked)
    },
    handleMenuTheme(theme) {
      this.$store.dispatch('ToggleTheme', theme)
    },
    doCopy() {
      // get current settings from mixin or this.$store.state.app, pay attention to the property name
      const text = `export default {
  primaryColor: '${this.primaryColor}', // primary color of ant design
  navTheme: '${this.navTheme}', // theme for nav menu
  layout: '${this.layoutMode}', // nav menu position: sidemenu or topmenu
  contentWidth: '${this.contentWidth}', // layout of content: Fluid or Fixed, only works when layout is topmenu
  fixedHeader: ${this.fixedHeader}, // sticky header
  fixSiderbar: ${this.fixSiderbar}, // sticky siderbar
  autoHideHeader: ${this.autoHideHeader}, //  auto hide header
  colorWeak: ${this.colorWeak},
  multiTab: ${this.multiTab},
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true',
  // vue-ls options
  storageOptions: {
    namespace: 'pro__',
    name: 'ls',
    storage: 'local',
  }
}`
      this.$copyText(text)
        .then((message) => {
          console.log('copy', message)
          this.$message.success('复制完毕')
        })
        .catch((err) => {
          console.log('copy.err', err)
          this.$message.error('复制失败')
        })
    },
    handleLayout(mode) {
      this.$store.dispatch('ToggleLayoutMode', mode)
      // 因为顶部菜单不能固定左侧菜单栏，所以强制关闭
      this.handleFixSiderbar(false)
    },
    handleContentWidthChange(type) {
      this.$store.dispatch('ToggleContentWidth', type)
    },
    changeColor(color) {
      if (this.primaryColor !== color) {
        this.$store.dispatch('ToggleColor', color)
        updateTheme(color)
      }
    },
    handleFixedHeader(fixed) {
      this.$store.dispatch('ToggleFixedHeader', fixed)
    },
    handleFixedHeaderHidden(autoHidden) {
      this.$store.dispatch('ToggleFixedHeaderHidden', autoHidden)
    },
    handleFixSiderbar(fixed) {
      if (this.layoutMode === 'topmenu') {
        this.$store.dispatch('ToggleFixSiderbar', false)
        return
      }
      this.$store.dispatch('ToggleFixSiderbar', fixed)
    },
  },
}
</script>

<style lang="less">
.settings {

   .el-drawer__body {
    padding: 20px;
  .setting-drawer-index-content {
    .setting-drawer-index-blockChecbox {
      display: flex;

      .setting-drawer-index-item {
        margin-right: 16px;
        position: relative;
        border-radius: 4px;
        cursor: pointer;

        img {
          width: 48px;
        }

        .setting-drawer-index-selectIcon {
          position: absolute;
          top: 0;
          right: 0;
          width: 100%;
          padding-top: 15px;
          padding-left: 24px;
          height: 100%;
          color: #1890ff;
          font-size: 14px;
          font-weight: 700;
        }
      }
    }
    .setting-drawer-theme-color-colorBlock {
      width: 20px;
      height: 20px;
      border-radius: 2px;
      float: left;
      cursor: pointer;
      margin-right: 8px;
      padding-left: 0px;
      padding-right: 0px;
      text-align: center;
      color: #fff;
      font-weight: 700;

      i {
        font-size: 14px;
      }
    }
  }}
}

.setting-drawer-index-handle {
  position: absolute;
  top: 240px;
  background: #1890ff;
  width: 48px;
  height: 48px;
  right: 0px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  pointer-events: auto;
  z-index: 1001;
  text-align: center;
  font-size: 16px;
  border-radius: 4px 0 0 4px;

  i {
    color: rgb(255, 255, 255);
    font-size: 20px;
  }
}
</style>
