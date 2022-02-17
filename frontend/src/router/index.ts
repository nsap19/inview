import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Landing from '../views/Landing.vue'
import Home from '../views/Home.vue'
import axios from "axios";

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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
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
  // {
  //   path: '/result/:userName/:meetingId',
  //   name: 'Result',
  //   component: () => import(/* webpackChunkName: "create" */ '@/views/Result.vue')
  // },
  // {
  //   path: "/:pathMatch(.*)*",
  //   name: '404',
  //   component: () => import(/* webpackChunkName: "create" */ '@/views/PageNotFound.vue')
  // },
  {
    path: "/:pathMatch(.*)*",
    name: '404',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Page404.vue')
  },
  // {
  //   path: '/account',
  //   name: 'Account',
  //   component: () => import(/* webpackChunkName: "create" */ '@/components/NavBar/UpdateUser.vue')
  // },
  // {
  //   path: '/video',
  //   name: 'Video',
  //   component: () => import(/* webpackChunkName: "create" */ '@/components/Meeting/Video.vue')
  // },
  // {
  //   path: "/mypage",
  //   name: 'mypage',
  //   component: () => import(/* webpackChunkName: "create" */ '@/views/Mypage.vue')
  // },
  {
    path: "/myaccount",
    name: 'MyAccount',
    component: () => import(/* webpackChunkName: "create" */ '@/views/MyAccount.vue')
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
      console.log(res);
    })
    .catch((err) => {
      console.log(err.response);
      router.push({ name: 'Home' })
      return false;
    });
};

export default router
