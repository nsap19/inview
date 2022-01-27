import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";

export default createStore({
  plugins: [createPersistedState({
    // user라는 값만 새로고침해도 유지 할 수 있도록 
    paths:['user']
  })],
  state: {
    modal: {
      login: false,
      register: false,
    },
    user: {},
    options:[],
    location:{},
    fileList: [],
  },
  mutations: {
    SET_LOGIN_MODAL(state, data) {
      state.modal.register = false;
      state.modal.login = data;
    },
    SET_REGISTER_MODAL(state, data) {
      state.modal.login = false;
      state.modal.register = data;
    },
    SET_USER(state, data) {
      state.user = data;
    },
    SET_LOGOUT(state) {
      state.user = {};
      localStorage.removeItem("token");
    },
    SET_OPTION(state, data){
      state.options = data;
    },
    SET_FILE_LIST(state, data) {
      state.fileList = data;
    },
    SET_LOCATION(state, data){
      state.location = data;
    },

  },
  actions: {},
  modules: {},
})
