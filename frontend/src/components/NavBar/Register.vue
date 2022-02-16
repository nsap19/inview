<template>
  <el-dialog 
    v-model="openDialog"
    width="320px"
  >
    <EmailCertification 
      v-if="showEmailCertification" 
      :code="emailCertificationCode" 
      :signupInfo="ruleForm"
      @closeDialog="[openDialog=false, showEmailCertification=false]"
    />
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      size="large"
      :hide-required-asterisk="true"
      v-else
      v-loading="loading"
     >
      <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
      <el-form-item prop="email" :error="emailError">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="inputEmail" placeholder="이메일"></el-input>
      </el-form-item>
      <el-form-item prop="nickname" :error="nicnknameError">
        <el-input v-model="ruleForm.nickname" autocomplete="off" @input="inputNickname" placeholder="닉네임"></el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="비밀번호"></el-input>
      </el-form-item>
      <el-form-item prop="passwordCheck">
        <el-input type="password" v-model="ruleForm.passwordCheck" autocomplete="off" placeholder="비밀번호 확인"></el-input>
      </el-form-item>
      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="register(ruleFormRef)">다음</el-button>
      </div>
      <div class="d-flex flex-row justify-content-center">
        <el-button type="text" @click="$emit('login')">로그인</el-button>
      </div>
    </el-form>

  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import EmailCertification from '@/components/NavBar/EmailCertification.vue';

export default defineComponent({
  name: "Register",
  components: { EmailCertification },
  props: {
    modelValue: Boolean,
  },
  emits: ['login', 'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });

    watch(() => props.modelValue, (newValue, oldValue) => {
      console.log('props.visible 의 변이가 감지되었을 때 ', {newValue, oldValue})
      showEmailCertification.value = false
    })

    const loading = ref(false)

    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    const ruleForm = reactive({
      nickname: "",
      email:"",
      password:"",
      passwordCheck:""
    })
    const nicnknameError = ref('')
    const inputNickname = function () {
      nicnknameError.value = ''
    }

    const emailError = ref('')
    const inputEmail = function () {
      emailError.value = ''
    }

    // eslint-disable-next-line
    const validateNickname = (rule: any, value: any, callback: any) => {
      if (ruleForm.nickname === '') {
        callback(new Error('닉네임을 입력해주세요'))
      } else if (ruleForm.nickname.length > 10) {
        callback(new Error('10자 이하로 입력해주세요'))
      } else if (ruleForm.nickname.indexOf(' ') > -1) {
        callback(new Error('닉네임에 공백이 포함될 수 없습니다'))
      } else {
        callback()
      }
    }

    // eslint-disable-next-line
    const validateEmail = (rule: any, value: any, callback: any) => {
      const emailPattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
      if (ruleForm.email === '') {
        callback(new Error('이메일을 입력해주세요'))
      } else if (emailPattern.test(ruleForm.email) === false) {
        callback(new Error('이메일 형식을 확인해주세요'))
      } else {
        callback()
      }
    }

    // eslint-disable-next-line
    const validatePassword = (rule: any, value: any, callback: any) => {
      const passwordPattern = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z]).{8,}$/;
      if (ruleForm.password === '') {
        callback(new Error('비밀번호를 입력해주세요'))
      } else if (passwordPattern.test(ruleForm.password) === false) {
        callback(new Error('8자 이상으로 영문, 숫자, 특수문자를 포함시켜 주세요'))
      } else {
        callback()
      }
    }

    // eslint-disable-next-line
    const validatePasswordCheck = (rule: any, value: any, callback: any) => {
      if (ruleForm.passwordCheck === '') {
        callback(new Error('비밀번호를 다시 입력해주세요'))
      } else if (ruleForm.password !== ruleForm.passwordCheck) {
        callback(new Error('비밀번호가 일치하지 않습니다'))
      } else {
        callback()
      }
    }

    const rules = {
      nickname: [{ validator: validateNickname, trigger: 'blur' }],
      email: [{ validator: validateEmail, trigger: 'blur' }],
      password: [{ validator: validatePassword, trigger: 'blur' }],
      passwordCheck: [{ validator: validatePasswordCheck, trigger: 'blur' }],
    }

    const showEmailCertification = ref(false)
    const emailCertificationCode = ref('')

    const signup = function (body: {nickname: string, email: string, password: string}) {
      axios.post("/users/signup/", body).then(res => {
        loading.value = false
        console.log(res)
        emailCertificationCode.value = res.data.code
        showEmailCertification.value = true
      }).catch(err => {
        loading.value = false
        console.log(err.response)
        if (err.response.data.message === "이미 등록된 닉네임입니다.") {
          nicnknameError.value = "중복된 닉네임입니다"
        } else if (err.response.data.message === "이미 등록된 이메일입니다.") {
          emailError.value = "이미 등록된 이메일입니다"
        }
      })
    }

    const register = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          console.log('valid')
          loading.value = true
          const { nickname, email, password } = ruleForm
          signup({nickname, email, password})
        } else {
          console.log('error submit!')
          return false
        }
      })
    } 

    return { 
      openDialog, ruleFormRef, ruleForm, rules, 
      register, nicnknameError, inputNickname, emailError, inputEmail,
      showEmailCertification, emailCertificationCode, loading
    }
  },
})
</script>
