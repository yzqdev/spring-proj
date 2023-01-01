import common from "../../common";
interface IState {
  tableData: any[];
  currentDirectory: any;
  hoverRow: any;
  rightClickRow: any;
  searchParam: string;
}
const state: IState = {
  tableData: [],
  currentDirectory: {},
  hoverRow: {},
  rightClickRow: {},
  searchParam: "",
};

const mutations = {
  tableData(state: IState, v) {
    state.tableData = v;
  },
  appendTableData(state: IState, v) {
    if (v) {
      state.tableData = state.tableData.concat(v);
    }
  },
};

const getters = {
  tableData: (state: IState) => {
    let tableData = state.tableData;
    tableData.forEach((item) => {
      if (!item.icon) {
        item["icon"] = common.getFileIconName(item);
      }
    });
    return tableData;
  },
  filterFileByType: (state: IState) => (type: string) => {
    return state.tableData.filter(function (item: any) {
      if (item.type === "BACK") {
        return false;
      }
      let name = item.name;
      let suffix = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
      return common.constant.fileTypeMap[type].indexOf(suffix) !== -1;
    });
  },
};

const actions = {};

export default { namespaced: true, state, mutations, getters, actions };
