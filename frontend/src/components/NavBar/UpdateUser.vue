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
     >
      <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
      <el-form-item prop="nickname" :error="nicknameError">
        <el-input v-model="ruleForm.nickname" autocomplete="off" @input="inputNickname" placeholder="닉네임"></el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="비밀번호"></el-input>
      </el-form-item>
      <el-form-item prop="passwordCheck">
        <el-input type="password" v-model="ruleForm.passwordCheck" autocomplete="off" placeholder="비밀번호 확인"></el-input>
      </el-form-item>
      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="ChangeInfo()">수정</el-button>
      </div>
      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="text" @click="$emit('outUser')">회원탈퇴</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import type { ElForm } from 'element-plus'
import axios from 'axios'

export default defineComponent({
  name: "UpdateUser",
  props: {
    modelValue: Boolean,
  },
  emits: ['outUser', 'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({

      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });

    watch(() => props.modelValue, (newValue, oldValue) => {
      console.log('props.visible 의 변이가 감지되었을 때 ', {newValue, oldValue})
    })

    const resetInfoForm = () =>{
        //infoChangeForm.nickname =''
        infoChangeForm.password =''
        infoChangeForm.passwordCheck =''
    }

    const loading = ref(false)
    const store = useStore()
    const router = useRouter()

    const password = ref('')
    const nickname = ref(store.state.user.nickname)

    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    const ruleForm = reactive({
      nickname: store.state.user.nickname,
      password:"",
      passwordCheck:""
    })
    const nicknameError = ref('')
    const inputNickname = function () {
      nicknameError.value = ''
    }

    // eslint-disable-next-line
    const validateNickname = (rule: any, value: any, callback: any) => {
      if (ruleForm.nickname.indexOf(' ') > -1) {
        callback(new Error('닉네임에 공백이 포함될 수 없습니다'))
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
      nickname: [{ validator: validateNickname, trigger: 'blur' }],
      password: [{ validator: validatePassword, trigger: 'blur' }],
      passwordCheck: [{ validator: validatePasswordCheck, trigger: 'blur' }],
    }

    const infoChangeForm = reactive({
        password: '',
        passwordCheck: '',
        nickname: nickname
    })
        
    const infoChangeErrorMessage = ref('')

    const openOutUserDialog = ref(false)

    const ChangeInfo = ()=>{
        const {nickname, password, passwordCheck } = infoChangeForm
        if(password == "" || passwordCheck == ""){
               infoChangeErrorMessage.value = "빈칸을 모두 입력해주세요."
        }
        else if (password != passwordCheck) {
            infoChangeErrorMessage.value = '비밀번호가 같지 않습니다.'
        }
        else {
            const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
            if(passwordPattern.test(password) === false){
                infoChangeErrorMessage.value = '비밀번호는 8자를 넘어야 하며 숫자, 영문 대문자, 특수 문자를 포함해야 합니다'
            }
            else{
              infoChangeErrorMessage.value = ''
                axios.put( `/users/${store.state.user.id}`, {
                    nickname: nickname,
                    password: password,
                },
                    {
                      headers: 
                        {
                            Authorization: `Bearer ${localStorage.getItem("token")}`,
                        }
                    }
                ).then((res: any) => {
                    console.log('SUCCESS!!');
                    console.log(res)
                        
                    resetInfoForm()
                        
                }).catch((err: any) => {
                    console.log('FAILURE!!');
                    console.log(err.response)
                        
                });  
                    //reset
            }

        }
    }

    return { 
      openDialog, ruleFormRef, ruleForm, rules, 
      nicknameError, inputNickname, loading,
      password, openOutUserDialog, ChangeInfo,
      infoChangeForm
    }
  },
})
</script>

<style>

.wrap {
    max-width: 1000px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    margin: 0 auto;
}

.card-1 {
    width: 400px;
    min-height: 350px;
    margin-top: 50px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-2 {
    margin-top: 100px;
    width: 400px;
    min-height: 200px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-3 {
    margin-top: 100px;
    width: 400px;
    min-height: 400px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-body2 {
    padding: 10px;
}

.card-header2 {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 2%;
}

.card-header {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
}

.card-body1 {
    padding: 2%;
}

.change {
    position: absolute;
    right: 10px;
}

#change {
    justify-self: end;
}

#box {
    margin: 10px;
}

#btn_submit {
    position: absolute;
    bottom: 10px;
    right: 10px;
    width: 80px;
    height: 30px;
}

#err_msg1 {
    color: red;
    text-align: center;
}

</style>
