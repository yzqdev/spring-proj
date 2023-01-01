interface ICommonState {
  config: any;
  currentStorageStrategy: any;
  currentDirectory: any;
  imgMode: boolean;
  newImgMode: boolean;
  oldDriveId: any;
}

const state: ICommonState = {
  config: {},
  currentStorageStrategy: null,
  currentDirectory: null,
  imgMode: false,
  newImgMode: false,
  oldDriveId: null,
};

const mutations = {
  updateOldDriveId(state: ICommonState, v) {
    state.oldDriveId = v;
  },
  updateNewImgMode(state: ICommonState, v) {
    state.newImgMode = v;
  },
  updateConfig(state: ICommonState, v) {
    state.config = v;
  },
  currentDirectory(state: ICommonState, v) {
    state.currentDirectory = v;
  },
  hoverRow(state: ICommonState, v) {
    state.hoverRow = v;
  },
  rightClickRow(state: ICommonState, v) {
    state.rightClickRow = v;
  },
  updateSearchParam(state: ICommonState, v) {
    state.searchParam = v;
  },
  updateCurrentStorageStrategy(state: ICommonState, v) {
    state.currentStorageStrategy = v;
  },
  switchImgMode(state: ICommonState, v) {
    state.imgMode = v;
  },
};

const getters = {
  directLinkPrefix: (state: ICommonState) => {
    return state.config && state.config.directLinkPrefix;
  },
  oldDriveId: (state: ICommonState) => {
    return state.oldDriveId;
  },
  newImgMode: (state: ICommonState) => {
    return state.newImgMode === true;
  },
  defaultSwitchToImgMode: (state: ICommonState) => {
    return state.config && state.config.defaultSwitchToImgMode;
  },
  imgMode: (state: ICommonState) => {
    return state.imgMode;
  },
  infoEnable: (state: ICommonState) => {
    return state.config && state.config.infoEnable;
  },
  showLinkBtn: (state: ICommonState) => {
    return state.config && state.config.showLinkBtn;
  },
  showShortLink: (state: ICommonState) => {
    return state.config && state.config.showShortLink;
  },
  showPathLink: (state: ICommonState) => {
    return state.config && state.config.showPathLink;
  },
  tableSize: (state: ICommonState) => {
    return state.config && state.config.tableSize;
  },
  currentStorageStrategyType: (state: ICommonState) => {
    return state.currentStorageStrategy.type.key;
  },
  currentStorageStrategyId: (state: ICommonState) => {
    return state.currentStorageStrategy.id;
  },
  showOperator: (state: ICommonState) => {
    return state.config && state.config.showOperator;
  },
  showDocument: (state: ICommonState) => {
    return state.config && state.config.showDocument;
  },
  debugMode: (state: ICommonState) => {
    return state.config && state.config.debugMode;
  },
  siteName: (state: ICommonState) => {
    return state.config && state.config.siteName;
  },
  domain: (state: ICommonState) => {
    return state.config && state.config.domain;
  },
  announcement: (state: ICommonState) => {
    return state.config && state.config.announcement;
  },
  layout: (state: ICommonState) => {
    return state.config && state.config.layout;
  },
  showAnnouncement: (state: ICommonState) => {
    return state.config && state.config.showAnnouncement;
  },
  searchEnable: (state: ICommonState) => {
    return state.config && state.config.searchEnable;
  },
};

const actions = {};

export default {
  namespaced: true,
  state,

  getters,
  mutations,
  actions,
};
