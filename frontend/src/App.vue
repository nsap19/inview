<template>
  <NavBar/>
  <router-view/>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import NavBar from "./components/NavBar/NavBar.vue"

export default defineComponent({
  name: 'App',
  components: {
    NavBar
  },
  setup() {
    const openCreateMeetingDialog = ref(false)

    return { openCreateMeetingDialog }
  }
})
</script>

<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');

#app {
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  --el-color-primary: #76B57A;
  /* --el-bg-color: #F3F0D7; */
}

#body {
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  /* background: linear-gradient(-45deg, #cee5d0, #f3f0d7, #e0c097, #ff7878);
  background-size: 400% 400%;
  animation: gradient 15s ease infinite; */
  padding: 0 10px;
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
