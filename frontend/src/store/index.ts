import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";
import axios from 'axios'

export default createStore({
  plugins: [createPersistedState({
    // user라는 값만 새로고침해도 유지 할 수 있도록 
    paths:['user', 'meeting']
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
    searchResult: [],
    meeting: {}
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
    SAVE_SEARCH_RESULT(state, data) {
      state.searchResult = data
    },
    SET_MEETING(state, data) {
      console.log('뮤테이션 실행', data)
      state.meeting = data
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
        url: "/meeting/",
        method: 'GET',
        params: {
          title: payload.title,
          industry: payload.industry,
          company: payload.company
        }
      }).then(res => {
          commit('SAVE_SEARCH_RESULT', res.data.data.content)
        }).catch(err => {
          console.log(err)
        })
    },
    setMeeting ( { commit }, meetingId ) {
      axios({
        url: `/meeting/${meetingId}`,
        method: 'GET',
      }).then(res => {
          commit('SET_MEETING', { 
            id: meetingId,
            participantNicknameList: res.data.data.participantNicknameList,
            startTime: res.data.data.startTime,
            title: res.data.data.title
          })
        })
    },
  },
  modules: {},
})
