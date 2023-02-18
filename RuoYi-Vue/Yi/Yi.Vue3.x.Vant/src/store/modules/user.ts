import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
// import defAva from '@/assets/images/profile.jpg'
import {defineStore} from 'pinia'
const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      token: getToken(),
      user:{username:"",nick:"",icon:""},
      roles: [],
      permissions: []
    }),
    actions: {
      // 登录
      login(userInfo:any ) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise((resolve, reject) => {
       
          login(username, password, code, uuid).then(res => {
            if(!res.status)
            {
              reject(res)
            }
            setToken(res.data.token);
            this.token = res.data.token;
            resolve(res);
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 获取用户信息
      getInfo() {
        return new Promise((resolve, reject) => {
          getInfo().then(response => {
            const res=response.data;
            const user = res.user
            // const avatar = (user.avatar == "" || user.avatar == null) ? defAva : import.meta.env.VITE_APP_BASE_API + user.avatar;

            if (res.roleCodes && res.roleCodes.length > 0) { // 验证返回的roles是否是一个非空数组
              this.roles = res.roleCodes
              this.permissions = res.permissionCodes
              // this.roles = ["admin"];
              // this.permissions=["*:*:*"]

            } else {
              this.roles = ["ROLE_DEFAULT"] as never[]
            }
            // this.roles = ["admin"];
            // this.permissions=["*:*:*"]
            this.user.username = user.userName;
            this.user.nick=user.nick
            this.user.icon = user.icon;
            resolve(res)
          }).catch(error => {
            reject(error)
          })



        })
      },
      // 退出系统
      logOut() {
        return new Promise((resolve, reject) => {
          //this.token
          logout().then((response) => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      }
    }
  })

export default useUserStore
