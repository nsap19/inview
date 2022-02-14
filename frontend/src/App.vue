<template>
  <NavBar/>
  <router-view/>
  <Footer/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavBar from "./components/NavBar/NavBar.vue"
import Footer from "./components/Footer.vue"
import { useRouter } from "vue-router"
import { useStore } from "vuex"

export default defineComponent({
  name: 'App',
  components: {
    NavBar, Footer,
  },
  setup() {
    const router = useRouter()
    const store = useStore()
    if (router.currentRoute.value.name !== "Meeting") {
      // store.dispatch('deleteMeeting')
      // store.dispatch('setParticipants', [])
    }

  }
})
</script>

<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');
@import url('https://fonts.googleapis.com/css2?family=Nunito&display=swap');

@font-face {
  font-family: 'TmoneyRoundWindRegular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/TmoneyRoundWindRegular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
  unicode-range: U+AC00-U+D7A3;
}

@font-face {
  font-family: 'TmoneyRoundWindExtraBold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/TmoneyRoundWindExtraBold.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

#app {
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  /* font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif; */
  font-family: 'Nunito', TmoneyRoundWindRegular, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

#body {
  /* background: linear-gradient(to bottom, rgba(206, 229, 208, 0.5) 0%, rgba(243, 240, 215, 0.5) 20%, rgba(224, 192, 151, 0.5), 80%, rgba(255, 120, 120, 0.5) 100%); */
  /* font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif; */
  /* background: linear-gradient(-45deg, #cee5d0, #f3f0d7, #e0c097, #ff7878);
  background-size: 400% 400%;
  animation: gradient 15s ease infinite; */
  /* padding: 0 10px; */
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
