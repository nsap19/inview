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
          <el-select v-model="industry" placeholder="직군을 선택해주세요" class="w-100">
            <el-option label="Zone one" value="shanghai"></el-option>
            <el-option label="Zone two" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="회사">
          <CompanySearchBar v-model="company" />
        </el-form-item>
        <el-form-item>
          <el-button @click="getSearchResult">검색하기</el-button>
        </el-form-item>
      </el-form>
      <!-- <div>
        <div class="d-flex flex-row justify-content-center">
          <span>제목</span>
          <el-input v-model="title" class="w-50" placeholder="제목을 입력해주세요" />
        </div>
      </div>
      <div>
        <div class="d-flex flex-row justify-content-center">
          <span>직군</span>
          <el-input v-model="industry" class="w-50" placeholder="직군을 입력해주세요" />
        </div>
      </div>
      <div>
        <div class="d-flex flex-row justify-content-center">
          <span>회사</span>
          <el-input v-model="company" class="w-50" placeholder="회사를 입력해주세요" />
        </div>
      </div> -->
      
    </el-collapse-item>
  </el-collapse>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CompanySearchBar from '@/components/CompanySearchBar.vue';

export default defineComponent({
  name: 'SearchFilterBar',
  components: {
    CompanySearchBar,
  },
  setup () {
    const route = useRoute()
    const router = useRouter()

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
      } 
    }

    return { title, industry, company, getSearchResult }
  }
})
</script>

<style>

</style>