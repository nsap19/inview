<template>
  <div class="search-filter-bar">
    <el-form 
      size="large"
      label-position="top"
      label-width="auto"
    >
      <div class="container">
        <div class="row justify-content-center pt-3">
          <div class="col-12 col-lg-10 w-100" style="max-width: 1000px;">
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
        </div>
      </div>
    </el-form>
  </div>
</template>

<script>
import { defineComponent, ref, watch } from 'vue'
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

    watch([title, industry, company], ([newTitle, newIndustry, newCompany], [preTitle, preIndustry, preCompany]) => {
      getSearchResult()
    })

    const makeQuery = function (page) {
      let query = {page: page}
      if (title.value && title.value?.toString().trim()) {
        query['title'] = title.value?.toString().trim() 
      }
      if (industry.value && industry.value?.toString().trim()) {
        query['industryList'] = industry.value?.toString().trim()
      }
      if (company.value && company.value?.toString().trim()) {
        query['companyList'] = company.value?.toString().trim()
      }
      return query
    }

    const getSearchResult = () => {
      console.log(makeQuery(1))
      router.push({ name: 'Search', query: makeQuery(1)})
      store.dispatch('search', makeQuery(1))
    }

    return { title, industry, company, getSearchResult }
  }
})
</script>

<style>
.search-filter-bar {
  border-radius: 10px;
}
</style>