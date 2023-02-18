const state = {
    basePath: "",
    extendPath:""
}

const mutations = { //变化//载荷
    SetExtendPath(state, extendPath) {
        state.extendPath = extendPath
    },

    SetBasePath(state, basePath) {
        state.basePath = basePath
    }
}

//在action中可以配合axios进行权限判断
const actions = { //动作
    Set_ExtendPath(context,extendPath) {
        context.commit('SetExtendPath',extendPath)
    },
    Set_BasePath(context,basePath) {
        context.commit('SetBasePath',basePath)
    },
   Add_ExtendPath(context,extendPath) {
        context.commit('SetExtendPath',context.state.extendPath+"/"+extendPath)
    },
}
const getters = {
    path: state => {
        return state.basePath+state.extendPath;
    }

}


export default { state, mutations, actions, getters }