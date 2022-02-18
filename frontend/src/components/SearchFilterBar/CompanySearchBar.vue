<template>
  <el-autocomplete
    v-model="company"
    :fetch-suggestions="querySearch"
    :trigger-on-focus="false"
    class="inline-input w-100"
    placeholder="회사명을 입력해주세요"
    @select="handleSelect"
  />

</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
import axios from 'axios'

export default defineComponent({
  name: 'CompanySearchBar',
  props: {
    modelValue: String,
  },
  setup(props, { emit }) {
    const company = computed({
      get: () => props.modelValue || '',
      set: (value) => emit("update:modelValue", value),
    });

    interface CompanyItem {
      value: string
      id: number
    }
    const state = ref('')

    const companies = ref<CompanyItem[]>([])

    // eslint-disable-next-line
    const querySearch = (queryString: string, cb: any) => {
      const results = queryString
        ? companies.value.filter(createFilter(queryString))
        : companies.value
      cb(results)
    }

    const createFilter = (queryString: string) => {
      return (company: CompanyItem) => {
        return (
          company.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
      }
    }

    // DB에 저장된전체 회사 목록 조회
    const loadAll = () => {
      axios({
        url: "/meeting/company/",
        method: 'GET',
      })
      .then(res => {
        res.data.data.forEach((element: {companyName: string, id: number}) => {
          companies.value.push({
            value: element.companyName,
            id: element.id
          })
        });
      })
    }

    const handleSelect = (item: CompanyItem) => {
      company.value = item.value
    }

    onMounted(() => {
      loadAll()
    })

    return { company, state, querySearch, handleSelect }
  },
  
})
</script>

<style scoped>

</style>