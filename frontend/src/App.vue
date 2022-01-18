<template>
  <div id="nav" class="container p-5">
    <div class="row justify-content-between align-items-center">
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

export default defineComponent({
  name: 'App',
  components: {
    Search,
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
  --el-color-primary: #CEE5D0;
  /* --el-bg-color: #F3F0D7; */
}

#body {
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;

}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

:root {
  --el-color-primary: green;
}
</style>
