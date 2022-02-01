<template>
  <el-card>
    <el-form 
      class="demo-ruleForm"
      size="large"
      label-position="top"
      label-width="auto"
    >
      <div class="container">
        <div class="row justify-content-center pt-3">
          <div class="col-12 col-lg-10">
            <div class="row row-cols-1 row-cols-md-3">
              <el-form-item label="제목" class="col">
                <el-input v-model="title" placeholder="제목을 입력해주세요"></el-input>
              </el-form-item>
              <el-form-item label="직군" class="col">
                <IndustrySearchBar v-model="industry" />
              </el-form-item>
              <el-form-item label="회사" class="col">
                <CompanySearchBar v-model="company" />
              </el-form-item>
            </div>
          </div>
          <div class="col-12 col-lg-2">
            <div class="text-center">
              <div class="d-none d-lg-block" style="height: 22px; margin-bottom: 12px;"></div>
              <el-button round @click="getSearchResult">상세 검색</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-form>
  </el-card>
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