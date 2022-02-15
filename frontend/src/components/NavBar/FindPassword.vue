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
      v-loading="loading"
      style="margin-bottom: 40px"
     >
     <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
      <el-form-item prop="email" :error="emailError">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="inputEmail" placeholder="이메일"></el-input>
      </el-form-item>
      <div class="d-flex flex-row justify-content-center">
        <el-button  style="width:100%" round type="primary" @click="email">이메일 코드 요청</el-button>
      </div>
    </el-form>
      <el-form 
      ref="ruleFormRef"
      :model="ruleForm2"
      :rules="rules2"
      size="large"
      :hide-required-asterisk="true"
      v-loading="loading"
     >
      <el-form-item prop="code" :error="codeError">
        <el-input v-model="ruleForm2.code" autocomplete="off" @input="inputCode" placeholder="인증코드"></el-input>
      </el-form-item>
      <div class="d-flex flex-row justify-content-center">
        <el-button  style="width:100%" round type="primary" @click="code">코드 인증 하기</el-button>

      </div>
    </el-form>

  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'

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
          axios.post( `/users/findpw`, ruleForm
                    ).then((res: any) => {
                       alert("이메일로 코드가 전송되었습니다.")
                        console.log('SUCCESS!!');
                        console.log(res.data)
                        codeServer.value = res.data.code

                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        alert(err.response.data.message)
                        console.log(err.response)
                    });  
   }

    const code = function(){
        if(codeServer.value == ruleForm2.code){
            axios.post( `/users/findpw/email-certi`, ruleForm
                    ).then((res: any) => {
                        alert("이메일로 임시 비밀번호가 전송되었습니다.")
                        console.log('SUCCESS!!');
                        emit('password')

                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        console.log(err.response)
                    });  
        }else{
            alert("코드가 일치하지 않습니다.")
        }
        
   }


    return { 
codeError,inputCode, email,
        ruleForm, ruleForm2,rules,
      openDialog, ruleFormRef ,rules2
      , nicnknameError, inputNickname, emailError, inputEmail,
      showEmailCertification, emailCertificationCode, loading,
      code
    }
  },
})
</script>
