import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    username: "yzq",
    age: 18,
  },
  mutations: {
    getUsername(state) {
      return state.username;
    },
    increaseUserAge(state,n) {
      state.age+=n;
    },
  },
  actions: {},
  modules: {},
});
