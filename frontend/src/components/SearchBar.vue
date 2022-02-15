<template>
  <el-input
    v-model="searchQuery"
    placeholder="검색어를 입력하세요"
    size="large"
    @keyup.enter="goToSearchResult"
    style="max-width: 400px;"
  >
    <template #prepend>
      <el-select v-model="selectedCategory" placeholder="제목" style="width: 80px">
        <el-option label="제목" value="title"></el-option>
        <el-option label="직군" value="industry"></el-option>
        <el-option label="회사" value="company"></el-option>
      </el-select>
    </template>
    <template #append>
      <el-button :icon="Search" @click="goToSearchResult"></el-button>
    </template>
  </el-input>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'Search',
  setup () {
    const searchQuery = ref('')
    const selectedCategory = ref('title')
    const router = useRouter()
    const store = useStore()

    const goToSearchResult = () => {
      // 검색어에 글자가 있는 경우 검색 결과 페이지로 이동
      if (searchQuery.value.trim()) {
        const wholeQuery = {
          title: selectedCategory.value === 'title' ? searchQuery.value : '',
          industry: selectedCategory.value === 'industry' ? searchQuery.value : '',
          company: selectedCategory.value === 'company' ? searchQuery.value : ''
        }
        router.push({ name: 'Search', query: wholeQuery})
        store.dispatch('search', wholeQuery)
      }
    }

    return {
      searchQuery, selectedCategory, goToSearchResult, Search
    }
  },
  
})
</script>

<style scoped>
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css");

.el-input__inner{
  border: none;
}
</style>