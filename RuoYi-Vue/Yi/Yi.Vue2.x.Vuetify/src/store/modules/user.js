import { getPer, setPer, getToken, setToken, getUser, setUser, removeToken } from '../../util/usertoken'
import accountApi from "@/api/accountApi"

//再导入axion请求
const state = { //状态
    per: getPer(),
    token: getToken(),
    user: getUser(),
    dark: false,
    drawer: {
        image: 0,
        gradient: 1,
        mini: false,
    },
    gradients: [
        'rgba(0, 0, 0, .7), rgba(0, 0, 0, .7)',
        'rgba(228, 226, 226, 1), rgba(255, 255, 255, 0.7)',
        'rgba(244, 67, 54, .8), rgba(244, 67, 54, .8)',
    ],
    images: [
        'https://s1.ax1x.com/2022/03/26/qdNnbD.jpg',
        'https://s1.ax1x.com/2022/03/26/qdNMUH.jpg',
        'https://s1.ax1x.com/2022/03/26/qdNKVe.jpg',
        'https://s1.ax1x.com/2022/03/26/qdNmDO.jpg'
    ],
    notifications: [],
    rtl: false
}

const mutations = { //变化//载荷
    SET_PER(state, per) {
        state.per = per
        setPer(per)
    },
    SET_TOKEN(state, token) {
        state.token = token
        setToken(token)
    },
    SET_USER(state, user) {
        state.user = user
        setUser(user)
    },
    SET_NEW_USER(state, userInfo) {
        state.user.user = userInfo
        setUser(state.user)
    },

    SetGradient(state, gradient) {
        state.drawer.gradient = gradient
    },
    SetImage(state, image) {
        state.drawer.image = image
    }
}

//在action中可以配合axios进行权限判断
const actions = { //动作
    SetIcon({ commit, state }, icon) {
        state.user.icon = icon
        commit('SET_USER', state.user)
    },
    // qqUpdate({ state }, openid) {
    //     return new Promise((resolv, reject) => {
    //         qqApi.qqupdate(openid, state.user.id).then(resp => {
    //             resolv(resp)
    //         }).catch(error => {
    //             reject(error)
    //         })
    //     })
    // },

    // qqLogin({ commit }, openid) {
    //     return new Promise((resolv, reject) => {
    //         qqApi.qqlogin(openid).then(resp => {
    //             if (resp.status) {
    //                 commit('SET_TOKEN', resp.data.token)
    //                 commit('SET_USER', resp.data.user)
    //             }
    //             resolv(resp)
    //         }).catch(error => {
    //             reject(error)
    //         })
    //     })
    // },

    Login({ commit }, form) {
        return new Promise((resolv, reject) => {
            accountApi.login(form.username.trim(), form.password.trim()).then(resp => {
                if (resp.status) {
                    commit('SET_TOKEN', resp.data.token)
     

                    accountApi.getUserAllInfo().then(resp2=>{
                        commit('SET_USER', resp2.data)

                        var code=[];
                        resp2.data.menus.forEach(element => {
                            if(element.permissionCode!="")
                            {
                                code.push(element.permissionCode)
                            }
                       
                        });
                        commit('SET_PER', code)

                        resolv(resp)
                    })
                }
                else
                {
                    resolv(resp)
                }
            }).catch(error => {
                reject(error)
            })
        })
    },



    Register({ commit }, form) {
        return new Promise((resolv, reject) => {
            accountApi.register(form.username.trim(), form.password.trim(), form.email.trim(), form.code.trim()).then(resp => {
                resolv(resp)
            }).catch(error => {
                reject(error)
            })
        })
    },
    Logged({ commit }) {
        return new Promise((resolv, reject) => {
            accountApi.logged().then(resp => {
                resolv(resp)
            }).catch(error => {
                reject(error)
            })
        })
    },

    // GetUserInfo({ commit, state }) {
    //     return new Promise((resolv, reject) => {
    //         // getUserInfo(state.token).then(response => {
    //         //     commit('SET_USER', response.data)
    //         // resolve(response)
    //         // }).catch(error=>{
    //         //     reject(error)
    //         // })
    //     })
    // },
    Logout({ commit, state }) {
        return new Promise((resolv, reject) => {
            accountApi.logout().then(response => {
                commit('SET_TOKEN', '')
                commit('SET_USER', null)
                removeToken()
                resolv(response)
            }).catch(error => {
                reject(error)
            })
        })
    }

}

const getters = { //类似与计算属性 派生属性
    dark: (state, getters) => {
        return (
            state.dark ||
            getters.gradient.indexOf('255, 255, 255') === -1
        )
    },
    gradient: state => {
        return state.gradients[state.drawer.gradient]
    },
    image: state => {
        return state.drawer.image === '' ? state.drawer.image : state.images[state.drawer.image]
    }
}



export default { state, mutations, actions, getters }