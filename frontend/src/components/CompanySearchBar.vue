<template>
  <el-autocomplete
    v-model="state"
    :fetch-suggestions="querySearch"
    :trigger-on-focus="false"
    class="inline-input w-100"
    placeholder="회사명을 입력해주세요"
    @select="handleSelect"
    @keyup="handleInput"
  />

</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'

export default defineComponent({
  name: 'CompanySearchBar',
  props: ['modelValue'],
  setup (props, { emit }) {
    interface CompanyItem {
      value: string
      link: string
    }
    const state = ref('')

    const companies = ref<CompanyItem[]>([])

    // eslint-disable-next-line
    const querySearch = (queryString: string, cb: any) => {
      const results = queryString
        ? companies.value.filter(createFilter(queryString))
        : companies.value
      // call callback function to return suggestions
      cb(results)
    }

    const createFilter = (queryString: string) => {
      return (company: CompanyItem) => {
        return (
          company.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
      }
    }

    const loadAll = () => {
      return [
        { value: 'vue1', link: 'https://github.com/vuejs/vue' },
        { value: 'element', link: 'https://github.com/ElemeFE/element' },
        { value: 'cooking', link: 'https://github.com/ElemeFE/cooking' },
        { value: 'mint-ui', link: 'https://github.com/ElemeFE/mint-ui' },
        { value: 'vuex', link: 'https://github.com/vuejs/vuex' },
        { value: 'vue-router', link: 'https://github.com/vuejs/vue-router' },
        { value: '네이버', link: 'https://github.com/babel/babel' },
        { value: '삼성', link: 'https://github.com/babel/babel' },
        { value: 'SK', link: 'https://github.com/babel/babel' },
      ]
    }

    const handleSelect = (item: CompanyItem) => {
      state.value = item.value
      handleInput()
    }

    const handleInput = () => {
      if (state.value.trim()) {
        emit('update:modelValue', state.value)
      }
    }

    onMounted(() => {
      companies.value = loadAll()
    })

    return { state, querySearch, handleSelect, handleInput }
  },
  
})
</script>

<style scoped>

</style>