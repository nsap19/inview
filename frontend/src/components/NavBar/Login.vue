<template>
  <el-dialog
    v-model="openDialog"
    width="320px"
  >
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      size="large"
      :hide-required-asterisk="true"
    >
      <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
      <el-form-item prop="email" :error="errorMessage">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="clearErrorMessage" placeholder="이메일"></el-input>
      </el-form-item>

      <el-form-item prop="password" :error="errorMessage">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" @input="clearErrorMessage" placeholder="비밀번호"></el-input>
      </el-form-item>

      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="login(ruleFormRef)">로그인</el-button>
        <div class="mt-2">
          <Kakao />
        </div>
        <!-- <el-button @click="openDialog=false">취소</el-button> -->
      </div>
      <div class="d-flex flex-row justify-content-center">
        <el-button type="text" @click="$emit('signup')">회원가입</el-button>
      </div>
       <div class="d-flex flex-row justify-content-center" style="margin-top: 4px; cursor:pointer; font-size: 12px; color: blue;"  @click="$emit('password')">
        <div>비밀번호를 잊어버리셨나요?</div>
      </div>

    </el-form>
  </el-dialog>
</template>

<script lang="ts">
import axios from 'axios'
// import { userAPI } from '@/utils/axios.js'
import { useStore } from 'vuex'
import { defineComponent, computed, ref, reactive } from 'vue'
import type { ElForm } from 'element-plus'
import Kakao from './Kakao.vue'
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: "Login",
  props: {
    modelValue: Boolean,
  },
  components:{
    Kakao
  },
  emits: ['signup', "password",'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({
      get: () => props.modelValue,
      set: (value) => {   ruleForm.email = ''
                          ruleForm.password = ''; 
                          emit("update:modelValue", value); 
                      },
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
            store.dispatch('setUser', { nickname: res.data.nickname, id: res.data.userId });
            ElMessage({
              message: '로그인이 완료되었습니다.',
              type: 'success',
            })
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