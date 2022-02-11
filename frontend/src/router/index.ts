import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
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
    component: () => import(/* webpackChunkName: "create" */ '@/views/Meeting.vue')
  },
  {
    path: '/result/:userName/:meetingId',
    name: 'Result',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Result.vue')
  },
  {
    path: "/:pathMatch(.*)*",
    name: '404',
    component: () => import(/* webpackChunkName: "create" */ '@/views/PageNotFound.vue')
  },
  {
    path: '/account',
    name: 'Account',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Mypage.vue')
  },
  {
    path: '/video',
    name: 'Video',
    component: () => import(/* webpackChunkName: "create" */ '@/components/Meeting/Video.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
