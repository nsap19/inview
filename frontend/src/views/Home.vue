<template>
  <div class="home container text-center">
    <img alt="INVIEW logo" src="../assets/logo.png" class="logo">
    <div class="py-4 search-bar">
      <SearchBar/>
    </div>
  </div>
  <div class="container recent-result">
    <h4>최근 개설 방</h4>
    <div class="row row-cols-1 row-cols-md-3 g-4">
      <div 
        v-for="meeting in meetings" 
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
.home {
  margin: 15vw auto;
}

.logo {
  width: 546px;
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
