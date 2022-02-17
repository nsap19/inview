<template>
  <div class="home text-center">
    <div class="logo-wrapper">
      <img alt="INVIEW logo" src="../assets/logo.png" class="logo">
    </div>
    <div class="search-bar">
      <SearchBar style="box-shadow: 0px 8px 20px rgb(0 0 0 / 6%);" />
    </div>
  </div>
  <div class="container recent-result">
    <p class="fs-4">최근 만들어진 방</p>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
      <div 
        v-for="meeting in meetings.slice(0, 10)" 
        :key="meeting.id" 
        class="col"
      > 
        <MeetingCard :meeting="meeting" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';
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

    return { meetings }
  }
});
</script>

<style scoped>
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
