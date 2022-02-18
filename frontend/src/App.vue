<template>
  <NavBar/>
  <router-view/>
  <Footer/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import NavBar from "./components/NavBar/NavBar.vue"
import Footer from "./components/Footer.vue"

export default defineComponent({
  name: 'App',
  components: {
    NavBar, Footer,
  },
  setup() {
  }
})
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito&display=swap');

@font-face {
  font-family: 'TmoneyRoundWindRegular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/TmoneyRoundWindRegular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
  unicode-range: U+AC00-U+D7A3;
}

#app {
  font-family: 'Nunito', TmoneyRoundWindRegular, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

#body {
  overflow: overlay;
  overflow-x: hidden;
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
    visibility: hidden;
}

*::-webkit-scrollbar:hover {
    visibility: visible; 
}

*::-webkit-scrollbar-track {
    border-radius: 8px;
    background-color: rgba(0,0,0,0);
}

*::-webkit-scrollbar-thumb {
    height: 30px;
    border-radius: 8px;
    border: 4px solid transparent;
    background-clip: content-box;
    background-color: #888;
}

*::-webkit-scrollbar-thumb:hover {
    background-color: #555;
}
</style>
