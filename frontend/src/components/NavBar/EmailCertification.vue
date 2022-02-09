<template>
  <el-form 
    ref="ruleFormRef"
    :model="ruleForm"
    :rules="rules"
    size="large"
    :hide-required-asterisk="true"
  >
    <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
    <div class="text-center mb-2">
      <p>이메일로 인증 번호가 전송되었습니다.</p>
      <p>인증 번호를 입력해주세요.</p>
    </div>
    <el-form-item prop="code">
      <el-input v-model="ruleForm.code" autocomplete="off" placeholder="인증 번호"></el-input>
    </el-form-item>
    <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
      <el-button type="primary" round @click="certificate(ruleFormRef)">인증</el-button>
    </div>
  </el-form>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from 'vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: "Register",
  props: ['code', 'signupInfo'],
  setup(props, { emit }) {
    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    const ruleForm = reactive({
      code: "",
    })

    // eslint-disable-next-line
    const validateCode = (rule: any, value: any, callback: any) => {
      if (ruleForm.code === '') {
        callback(new Error('인증번호를 입력해주세요'))
      } else if (ruleForm.code !== props.code) {
        callback(new Error('인증번호가 일치하지 않습니다'))
      } else {
        callback()
      }
    }

    const rules = {
      code: [{ validator: validateCode, trigger: 'blur' }],
    }
    const store = useStore()

    const login = function () {
      axios.post('/users/login', {
        email: props.signupInfo.email,
        password: props.signupInfo.password
      }).then(res => {
        console.log(res)
        localStorage.setItem("token", res.data.token);
        store.dispatch('setUser', { nickname: res.data.nickname, id: res.data.userId });
        emit('closeDialog')
      }).catch(err => {
        console.log(err.response)
      })
    }

    const certificate = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          axios.post('/users/signup/email-certi', {
            code: ruleForm.code,
            email: props.signupInfo.email,
            nickname: props.signupInfo.nickname,
            password: props.signupInfo.password
          }).then(res => {
            console.log(res)
            login()
            ElMessage({
              message: '회원가입이 완료되었습니다.',
              type: 'success',
            })
          }).catch(err => {
            console.log(err)
          })
        } else {
          console.log('error submit!')
          return false
        }
      })
    }

    return { ruleFormRef, ruleForm, rules, certificate }
  }
})
</script>

<style>

</style>