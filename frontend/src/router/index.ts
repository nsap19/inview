import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'about',
    component: () => import(/* webpackChunkName: "home" */ '../views/About.vue')
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import(/* webpackChunkName: "search" */ '@/views/SearchResult.vue')
  },
  {
    path: '/create',
    name: 'Create',
    component: () => import(/* webpackChunkName: "create" */ '@/views/CreateMeeting.vue')
  },
  {
    path: '/meeting',
    name: 'Meeting',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Meeting.vue')
  },
  {
    path: '/result/:userName/:meetingId',
    name: 'Result',
    component: () => import(/* webpackChunkName: "create" */ '@/views/Result.vue')
  },
  // {
  //   path: '/chat',
  //   name: 'Chat',
  //   component: () => import(/* webpackChunkName: "create" */ '@/views/Chat.vue')
  // },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
