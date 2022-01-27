<template>
  <div class="container w-50">
    <h1>미팅 생성</h1>
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      class="demo-ruleForm"
      :size="formSize"
    >
      <el-form-item label="제목" required prop="title">
        <el-input v-model="ruleForm.title" placeholder="제목을 입력해주세요"></el-input>
      </el-form-item>
      <el-form-item label="직군" required prop="industry">
        <el-select v-model="ruleForm.industry" placeholder="직군을 선택해주세요" class="w-100">
          <el-option label="Zone one" value="shanghai"></el-option>
          <el-option label="Zone two" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="회사">
        <CompanySearchBar/>
      </el-form-item>
      <el-form-item label="참가 인원">
        <el-select v-model="ruleForm.userLimit" placeholder="최대 참가 인원을 선택해주세요" class="w-100">
          <el-option label="Zone one" value="shanghai"></el-option>
          <el-option label="Zone two" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="시작 시간">
        <el-col :span="11">
          <el-date-picker
            v-model="ruleForm.date1"
            type="date"
            placeholder="날짜"
            style="width: 100%"
          ></el-date-picker>
        </el-col>
        <el-col :span="2" class="text-center">
          <span class="text-gray-500">-</span>
        </el-col>
        <el-col :span="11">
          <el-time-picker
            v-model="ruleForm.date2"
            placeholder="시간"
            format="HH:mm"
            style="width: 100%"
          ></el-time-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="비밀번호">
        <el-input 
          v-model="ruleForm.password" 
          placeholder="비밀번호 입력해주세요" 
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">생성</el-button>
        <el-button>취소</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import CompanySearchBar from '@/components/CompanySearchBar.vue';
import type { ElForm } from 'element-plus'

export default defineComponent({
  name: 'CreateMeeting',
  components: {
    CompanySearchBar,
  },
  setup() {
    const formSize = ref('')
    const ruleFormRef = ref<InstanceType<typeof ElForm>>()

    const ruleForm = reactive({
      title: '',
      industry: '',
      company: '',
      userLimit: '',
      date1: '',
      date2: '',
      password: ''
    })

    const onSubmit = () => {
      console.log('submit!')
    }
    const rules = {
      title: [
        {
          required: true,
          message: '제목을 입력해주세요',
          trigger: 'blur',
        },
        {
          min: 1,
          max: 20,
          message: 'Length should be 1 to 20',
          trigger: 'blur',
        },
      ],
      industry: [
        {
          required: true,
          message: '직군을 선택해주세요',
          trigger: 'blur',
        },
      ],
      date1: [
        {
          type: 'date',
          required: true,
          message: 'Please pick a date',
          trigger: 'change',
        },
      ],
      date2: [
        {
          type: 'date',
          required: true,
          message: 'Please pick a time',
          trigger: 'change',
        },
      ],
    }

    return { formSize, ruleFormRef, ruleForm, rules, onSubmit }
  }
})
</script>

<style>

</style>