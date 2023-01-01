<template>
  <div class='main user-layout-register'>
    <h3><span>注册</span></h3>
    <a-form-model ref='formRegister' :model='form' :rules='rule' id='formRegister'>
      <a-form-model-item prop='username'>
        <a-input
          size='large'
          type='text'
          placeholder='用户名'
          v-model='form.username'
        ></a-input>
      </a-form-model-item>
      <a-form-model-item prop='email'>
        <a-input
          size='large'
          type='text'
          placeholder='邮箱'
          v-model='form.email'
        ></a-input>
      </a-form-model-item>

      <a-popover
        placement='rightTop'
        :trigger="['focus']"
        :getPopupContainer='(trigger) => trigger.parentElement'
        v-model='state.passwordLevelChecked'>
        <template slot='content'>
          <div :style="{ width: '240px' }">
            <div :class="['user-register', passwordLevelClass]">强度：<span>{{ passwordLevelName }}</span></div>
            <a-progress :percent='state.percent' :showInfo='false' :strokeColor=' passwordLevelColor ' />
            <div style='margin-top: 10px;'>
              <span>请至少输入 6 个字符。请不要使用容易被猜到的密码。</span>
            </div>
          </div>
        </template>
        <a-form-model-item prop='password'>
          <a-input
            size='large'
            type='password'
            @click='handlePasswordInputClick'
            autocomplete='false'
            placeholder='至少6位密码，区分大小写'
            v-model='form.password'
          ></a-input>
        </a-form-model-item>
      </a-popover>

      <a-form-model-item prop='password2'>
        <a-input
          size='large'
          type='password'
          autocomplete='false'
          placeholder='确认密码'
          v-model='form.password2'
        ></a-input>
      </a-form-model-item>

      <a-form-model-item prop='mobile'>
        <a-input size='large' placeholder='11 位手机号'
                 v-model='form.mobile'>
          <a-select slot='addonBefore' size='large' defaultValue='+86'>
            <a-select-option value='+86'>+86</a-select-option>
            <a-select-option value='+87'>+87</a-select-option>
          </a-select>
        </a-input>
      </a-form-model-item>
      <!--<a-input-group size="large" compact>
            <a-select style="width: 20%" size="large" defaultValue="+86">
              <a-select-option value="+86">+86</a-select-option>
              <a-select-option value="+87">+87</a-select-option>
            </a-select>
            <a-input style="width: 80%" size="large" placeholder="11 位手机号"></a-input>
          </a-input-group>-->

      <a-row :gutter='16'>
        <a-col class='gutter-row' :span='16'>
          <a-form-model-item prop='captcha'>
            <a-input size='large' type='text' placeholder='验证码'
                     v-model='form.captcha'>
              <a-icon slot='prefix' type='mail' :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class='gutter-row' :span='8'>
          <a-button
            class='getCaptcha'
            size='large'
            :disabled='state.smsSendBtn'
            @click.stop.prevent='getCaptcha'
            v-text="!state.smsSendBtn && '获取验证码'||(state.time+' s')"></a-button>
        </a-col>
      </a-row>

      <a-form-model-item>
        <a-button
          size='large'
          type='primary'
          htmlType='submit'
          class='register-button'
          :loading='registerBtn'
          @click.stop.prevent='handleSubmit'
          :disabled='registerBtn'>注册
        </a-button>
        <router-link class='login' :to="{ name: 'login' }">使用已有账户登录</router-link>
      </a-form-model-item>

    </a-form-model>
  </div>
</template>

<script>
import { mixinDevice } from '@/utils/mixin.js'
import { getSmsCaptcha, register } from '@/api/modular/system/loginManage'

const levelNames = {
  0: '低',
  1: '低',
  2: '中',
  3: '强'
}
const levelClass = {
  0: 'error',
  1: 'error',
  2: 'warning',
  3: 'success'
}
const levelColor = {
  0: '#ff0000',
  1: '#ff0000',
  2: '#ff7e05',
  3: '#52c41a'
}
export default {
  name: 'Register',
  components: {},
  mixins: [mixinDevice],
  data() {
    return {
      form: {
        username: 'yzqbot',
        email: '3306359022@qq.com',
        password: 'Lenovo2020',
        password2: 'Lenovo2020',
        mobile: '18856967790',
        captcha: '1234'
      },
      rule: {
        email: [{ required: true, type: 'email', message: '请输入邮箱地址', trigger: ['change', 'blur'] }],
        password: [{
          required: true,
          message: '至少6位密码，区分大小写',
          trigger: ['change', 'blur']
          , validator: this.handlePasswordLevel
        }],
        password2: [{
          required: true,
          message: '至少6位密码，区分大小写',
          trigger: ['change', 'blur']
          , validator: this.handlePasswordCheck
        }],
        mobile: [{
          required: true,
          message: '请输入正确的手机号',
          pattern: /^1[3456789]\d{9}$/,
          trigger: ['change', 'blur']
        }],
        captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      state: {
        time: 60,
        smsSendBtn: false,
        passwordLevel: 0,
        passwordLevelChecked: false,
        percent: 10,
        progressColor: '#FF0000'
      },
      registerBtn: false
    }
  },
  computed: {
    passwordLevelClass() {
      return levelClass[this.state.passwordLevel]
    },
    passwordLevelName() {
      return levelNames[this.state.passwordLevel]
    },
    passwordLevelColor() {
      return levelColor[this.state.passwordLevel]
    }
  },
  methods: {
    handlePasswordLevel(rule, value, callback) {
      let level = 0

      // 判断这个字符串中有没有数字
      if (/[0-9]/.test(value)) {
        level++
      }
      // 判断字符串中有没有字母
      if (/[a-zA-Z]/.test(value)) {
        level++
      }
      // 判断字符串中有没有特殊符号
      if (/[^0-9a-zA-Z_]/.test(value)) {
        level++
      }
      this.state.passwordLevel = level
      this.state.percent = level * 30
      if (level >= 2) {
        if (level >= 3) {
          this.state.percent = 100
        }
        callback()
      } else {
        if (level === 0) {
          this.state.percent = 10
        }
        callback(new Error('密码强度不够'))
      }
    },

    handlePasswordCheck(rule, value, callback) {
      const password = this.form.password
      console.log('value', value)
      if (value === undefined) {
        callback(new Error('请输入密码'))
      }
      if (value && password && value.trim() !== password.trim()) {
        callback(new Error('两次密码不一致'))
      }
      callback()
    },


    handlePasswordInputClick() {
      if (!this.isMobile()) {
        this.state.passwordLevelChecked = true
        return
      }
      this.state.passwordLevelChecked = false
    },

    handleSubmit() {


      this.$refs.formRegister.validate((valid) => {
        if (valid) {
          this.state.passwordLevelChecked = false
          register(this.form).then(({ data }) => {
            console.log(data)
            this.$router.push({ name: 'registerResult', params: this.form })
          })

        }
      })
    },

    getCaptcha(e) {
      e.preventDefault()
      const { form: { validateFields }, state, $message, $notification } = this

      validateFields(['mobile'], { force: true },
        (err, values) => {
          if (!err) {
            state.smsSendBtn = true

            const interval = window.setInterval(() => {
              if (state.time-- <= 0) {
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }, 1000)

            const hide = $message.loading('验证码发送中..', 0)

            getSmsCaptcha({ mobile: values.mobile }).then(res => {
              setTimeout(hide, 2500)
              $notification['success']({
                message: '提示',
                description: '验证码获取成功，您的验证码为：' + res.result.captcha,
                duration: 8
              })
            }).catch(err => {
              setTimeout(hide, 1)
              clearInterval(interval)
              state.time = 60
              state.smsSendBtn = false
              this.requestFailed(err)
            })
          }
        }
      )
    },
    requestFailed(err) {
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
      this.registerBtn = false
    }
  },
  watch: {
    'state.passwordLevel'(val) {
      console.log(val)
    }
  }
}
</script>
<style lang='less'>
.user-register {

  &.error {
    color: #ff0000;
  }

  &.warning {
    color: #ff7e05;
  }

  &.success {
    color: #52c41a;
  }

}

.user-layout-register {
  .ant-input-group-addon:first-child {
    background-color: #fff;
  }
}
</style>
<style lang='less' scoped>
.user-layout-register {

  & > h3 {
    font-size: 16px;
    margin-bottom: 20px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .register-button {
    width: 50%;
  }

  .login {
    float: right;
    line-height: 40px;
  }
}
</style>
