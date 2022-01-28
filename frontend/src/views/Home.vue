<template>
  <div class="home container text-center">
    <img alt="INVIEW logo" src="../assets/logo.png" class="w-50 m-4">
    <div class="py-4 search-bar">
      <SearchBar/>
    </div>
  </div>
  
  <div>
    <h1>최근 개설 방</h1>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import SearchBar from '@/components/SearchBar.vue';

export default defineComponent({
  name: 'Home',
  components: {
    SearchBar,
  },
});
</script>

<style scoped>
.search-bar {
  width: 400px;
  margin: 0 auto;
  transform: scale(1.3);
}
</style>