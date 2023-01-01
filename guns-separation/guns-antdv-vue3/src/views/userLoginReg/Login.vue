<template>
  <div class="main">
    <a-form
        id="formLogin"
        class="user-layout-login"
        ref="formLogin"
        :model="form"
        :rules="loginRules"
        @submit="handleSubmit"
        @finish="handleFinish"
        @finishFailed="handleFinishFailed"
    >
      <a-tabs
          :activeKey="customActiveKey"
          :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
          @change="handleTabClick"
      >
        <a-tab-pane key="tab1" tab="账号密码登录">
          <a-alert
              v-if="isLoginError"
              type="error"
              showIcon
              style="margin-bottom: 24px"
              :message="this.accountLoginErrMsg"
          />

          <a-form-item v-if="tenantOpen" name="tenantCode">
            <a-select style="width: 100%" size="large" placeholder="请选择租户" v-model:value="form.tenantCode">
              <a-select-option v-for="(item, index) in tenantsList" :key="index" :value="item.code"
              >{{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item name="account">
            <a-input size="large" type="text" placeholder="账号" v-model:value="form.account">
              <template #addonBefore>
                <UserOutlined :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item name="password">
            <a-input size="large" type="password" autocomplete="false" placeholder="密码" v-model:value="form.password">
              <template #addonBefore>
                <LockOutlined :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </template>
            </a-input>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane key="tab2" tab="手机号登录">
          <a-alert
              v-if="isLoginError"
              type="error"
              showIcon
              style="margin-bottom: 24px"
              :message="this.accountLoginErrMsg"
          />
          <a-form-item v-if="tenantOpen" name="tenantCode">
            <a-select style="width: 100%" size="large" placeholder="请选择租户" v-model:value="form.tenantCode">
              <a-select-option v-for="(item, index) in tenantsList" :key="index" :value="item.code"
              >{{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item name="mobile">
            <a-input size="large" type="text" placeholder="手机号" v-model:value="form.mobile">
              <template #addonBefore>
                <MobileOutlined :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </template>
            </a-input>
          </a-form-item>

          <a-row :gutter="16">
            <a-col class="gutter-row" :span="16">
              <a-form-item>
                <a-input size="large" type="text" placeholder="验证码" v-model:value="form.captcha">
                  <template #addonBefore>
                    <MailOutlined :style="{ color: 'rgba(0,0,0,.25)' }"/>
                  </template>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row" :span="8">
              <a-button
                  class="getCaptcha"
                  tabindex="-1"
                  :disabled="state.smsSendBtn"
                  @click.stop.prevent="getCaptcha"
                  v-text="(!state.smsSendBtn && '获取验证码') || state.time + ' s'"
              ></a-button>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>

      <a-form-item>
        <a-checkbox v-model:checked="form.rememberMe">自动登录</a-checkbox>
        <router-link :to="{ name: 'recover', params: { user: 'aaa' } }" class="forge-password" style="float: right"
        >忘记密码
        </router-link>
      </a-form-item>

      <a-form-item style="margin-top: 24px">
        <a-button
            size="large"
            type="primary"
            htmlType="submit"
            class="login-button"
            :loading="state.loginBtn"
            :disabled="state.loginBtn"
        >确定
        </a-button>
      </a-form-item>

      <div class="user-login-other">
        <span>其他登录方式</span>
        <a>
          <AlipayCircleOutlined class="item-icon"/>
        </a>
        <a>
          <TaobaoCircleOutlined class="item-icon"/>
        </a>
        <a>
          <WeiboCircleOutlined class="item-icon"/>
        </a>
        <router-link class="register" :to="{ name: 'register' }">注册账户</router-link>
      </div>
    </a-form>

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
import {mapActions} from 'vuex'
import {getSmsCaptcha} from '@/api/modular/system/loginManage'
import {
  MobileOutlined,
  AlipayCircleOutlined,
  LockOutlined,
  MailOutlined,
  TaobaoCircleOutlined,
  WeiboCircleOutlined,
  UserOutlined,
} from '@ant-design/icons-vue'
import {defineComponent} from "vue";

export default defineComponent({
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
      form: {account: 'admin', password: '123456', tenantCode: '', captcha: '', mobile: '', rememberMe: false},
      loginRules: {
        tenantCode: [{required: true, message: '请选择租户！'}],
        account: [
          {trigger: 'blur', required: true, message: '请输入帐户名'},
          {validator: this.handleUsernameOrEmail},
        ],
        captcha: [{trigger: 'blur', required: true, message: '请输入验证码'}],
        mobile: [{trigger: 'change', required: true, pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号'}],
        password: [{trigger: 'blur', required: true, message: '请输入密码'}],
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
      const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
      if (regex.test(value)) {
        this.state.loginType = 0
      } else {
        this.state.loginType = 1
      }
      return Promise.resolve()
    },
    handleTabClick(key) {
      this.isLoginError = false
      this.customActiveKey = key
      // this.form.resetFields()
    },
    handleFinish(values) {
      console.log(`%c登录`, `color:red;font-size:16px;background:transparent`)
      console.log(values)

      const loginParams = {...values}
      delete loginParams.account
      loginParams[!this.state.loginType ? 'email' : 'account'] = values.account
      loginParams.password = values.password
      if (this.tenantOpen) {
        loginParams.tenantCode = values.tenantCode
      }
      this.Login(loginParams)
          .then((res) => this.loginSuccess(res))
          .catch((err) => this.requestFailed(err))
          .finally(() => {
            this.state.loginBtn = false
          })
    },
    handleFinishFailed(values) {
      console.log(values)
    },
    handleSubmit(e) {
      e.preventDefault()

      this.state.loginBtn = true

      const validateFieldsKey = this.customActiveKey === 'tab1' ? ['account', 'password'] : ['mobile', 'captcha']
      if (this.tenantOpen) {
        validateFieldsKey.push('tenantCode')
      }
      console.log(validateFieldsKey)
      console.log(this.form)
      this.$refs.formLogin
          .validateFields(validateFieldsKey)
          .then((values) => {

          })
          .catch((err) => {
            setTimeout(() => {
              console.error(err)
              this.state.loginBtn = false
            }, 600)
          })
    },
    getCaptcha(e) {
      e.preventDefault()
      const {
        form: {validateFields},
        state,
      } = this

      validateFields(['mobile'], {force: true}, (err, values) => {
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
          getSmsCaptcha({mobile: values.mobile})
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
      this.$router.push({path: '/'})
      this.isLoginError = false
      // 加载字典所有字典到缓存中
      this.dictTypeData().then((res) => {
      })
    },
    requestFailed(err) {
      this.accountLoginErrMsg = err
      this.isLoginError = true
    },
  },
})
</script>

<style lang="less" scoped>
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

  button.login-button {
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
</style>
