<template>
  <div class="container w-50">
    <h1>미팅 생성</h1>
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      class="demo-ruleForm"
      size="large"
    >
      <el-form-item label="제목" prop="title">
        <el-input v-model="ruleForm.title" placeholder="제목을 입력해주세요"></el-input>
      </el-form-item>
      <el-form-item label="직군" prop="industry">
        <IndustrySearchBar v-model="ruleForm.industry" />
      </el-form-item>
      <el-form-item label="회사">
        <CompanySearchBar v-model="ruleForm.company" />
      </el-form-item>
      <el-form-item label="참가 인원">
        <el-radio-group v-model="ruleForm.userLimit" size="large">
          <el-radio-button label="1"></el-radio-button>
          <el-radio-button label="2"></el-radio-button>
          <el-radio-button label="3"></el-radio-button>
          <el-radio-button label="4"></el-radio-button>
          <el-radio-button label="5"></el-radio-button>
          <el-radio-button label="6"></el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="시작 시간" prop="startTime">
        <el-col :span="11">
          <el-date-picker
            v-model="ruleForm.startTime"
            type="datetime"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            placeholder="날짜"
            style="width: 100%"
          ></el-date-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="종료 시간" prop="endTime">
        <el-col :span="11">
          <el-date-picker
            v-model="ruleForm.endTime"
            type="datetime"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm"
            placeholder="날짜"
            style="width: 100%"
          ></el-date-picker>
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
        <el-button type="primary" @click="submitForm(ruleFormRef)">생성</el-button>
        <el-button>취소</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import CompanySearchBar from '@/components/SearchFilterBar/CompanySearchBar.vue'
import IndustrySearchBar from '@/components/SearchFilterBar/IndustrySearchBar.vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'

export default defineComponent({
  name: 'CreateMeeting',
  components: {
    CompanySearchBar, IndustrySearchBar
  },
  setup() {
    const ruleFormRef = ref<InstanceType<typeof ElForm>>()

    const ruleForm = reactive({
      title: '',
      industry: '',
      company: '',
      userLimit: '',
      startTime: '',
      endTime: '',
      password: ''
    })

    // eslint-disable-next-line
    const validStartTime = (rule: any, value: any, callback: any) => {
      const datetime_object = new Date()
      const current_datetime = datetime_object.getFullYear() + "-"
                              + ("0" + (1 + datetime_object.getMonth())).slice(-2) + "-" 
                              + ("0" + datetime_object.getDate()).slice(-2) + " " 
                              + datetime_object.getHours() + ":" + datetime_object.getUTCMinutes()
      if (ruleForm.startTime === '') {
        callback()
      } else if (ruleForm.startTime < current_datetime) {
        callback(new Error('시작 시간은 현재 시간 이후로 설정해주세요'))
      } else {
        callback()
      }
    }

    // eslint-disable-next-line
    const validEndTime = (rule: any, value: any, callback: any) => {
      const datetime_object = new Date()
      const current_datetime = datetime_object.getFullYear() + "-"
                              + ("0" + (1 + datetime_object.getMonth())).slice(-2) + "-" 
                              + ("0" + datetime_object.getDate()).slice(-2) + " " 
                              + datetime_object.getHours() + ":" + datetime_object.getUTCMinutes()
      if (ruleForm.endTime === '') {
        callback()
      } else if (ruleForm.startTime && ruleForm.endTime < ruleForm.startTime) {
        callback(new Error('종료 시간은 시작 시간 이후로 설정해주세요'))
      } else if (ruleForm.endTime < current_datetime) {
        callback(new Error('종료 시간은 현재 시간 이후로 설정해주세요'))
      } else {
        callback()
      }
    }

    const rules = reactive({
      title: [
        {
          required: true,
          message: '제목을 입력해주세요',
          trigger: 'blur',
        },
        {
          min: 1,
          max: 100,
          message: '제목은 1자 이상 100자 이하 입력해주세요',
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
      startTime: [
        { 
          validator: validStartTime,
          trigger: "blur",
        }
      ],
      endTime: [
        { 
          validator: validEndTime,
          trigger: "blur",
        }
      ],
    })

    const submitForm = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          // console.log('submit!')
          axios.post('/meeting', ruleForm).then(res => {
            console.log(res.data)
          }).catch(err => {
            console.log(err.message)
          })
        } else {
          // console.log('error submit!')
          return false
        }
      })
    }

    return { ruleFormRef, ruleForm, rules, submitForm, validStartTime }
  }
})
</script>

<style>

</style>