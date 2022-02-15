<template>
  <!-- <div class="stars-wrapper">
    <div id='stars'></div>
    <div id='stars2'></div>
    <div id='stars3'></div> -->
    <div class="home text-center">
      <div class="logo-wrapper">
        <img alt="INVIEW logo" src="../assets/logo.png" class="logo">
      </div>
      <!-- <p style="font-size: 18px;" class="m-4 p-4">찾으시는 어쩌구 어쩌구를 입력하세요!</p> -->
      <div class="search-bar">
        <SearchBar style="box-shadow: 0px 8px 20px rgb(0 0 0 / 6%);" />
      </div>
    </div>
    <div class="container recent-result">
      <h4>최근 최근 개설 방</h4>
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
  <!-- </div> -->
  
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
      // document.getElementById('body')!.setAttribute('style', 'background: linear-gradient(to bottom, rgba(206, 229, 208, 0.3) 0%, rgba(243, 240, 215, 0.4) 20%, rgba(224, 192, 151, 0.3), 80%, rgba(255, 120, 120, 0.3) 100%);')
      // document.documentElement.setAttribute('style', 'width: 100%; height: 100%; padding: 0; margin: 0;')
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

.logo-wrapper {
  background-color: #F9F9F9;
  padding-top: 7rem;
  padding-bottom: 4rem;
}

.logo {
  width: 420px;
}

@media screen and (min-width: 620px) {
  .logo {
    width: 500px;
  }
}

@media screen and (max-width: 480px) {
  .logo {
    width: 300px;
  }
  .home {
    padding-top: 0rem;
  }
  .logo-wrapper {
    padding-top: 0rem;
  }
}

.search-bar {
  width: 420px;
  margin: 20px auto;
  transform: scale(1) translateY(-110%);
}

@media screen and (max-width: 480px) {
  .search-bar {
    width: 300px;
    transform: scale(1) translateY(-100%);
  }
}

@media screen and (min-width: 620px) {
  .search-bar {
    transform: scale(1.3) translateY(-80%);
  }
}

.recent-result {
  margin-top: 10rem;
  margin-bottom: 10rem;
  padding: 0 10px;
  min-height: 500px;
}

@media screen and (min-width: 1200px) {
  .recent-result {
    max-width: 1000px;
  }
}
</style>
