import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";
import axios from 'axios'

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
    searchResult: []
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
      console.log('뮤테이션 실행')
      state.user = data;
      console.log(state.user)
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
    saveSearchResult(state, data) {
      state.searchResult = data
    }
  },
  actions: {
    setUser({ commit }, payload) {
      console.log('액션 실행')
      commit('SET_USER', payload)
    },
    logout({ commit }) {
      commit('SET_LOGOUT')
    },
    search( { commit }, payload ) {
      axios({
        url: "http://localhost:8080/meeting/",
        method: 'GET',
        params: {
          title: payload.title,
          industry: payload.industry,
          company: payload.company
        }
      })
      .then(res => {
        commit('saveSearchResult', res.data.data.content)
      })
    }
  },
  modules: {},
})
