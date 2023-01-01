<template>
  <div class="user-wrapper">
    <div class="content-box">
      <a href="https://www.stylefeng.cn" target="_blank">
        <span class="action">
          <QuestionCircleOutlined />
        </span>
      </a>

      <notice-icon class="action" />

      <el-dropdown>
        <span class="action ant-dropdown-link user-dropdown-menu">
          <el-avatar class="avatar" size="small" :src="avatar" />
          <span>{{ nickname }}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown-menu-wrapper">
            <el-dropdown-item key="4" v-if="mode === 'sidemenu'">
              <a @click="appToggled()">
                <SwapOutlined />
                <span>切换应用</span>
              </a>
            </el-dropdown-item>
            <el-dropdown-item key="0">
              <router-link :to="{ name: 'center' }">
                <UserOutlined />
                <span>个人中心</span>
              </router-link>
            </el-dropdown-item>
            <el-dropdown-item key="1">
              <router-link :to="{ name: 'settings' }">
                <SettingOutlined />
                <span>账户设置</span>
              </router-link>
            </el-dropdown-item>

            <el-dropdown-item divided key="3">
              <a href="javascript:;" @click="handleLogout">
                <LogoutOutlined />
                <span>退出登录</span>
              </a>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
<!--    <el-dialog title="切换应用" v-model="visible" @cancel="handleCancel">-->
<!--      <div v-loading="confirmLoading">-->
<!--        <el-form :model="form1">-->
<!--          <el-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="选择应用">-->
<!--            <el-menu mode="inline" :default-selected-keys="this.defApp" style="border-bottom: 0px; line-height: 62px">-->
<!--              <el-dropdown-item-->
<!--                v-for="item in userInfo.apps"-->
<!--                :index="item.code"-->
<!--                style="top: 0px"-->
<!--                @click="switchApp(item.code)"-->
<!--              >-->
<!--                {{ item.name }}-->
<!--              </el-dropdown-item>-->
<!--            </el-menu>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
import NoticeIcon from '@/components/NoticeIcon/NoticeIcon.vue'
import { mapActions, mapGetters } from 'vuex'
import { ALL_APPS_MENU } from '@/store/mutation-types'

import {
  QuestionCircleOutlined,
  SwapOutlined,
  LogoutOutlined,
  UserOutlined,
  SettingOutlined,
} from '@ant-design/icons-vue'

export default {
  name: 'UserMenu',
  components: {
    NoticeIcon,
    QuestionCircleOutlined,
    SwapOutlined,
    UserOutlined,
    SettingOutlined,
    LogoutOutlined,
  },
  props: {
    mode: {
      type: String,
      // sidemenu, topmenu
      default: 'sidemenu',
    },
  },
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      visible: false,
      confirmLoading: false,
      form1: {},
      defApp: [],
    }
  },

  computed: {
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
  },
  methods: {
    ...mapActions(['Logout', 'MenuChange']),

    handleLogout() {
      this.$confirm('真的要注销登录吗 ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.Logout({}).then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 16)
          })
        })
        .catch((err) => {
          this.$message.warning({
            type: 'warning',
            message: '已取消',
          })
        })
    },

    /**
     * 打开切换应用框
     */
    appToggled() {
      this.visible = true
      this.defApp.push(localStorage.getItem(ALL_APPS_MENU)[0].code)
    },

    switchApp(appCode) {
      this.visible = false
      this.defApp = []
      const applicationData = this.userInfo.apps.filter((item) => item.code === appCode)
      const hideMessage = message.loading('正在切换应用！', 0)
      this.MenuChange(applicationData[0])
        .then((res) => {
          hideMessage()
          // eslint-disable-next-line handle-callback-err
        })
        .catch((err) => {
          message.error('应用切换异常')
        })
    },
    handleCancel() {
      this.form1.resetFields()
      this.visible = false
    },
  },
}
</script>

<style lang="less" scoped>
.appRedio {
  border: 1px solid #91d5ff;
  padding: 10px 20px;
  background: #e6f7ff;
  border-radius: 2px;
  margin-bottom: 10px;
  color: #91d5ff;
  /*display: inline;
    margin-bottom:10px;*/
}
</style>
