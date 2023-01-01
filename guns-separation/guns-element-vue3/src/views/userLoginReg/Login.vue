<template>
  <div class="main">
    <el-form id="formLogin" class="user-layout-login" ref="formLogin" :model="form" :rules="loginRules">
      <el-tabs
        stretch
        v-model="customActiveKey"
        :style="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick"
      >
        <el-tab-pane name="tab1" label="账号密码登录">
          <el-alert
            v-if="isLoginError"
            type="error"
            showIcon
            style="margin-bottom: 24px"
            :title="this.accountLoginErrMsg"
          />

          <el-form-item v-if="tenantOpen">
            <el-select style="width: 100%" size="large" placeholder="请选择租户" v-model="form.tenantCode">
              <el-option v-for="(item, index) in tenantsList" :key="index" :value="item.code">{{
                item.name
              }}</el-option>
            </el-select>
          </el-form-item>

          <el-form-item prop="account">
            <el-input size="large" type="text" placeholder="账号" v-model="form.account">
              <template #prepend>
                <UserOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input size="large" type="password" autocomplete="false" placeholder="密码" v-model="form.password">
              <template #prepend>
                <LockOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
              </template>
            </el-input>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane name="tab2" label="手机号登录">
          <el-alert
            v-if="isLoginError"
            type="error"
            showIcon
            style="margin-bottom: 24px"
            :message="this.accountLoginErrMsg"
          />
          <el-form-item v-if="tenantOpen">
            <el-select style="width: 100%" size="large" placeholder="请选择租户" v-model="form.tenantCode">
              <el-option v-for="(item, index) in tenantsList" :key="index" :value="item.code">{{
                item.name
              }}</el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input size="large" type="text" placeholder="手机号" v-model="form.mobile">
              <template #prepend>
                <MobileOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
              </template>
            </el-input>
          </el-form-item>

          <el-row :gutter="16">
            <el-col class="gutter-row" :span="16">
              <el-form-item>
                <el-input size="large" type="text" placeholder="验证码" v-model="form.captcha">
                  <template #prepend>
                    <MailOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col class="gutter-row" :span="8">
              <el-button
                class="getCaptcha"
                tabindex="-1"
                :disabled="state.smsSendBtn"
                @click.stop.prevent="getCaptcha"
                v-text="(!state.smsSendBtn && '获取验证码') || state.time + ' s'"
              ></el-button>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>

      <el-form-item>
        <el-checkbox v-model="form.rememberMe">自动登录</el-checkbox>
        <router-link :to="{ name: 'recover', params: { user: 'aaa' } }" class="forge-password" style="float: right"
          >忘记密码</router-link
        >
      </el-form-item>

      <el-form-item style="margin-top: 24px">
        <el-button
          size="large"
          type="primary"

          class="login-button"
          @click="handleSubmit"
          :loading="state.loginBtn"
          :disabled="state.loginBtn"
          >确定</el-button
        >
      </el-form-item>

      <div class="user-login-other">
        <span>其他登录方式</span>
        <a>
          <AlipayCircleOutlined class="item-icon" />
        </a>
        <a><TaobaoCircleOutlined class="item-icon" /> </a>
        <a>
          <WeiboCircleOutlined class="item-icon" />
        </a>
        <router-link class="register" :to="{ name: 'register' }">注册账户</router-link>
      </div>
    </el-form>

    <two-step-captcha
      v-if="requiredTwoStepCaptcha"
      :visible="stepCaptchaVisible"
      @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel"
    ></two-step-captcha>
  </div>
</template>

<script>
import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
import { mapActions } from 'vuex'
import { getSmsCaptcha } from '@/api/modular/system/loginManage'
import { defineComponent } from 'vue'
import {
  MobileOutlined,
  AlipayCircleOutlined,
  LockOutlined,
  MailOutlined,
  TaobaoCircleOutlined,
  WeiboCircleOutlined,
  UserOutlined,
} from '@ant-design/icons-vue'

export default defineComponent({
  name: 'Login',
  components: {
    TwoStepCaptcha,
    AlipayCircleOutlined,
    TaobaoCircleOutlined,
    MobileOutlined,
    WeiboCircleOutlined,
    LockOutlined,
    MailOutlined,
    UserOutlined,
  },
  data() {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: {
        account: 'yzqbot',
        password: 'Lenovo2020',
        tenantCode: '',
        captcha: '',
        mobile: '',
      },
      loginRules: {
        tenantCode: [{ required: true, message: '请选择租户！' }],
        account: [
          { trigger: 'change', required: true, message: '请输入帐户名' },
          { validator: this.handleUsernameOrEmail },
        ],
        captcha: [{ trigger: 'blur', required: true, message: '请输入验证码' }],
        mobile: [{ trigger: 'change', required: true, pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号' }],
        password: [{ trigger: 'blur', required: true, message: '请输入密码' }],
      },
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false,
      },
      accountLoginErrMsg: '',
      tenantOpen: false,
      tenantsList: [],
    }
  },
  created() {
    // this.requiredTwoStepCaptcha = true
  },
  methods: {
    ...mapActions(['Login', 'Logout', 'dictTypeData']),
    // handler
    handleUsernameOrEmail(rule, value) {
      const { state } = this
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        state.loginType = 0
      } else {
        state.loginType = 1
      }
      return Promise.resolve()
    },
    handleTabClick(key) {
      this.isLoginError = false
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleSubmit( ) {

      const { state, customActiveKey, Login } = this

      state.loginBtn = true

      const validateFieldsKey = customActiveKey === 'tab1' ? ['account', 'password'] : ['mobile', 'captcha']
      if (this.tenantOpen) {
        validateFieldsKey.push('tenantCode')
      }
      this.$refs.formLogin.validateField(validateFieldsKey, (errMsg) => {
        if (!errMsg) {

          const loginParams = { ...this.form }
          delete loginParams.account
          loginParams[!state.loginType ? 'email' : 'account'] = this.form.account
          loginParams.password = this.form.password
          if (this.tenantOpen) {
            loginParams.tenantCode = this.form.tenantCode
          }


          Login(loginParams)
            .then((res) => this.loginSuccess(res))
            .catch((err) => this.requestFailed(err))
            .finally(() => {
              state.loginBtn = false
            })
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    getCaptcha(e) {
      e.preventDefault()
      const { state } = this

      this.$refs.formLogin.validateFields().then((err, values) => {
        if (!err) {
          state.smsSendBtn = true

          const interval = window.setInterval(() => {
            if (state.time-- <= 0) {
              state.time = 60
              state.smsSendBtn = false
              window.clearInterval(interval)
            }
          }, 1000)

          const hide = this.$message.loading('验证码发送中..', 0)
          getSmsCaptcha({ mobile: values.mobile })
            .then((res) => {
              setTimeout(hide, 2500)
              this.$notification['success']({
                message: '提示',
                description: '验证码获取成功，您的验证码为：' + res.result.captcha,
                duration: 8,
              })
            })
            .catch((err) => {
              setTimeout(hide, 1)
              clearInterval(interval)
              state.time = 60
              state.smsSendBtn = false
              this.requestFailed(err)
            })
        }
      })
    },
    stepCaptchaSuccess() {
      this.loginSuccess()
    },
    stepCaptchaCancel() {
      this.Logout().then(() => {
        this.loginBtn = false
        this.stepCaptchaVisible = false
      })
    },
    loginSuccess(res) {
      this.$router.push({ path: '/' })
      this.isLoginError = false
      // 加载字典所有字典到缓存中
      this.dictTypeData().then((res) => {})
    },
    requestFailed(err) {
      this.accountLoginErrMsg = err
      this.isLoginError = true
    },
  },
})
</script>
<style lang="less" scoped>
.main {
  .user-layout-login {
    label {
      font-size: 14px;
    }

    .getCaptcha {
      display: block;
      width: 100%;
      height: 40px;
    }

    .forge-password {
      font-size: 14px;
    }

    .login-button {
      padding: 0 15px;
      font-size: 16px;
      height: 40px;
      width: 100%;
    }

    .user-login-other {
      text-align: left;
      margin-top: 24px;
      line-height: 22px;

      .item-icon {
        font-size: 24px;
        color: rgba(0, 0, 0, 0.2);
        margin-left: 16px;
        vertical-align: middle;
        cursor: pointer;
        transition: color 0.3s;

        &:hover {
          color: #1890ff;
        }
      }

      .register {
        float: right;
      }
    }
  }
}
</style>
