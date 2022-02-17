<template>
  <el-dialog 
    v-model="openDialog"
    width="320px"
  >
  <el-form v-loading="loading">
    <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      size="large"
      :hide-required-asterisk="true"
      v-if="!showCodeInput"
     >
      <div class="d-flex flex-column align-items-center">
          <p>입력하신 이메일로 인증번호가 발송됩니다.</p>
      </div>
      <el-form-item prop="email" :error="emailError">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="inputEmail" placeholder="이메일을 입력하세요"></el-input>
      </el-form-item>
      <div class="d-flex flex-row justify-content-center">
        <el-button round type="primary" @click="email">인증 번호 요청</el-button>
      </div>
    </el-form>
      <el-form 
      ref="ruleFormRef"
      :model="ruleForm2"
      :rules="rules2"
      size="large"
      :hide-required-asterisk="true"
      v-else
     >
      <div class="d-flex flex-column align-items-center">
          <p>이메일로 발송된 인증코드를 입력해주세요.</p>
      </div>
      <el-form-item prop="code" :error="codeError">
        <el-input v-model="ruleForm2.code" autocomplete="off" @input="inputCode" placeholder="인증코드을 입력하세요"></el-input>
      </el-form-item>
      <div class="d-flex flex-row justify-content-center">
        <el-button round type="primary" @click="code">임시 비밀번호 발급</el-button>

      </div>
    </el-form>
</el-form>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: "FindPassword",
  props: {
    modelValue: Boolean,
  },
  emits: ['password', 'update:modelValue'],
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
      email:"",
    })
     const ruleForm2 = reactive({
      code:"",
    })
    const nicnknameError = ref('')
    const inputNickname = function () {
      nicnknameError.value = ''
    }

    const emailError = ref('')
    const codeError = ref('')
     const inputEmail = function () {
      emailError.value = ''
    }

    const inputCode = function () {
      codeError.value = ''
    }
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
    const validateCode = (rule: any, value: any, callback: any) => {
      if (ruleForm2.code === '') {
        callback(new Error('코드를 입력해주세요'))
      }  else {
        callback()
      }
    }
    const rules = {
      email: [{ validator: validateEmail, trigger: 'blur' }],
    }

    const rules2 = {
      code: [{ validator: validateCode, trigger: 'blur' }],
    }
    const codeServer = ref('')

    const showEmailCertification = ref(false)
    const emailCertificationCode = ref('')

    const email = function(){
      loading.value = true
      axios.post( `/users/findpw`, ruleForm
        ).then((res: any) => {
            loading.value = false
            console.log(res.data)
            codeServer.value = res.data.code
            showCodeInput.value = true
        }).catch((err: any) => {
            loading.value = false
            console.log(err.response)
        });  
    }

    const code = function(){
      if(codeServer.value == ruleForm2.code){
        loading.value = true
        axios.post( `/users/findpw/email-certi`, ruleForm
          ).then((res: any) => {
              loading.value = false
              ElMessage({
                type: 'success',
                message: '이메일로 임시 비밀번호가 전송되었습니다.',
              })
              console.log('SUCCESS!!');
              emit('password')
          }).catch((err: any) => {
              loading.value = false
              console.log(err.response)
          });  
      } else {
        ElMessage.error('인증번호가 일치하지 않습니다.')
      }
    }
    
    const showCodeInput = ref(false)

    return { 
      codeError,inputCode, email, ruleForm, ruleForm2,rules,
      openDialog, ruleFormRef ,rules2, nicnknameError, inputNickname, emailError, inputEmail,
      showEmailCertification, emailCertificationCode, loading,
      code, showCodeInput
    }
  },
})
</script>
