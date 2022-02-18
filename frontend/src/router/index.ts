import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Landing from '../views/Landing.vue'
import Home from '../views/Home.vue'
import axios from "axios";
import store from '@/store';
import { ElMessage } from 'element-plus'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Landing',
    component: Landing
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import(/* webpackChunkName: "search" */ '@/views/SearchResult.vue')
  },
  {
    path: '/meeting/:meetingUrl',
    name: 'Meeting',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Meeting.vue'),
    beforeEnter: function (to, from, next) {
      try {
        joinMeeting(to.params.meetingUrl);
      } finally{
        next();
      }
    }
  },
  {
    path: "/:pathMatch(.*)*",
    name: '404',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Page404.vue')
  },
  {
    path: "/myaccount",
    name: 'MyAccount',
    component: () => import(/* webpackChunkName: "create" */ '@/views/MyAccount.vue'),
    beforeEnter: function (to, from, next) {
      if (Object.keys(store.state.user).length === 0) {
        next({ name: 'Home' }) 
        ElMessage({
          message: '로그인이 필요합니다.',
          type: 'warning',
        })
      }
      else next()
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

const joinMeeting = function (meetingUrl: unknown) {
  axios({
    url: `/meeting/${meetingUrl}/verify`,
    method: "GET",
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
  })
    .then((res) => {
    })
    .catch((err) => {
      router.push({ name: 'Home' })
      return false;
    });
};

export default router
