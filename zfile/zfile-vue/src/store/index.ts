import { createStore } from "vuex";
import file from "./modules/file";
import common from "./modules/common";

const store = createStore({
  modules: {
    file,
    common,
  },
});

export default store;
