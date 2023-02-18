import myaxios from '@/utils/myaxios'

// 登录方法
export function login(username:string, password:string, code:string, uuid:string) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return myaxios({
    url: '/account/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data:any) {
  return myaxios({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return myaxios({
    url: '/account/getUserAllInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return myaxios({
    url: '/account/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return myaxios({
    url: '/account/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}