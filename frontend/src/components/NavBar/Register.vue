<template>
  <el-dialog 
    title="회원가입" 
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
      <el-form-item label="이메일" prop="email" :error="emailError">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="inputEmail"></el-input>
      </el-form-item>
      <el-form-item label="닉네임" prop="nickname" :error="nicnknameError">
        <el-input v-model="ruleForm.nickname" autocomplete="off" @input="inputNickname"></el-input>
      </el-form-item>
      <el-form-item label="비밀번호" prop="password">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="비밀번호 확인" prop="passwordCheck">
        <el-input type="password" v-model="ruleForm.passwordCheck" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="register(ruleFormRef)">가입</el-button>
        <el-button @click="openDialog=false">취소</el-button>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="$emit('login')">로그인</el-button>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive } from 'vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'

export default defineComponent({
  name: "Register",
  props: {
    modelValue: Boolean,
  },
  emits: ['login', 'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({
      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });
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
      const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
      if (ruleForm.password === '') {
        callback(new Error('비밀번호를 입력해주세요'))
      } else if (passwordPattern.test(ruleForm.password) === false) {
        callback(new Error('비밀번호는 8자를 넘으며 숫자, 영문 대문자, 특수 문자를 포함해야 합니다'))
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
      nickname: [{ required: true, message: '닉네임을 입력해주세요', trigger: 'blur'}],
      email: [{ validator: validateEmail, trigger: 'blur' }],
      password: [{ validator: validatePassword, trigger: 'blur' }],
      passwordCheck: [{ validator: validatePasswordCheck, trigger: 'blur' }],
    }

    const register = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          console.log('valid')
          const { nickname, email, password } = ruleForm

          axios.post("/users/signup/", {
            nickname,
            email,
            password,
          }).then(res => {
            console.log(res)
          }).catch(err => {
            console.log(err.response)
            if (err.response.data.message === "이미 등록된 닉네임입니다.") {
              nicnknameError.value = "중복된 닉네임입니다"
            } else if (err.response.data.message === "이미 등록된 이메일입니다.") {
              emailError.value = "이미 등록된 이메일입니다"
            }
          })

        } else {
          console.log('error submit!')
          return false
        }
      })
    } 

    return { openDialog, ruleFormRef, ruleForm, rules, register, nicnknameError, inputNickname, emailError, inputEmail }
  },

  // methods:{
  //   ...mapMutations(['SET_REGISTER_MODAL', 'SET_LOGIN_MODAL' ]),
  //   async register(){
  //     try {
  //       const {name, email, password} = this.form;
  //       if(!name || !email || !password){
  //         alert("이름, 이메일, 비밀번호를 모두 입력해주세요")
  //         return;
  //       }

  //       const result = await userAPI.register(name, email, password);
  //       if(result.data.status === "OK"){
  //         alert("회원가입이 완료되었습니다");
  //         // 회원가입이 완료되면 자동으로 로그인 modal을 띄운다
  //         this.SET_LOGIN_MODAL(true);
  //       }else{
  //         alert("회원가입이 실패하였습니다.")
  //       }
  //     } catch (error) {
  //       alert("회원가입이 실패하였습니다.")
  //     }
  //   }
  // }
})
</script>
