<template>
  <transition name="showHeader">
    <div v-if="visible" class="header-animat">
      <el-header
        v-if="visible"
        :class="[
          fixedHeader && 'ant-header-fixedHeader',
          sidebarOpened ? 'ant-header-side-opened' : 'ant-header-side-closed',
        ]"
        :style="{ padding: '0' }"
      >
        <div v-if="mode === 'sidemenu'" class="header">
          <el-menu mode="horizontal" :default-active="defApp" style="border-bottom: 0; line-height: 62px">
            <template v-if="device === 'mobile'" class="trigger" @click="toggle">
              <menu-fold-outlined v-if="collapsed" /><menu-unfold-outlined v-else />
            </template>
            <template v-else class="trigger" @click="toggle">
              <menu-fold-outlined v-if="collapsed" /><menu-unfold-outlined v-else
            /></template>

            <el-menu-item
              v-for="item in userInfo.apps"
              :index="item.code"
              style="top: 0px"
              @click="switchApp(item.code)"
            >
              {{ item.name }}
            </el-menu-item>
            <user-menu></user-menu>
          </el-menu>
        </div>
        <div v-else :class="['top-nav-header-index', theme]">
          <div class="header-index-wide">
            <div class="header-index-left">
              <logo class="top-nav-header" :show-title="device !== 'mobile'" />
              <s-menu v-if="device !== 'mobile'" mode="horizontal" :menu="menus" :theme="theme" />
              <template v-else class="trigger" @click="toggle"
                ><menu-fold-outlined v-if="collapsed" /><menu-unfold-outlined v-else
              /></template>
            </div>
            <user-menu class="header-index-right"></user-menu>
          </div>
        </div>
      </el-header>
    </div>
  </transition>
</template>

<script>
import UserMenu from '../tools/UserMenu.vue'
import SMenu from '../Menu/index'
import Logo from '../tools/Logo.vue'
import { mixin } from '@/utils/mixin'
import { mapActions, mapGetters } from 'vuex'
import { ALL_APPS_MENU } from '@/store/mutation-types'
import ls from '@/core/ls'
import { MenuFoldOutlined, MenuOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'

export default {
  name: 'GlobalHeader',
  components: {
    UserMenu,
    SMenu,
    Logo,
    MenuOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
  },
  computed: {
    ...mapGetters(['userInfo']),
  },
  created() {
    this.defApp=ls.get(ALL_APPS_MENU)[0].code
    // this.defApp.push(ls.get(ALL_APPS_MENU)[0].code)
  },
  mixins: [mixin],
  props: {
    mode: {
      type: String,
      // sidemenu, topmenu
      default: 'sidemenu',
    },
    menus: {
      type: Array,
      required: true,
    },
    theme: {
      type: String,
      required: false,
      default: 'dark',
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false,
    },
    device: {
      type: String,
      required: false,
      default: 'desktop',
    },
  },
  data() {
    return {
      visible: true,
      oldScrollTop: 0,
      defApp:null,
    }
  },
  mounted() {
    document.addEventListener('scroll', this.handleScroll, { passive: true })
  },
  methods: {
    ...mapActions(['MenuChange']),

    /**
     * 应用切换
     */
    switchApp(appCode) {
      this.defApp = []
      const applicationData = this.userInfo.apps.filter((item) => item.code === appCode)

      this.MenuChange(applicationData[0])
        .then((res) => {
          console.log(res)
          this.$message.info('正在切换应用！', 0)
          // eslint-disable-next-line handle-callback-err
        })
        .catch((err) => {
          this.$message.error('应用切换异常')
        })
    },
    handleScroll() {
      if (!this.autoHideHeader) {
        return
      }

      const scrollTop = document.body.scrollTop + document.documentElement.scrollTop
      if (!this.ticking) {
        this.ticking = true
        requestAnimationFrame(() => {
          if (this.oldScrollTop > scrollTop) {
            this.visible = true
          } else if (scrollTop > 300 && this.visible) {
            this.visible = false
          } else if (scrollTop < 300 && !this.visible) {
            this.visible = true
          }
          this.oldScrollTop = scrollTop
          this.ticking = false
        })
      }
    },
    toggle() {
      this.$emit('toggle')
    },
  },
  beforeDestroy() {
    document.body.removeEventListener('scroll', this.handleScroll, true)
  },
}
</script>

<style lang="less">
@import '../index.less';

.header-animat {
  position: relative;
  z-index: @ant-global-header-zindex;
}
.showHeader-enter-active {
  transition: all 0.25s ease;
}
.showHeader-leave-active {
  transition: all 0.5s ease;
}
.showHeader-enter,
.showHeader-leave-to {
  opacity: 0;
}
</style>
