<template>
  <el-dialog
    title="로그인"
    v-model="openDialog"
  >
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      size="large"
      :hide-required-asterisk="true"
    >
      <el-form-item label="이메일" prop="email" :error="errorMessage">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="clearErrorMessage"></el-input>
      </el-form-item>

      <el-form-item label="비밀번호" prop="password" :error="errorMessage">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" @input="clearErrorMessage"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="login(ruleFormRef)">로그인</el-button>
        <el-button @click="openDialog=false">취소</el-button>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="$emit('signup')">회원가입</el-button>
  </el-dialog>
</template>

<script lang="ts">
import axios from 'axios'
// import { userAPI } from '@/utils/axios.js'
import { useStore } from 'vuex'
import { defineComponent, computed, ref, reactive } from 'vue'
import type { ElForm } from 'element-plus'

export default defineComponent({
  name: "Login",
  props: {
    modelValue: Boolean,
  },
  emits: ['signup', 'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });
    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    const ruleForm = reactive({
      email: '',
      password: '',
    })

    const rules = {
      email: [
        {
          required: true,
          message: '이메일을 입력해주세요',
          trigger: 'blur',
        },
      ],
      password: [
        {
          required: true,
          message: '비밀번호를 입력해주세요',
          trigger: 'blur',
        },
      ],
    }
    const errorMessage = ref('')
    const clearErrorMessage = function () {
      errorMessage.value = ''
    }

    const store = useStore()

    const login = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          const { email, password } = ruleForm

          axios.post('/users/login', {
            email: email,
            password: password
          }).then(res => {
            console.log(res)
            
            ruleForm.email = ''
            ruleForm.password = ''

            emit("update:modelValue", false)
            localStorage.setItem("token", res.data.token);
            store.dispatch('setUser', {nickname: email});
          }).catch(err => {
            console.log(err.response)
            errorMessage.value = "이메일과 비밀번호를 확인해주세요"
          })
        } else {
          // console.log('error submit!')
          return false
        }
      })
    } 
 

    return { openDialog, ruleFormRef, ruleForm, login, rules, errorMessage, clearErrorMessage }
  },
})
</script>

<style>

</style>