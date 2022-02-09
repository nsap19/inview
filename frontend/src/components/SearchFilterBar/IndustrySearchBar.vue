<template>
  <el-select v-model="industry" placeholder="직군을 선택해주세요" class="w-100">
    <el-option 
      v-for="industry in industries" 
      :key="industry.id" 
      :label="industry.industryName" 
      :value="industry.industryName"
    ></el-option>
  </el-select>
</template>

<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import axios from 'axios'

export default defineComponent({
  name: 'IndustrySearchBar',
  props: {
    modelValue: String,
  },
  setup(props, { emit }) {
    const industry = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });

    interface IndustryItem {
      industryName: string
      id: number
    }

    const industries = ref<IndustryItem[]>([])

    const loadIndustries = () => {
      axios({
        url: "http://localhost:8080/meeting/industry/",
        method: 'GET',
      })
      .then(res => {
        industries.value = res.data.data
      })
    }

    onMounted(() => {
      loadIndustries()
    })

    return { industry, industries }
  }
})
</script>

<style>

</style>