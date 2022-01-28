<template>
  <el-collapse>
    <el-collapse-item>
      <template #title class="text-center">
        상세검색
      </template>
      <el-form 
        label-width="120px"
        class="demo-ruleForm"
      >
        <el-form-item label="제목">
          <el-input v-model="title" placeholder="제목을 입력해주세요"></el-input>
        </el-form-item>
        <el-form-item label="직군">
          <IndustrySearchBar v-model="industry" />
        </el-form-item>
        <el-form-item label="회사">
          <CompanySearchBar v-model="company" />
        </el-form-item>
        <el-form-item>
          <el-button @click="getSearchResult">검색하기</el-button>
        </el-form-item>
      </el-form>
    </el-collapse-item>
  </el-collapse>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CompanySearchBar from '@/components/SearchFilterBar/CompanySearchBar.vue';
import IndustrySearchBar from '@/components/SearchFilterBar/IndustrySearchBar.vue';
import { useStore } from 'vuex'

export default defineComponent({
  name: 'SearchFilterBar',
  components: {
    CompanySearchBar,
    IndustrySearchBar
  },
  setup () {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()

    const title = ref(route.query.title)
    const industry = ref(route.query.industry)
    const company = ref(route.query.company)

    const getSearchResult = () => {
      // 검색어에 글자가 있는 경우 검색 결과 갱신
      if (title.value?.toString().trim() 
          || industry.value?.toString().trim() 
          || company.value?.toString().trim()) {

        const wholeQuery = {
          title: title.value,
          industry: industry.value,
          company: company.value
        }
        router.push({ name: 'Search', query: wholeQuery})
        store.dispatch('search', {
          title: route.query.title,
          industry: route.query.industry,
          company: route.query.company
        })
      } 
    }

    return { title, industry, company, getSearchResult }
  }
})
</script>

<style>

</style>