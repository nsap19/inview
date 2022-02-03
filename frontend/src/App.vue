<template>
  <div id="nav" class="container p-5" v-if="this.$route.path !== '/' && !this.$route.path.startsWith('/meeting')">
    <Nav-menu/>
    <NavBar/>
    <div class="row justify-content-between align-items-center">
      <!-- 메인 페이지에서 네비바가 어떻게 보일지 미정이라 :style 삭제 안함 -->
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="col-3 p-3">
        <router-link to="/">
          <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100">
        </router-link>
      </div>
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="col">
        <Search/>
      </div>
      <div class="col-3">
        <router-link to="/create">생성</router-link>
        <router-link to="/about">About</router-link>
      </div>
    </div>
  </div>
  <router-view/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Search from '@/components/SearchBar.vue';
import NavMenu from "./components/NavMenu.vue"
import NavBar from "./components/NavBar.vue"

export default defineComponent({
  name: 'App',
  components: {
    Search, NavMenu , NavBar
  },
})
</script>

<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');

#app {
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
  --el-color-primary: #76B57A;
  /* --el-bg-color: #F3F0D7; */
}

#body {
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  background: linear-gradient(-45deg, #cee5d0, #f3f0d7, #e0c097, #ff7878);
  background-size: 400% 400%;
  animation: gradient 15s ease infinite;
}

@keyframes gradient {
    0% {
        background-position: 0% 50%;
    }
    50% {
        background-position: 100% 50%;
    }
    100% {
        background-position: 0% 50%;
    }
}

#nav a.router-link-exact-active {
  color: #42b983;
}

*::-webkit-scrollbar {
    width: 16px;
}

*::-webkit-scrollbar-track {
    border-radius: 8px;
}

*::-webkit-scrollbar-thumb {
    height: 56px;
    border-radius: 8px;
    border: 4px solid transparent;
    background-clip: content-box;
    background-color: #888;
}

*::-webkit-scrollbar-thumb:hover {
    background-color: #555;
}
</style>
