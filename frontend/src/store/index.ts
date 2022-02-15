import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";
import axios from 'axios'

export default createStore({
  plugins: [createPersistedState({
    // user라는 값만 새로고침해도 유지 할 수 있도록 
    paths:['user', 'meeting', 'participants']
  })],
  state: {
    user: {},
    searchResult: [],
    meeting: {hostId: Number},
    participants: []
  },
  mutations: {
    SET_USER(state, data) {
      state.user = data;
    },
    SET_LOGOUT(state) {
      state.user = {};
      localStorage.removeItem("token");
    },
    SAVE_SEARCH_RESULT(state, data) {
      state.searchResult = data
    },
    ADD_SEARCH_RESULT(state, data) {
      state.searchResult = state.searchResult.concat(data)
    },
    SET_MEETING(state, data) {
      state.meeting = data
    },
    DELETE_MEETING(state) {
      state.meeting = {hostId: Number}
      state.participants = []
    },
    SET_PARTICIPANTS(state, data) {
      state.participants = data
      console.log(data)
    },
    SET_NEW_HOST(state, data) {
      state.meeting.hostId = data
    },
  },
  actions: {
    setUser({ commit }, payload) {
      commit('SET_USER', payload)
    },
    logout({ commit }) {
      commit('SET_LOGOUT')
    },
    search( { commit }, query ) {
      axios({
        url: "/meeting/",
        method: 'GET',
        params: {
          title: query.title,
          industry: query.industry,
          company: query.company,
          page: query.page
        }
      }).then(res => {
          if (1 < query.page) {
            commit('ADD_SEARCH_RESULT', res.data.data.content)
          } else {
            commit('SAVE_SEARCH_RESULT', res.data.data.content)
          }
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
            title: res.data.data.title,
            hostId: res.data.data.hostId
          })
        })
    },
    deleteMeeting ({ commit }) {
      commit('DELETE_MEETING')
    },
    setParticipants({ commit }, participants) {
      commit('SET_PARTICIPANTS', participants)
    },
    setNewHost({ commit }, hostId) {
      commit('SET_NEW_HOST', hostId)
    }
  },
  modules: {},
})
