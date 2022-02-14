<template>
  <div class="stars-wrapper">
    <div id='stars'></div>
    <div id='stars2'></div>
    <div id='stars3'></div>
    <div class="home container text-center">
      <img alt="INVIEW logo" src="../assets/logo.png" class="logo">
      <p style="font-size: 18px;" class="m-4 p-4">찾으시는 어쩌구 어쩌구를 입력하세요!</p>
      <div class="search-bar">
        <SearchBar/>
      </div>
    </div>
    <div class="container recent-result">
      <h4>최근 개설 방</h4>
      <div class="row row-cols-1 row-cols-md-3 g-4">
        <div 
          v-for="meeting in meetings.slice(0, 10)" 
          :key="meeting.id" 
          class="col"
        > 
          <MeetingCard :meeting="meeting" />
        </div>
      </div>
    </div>
  </div>
  
</template>

<script lang="ts">
import { defineComponent, computed, onMounted, onUnmounted } from 'vue';
import SearchBar from '@/components/SearchBar.vue';
import MeetingCard from '@/components/MeetingCard.vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'Home',
  components: {
    SearchBar, MeetingCard
  },
  setup() {
    const store = useStore()
    store.dispatch('search', {title: '', industry: '', company: ''})
    const meetings = computed(() => store.state.searchResult)
    onMounted(() => {
      document.getElementById('body')!.setAttribute('style', 'background: linear-gradient(to bottom, rgba(206, 229, 208, 0.3) 0%, rgba(243, 240, 215, 0.4) 20%, rgba(224, 192, 151, 0.3), 80%, rgba(255, 120, 120, 0.3) 100%);')
      document.documentElement.setAttribute('style', 'width: 100%; height: 200%; padding: 0; margin: 0;')
    })
    onUnmounted(() => {
      document.getElementById('body')!.setAttribute('style', 'background: #fff;')
      document.documentElement.setAttribute('style', '')
    })
    return { meetings }
  }
});
</script>

<style scoped lang="scss">
@import "./../style/home.scss";

.home {
  margin: 10vw auto;
}

.logo {
  width: 500px;
}

@media screen and (max-width: 620px) {
  .logo {
    width: 420px;
  }
}

@media screen and (max-width: 480px) {
  .logo {
    width: 300px;
  }
}

.search-bar {
  width: 420px;
  margin: 0 auto;
  transform: scale(1.3);
}

@media screen and (max-width: 480px) {
  .search-bar {
    width: 300px;
    transform: scale(1);
  }
}

@media screen and (max-width: 620px) {
  .search-bar {
    transform: scale(1);
  }
}

.recent-result {
  margin: 10px auto;
  padding: 0 10px;
}

@media screen and (min-width: 1200px) {
  .recent-result {
    max-width: 1000px;
  }
}
</style>
