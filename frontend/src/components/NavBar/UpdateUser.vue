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
      <!-- <el-form-item prop="email" :error="emailError">
        <el-input v-model="ruleForm.email" autocomplete="off" @input="inputEmail" placeholder="이메일"></el-input>
      </el-form-item> -->
      <el-form-item prop="nickname" :error="nicnknameError">
        <el-input v-model="ruleForm.nickname" autocomplete="off" @input="inputNickname" placeholder="닉네임"></el-input>
      </el-form-item>
      <el-form-item  prop="password">
        <el-input type="password" v-model="infoChangeForm.password" autocomplete="off" placeholder="비밀번호"></el-input>
      </el-form-item>
      <el-form-item prop="passwordCheck">
        <el-input type="password" v-model="infoChangeForm.passwordCheck" autocomplete="off" placeholder="비밀번호 확인"></el-input>
      </el-form-item>
      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="dialogVisible">회원탈퇴</el-button>
      </div>
      <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="login(ruleFormRef)">수정</el-button>
        <!-- <el-button @click="openDialog=false">취소</el-button> -->
      </div>
      <!-- <div class="d-flex flex-column align-self-center" style="margin: 10px auto">
        <el-button type="primary" round @click="register(ruleFormRef)">다음</el-button>
      </div> -->
      <!-- <div class="d-flex flex-row justify-content-center">
        <el-button type="text" @click="$emit('login')">로그인</el-button>
      </div> -->
      <p id="err_msg1">{{ passwordChangeErrorMessage }}</p>
    </el-form>
    <el-dialog v-model="dialogVisible" title="Warning" width="30%" center>
        <span
        >정말 회원 탈퇴를 진행하시겠습니까?</span
        >
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="Delete"
                >Confirm</el-button
                >
            </span>
        </template>
  </el-dialog>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import EmailCertification from '@/components/NavBar/EmailCertification.vue';

export default defineComponent({
  name: "UpdateUser",
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
    const nicknameError = ref('')
    const inputNickname = function () {
      nicknameError.value = ''
    }

    const emailError = ref('')
    const inputEmail = function () {
      emailError.value = ''
    }

    // eslint-disable-next-line
    const validateNickname = (rule: any, value: any, callback: any) => {
      if (ruleForm.nickname === '') {
        callback(new Error('닉네임을 입력해주세요'))
      } else if (ruleForm.nickname.indexOf(' ') > -1) {
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

    const showEmailCertification = ref(false)
    const emailCertificationCode = ref('')

    const store = useStore()
    const router = useRouter()

    const password = ref('')

    const infoChangeForm = reactive({
        password: '',
        passwordCheck: '',
        nickname: ''
    })
        
    const infoChangeErrorMessage = ref('')

    const dialogVisible = ref(false)

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
                    infoChangeErrorMessage.value = '비밀번호는 8자를 넘으며 숫자, 영문 대문자, 특수 문자를 포함해야 합니다'
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

        const resetInfoForm = () =>{
            infoChangeForm.nickname =''
            infoChangeForm.password =''
            infoChangeForm.passwordCheck =''
        }

        const Delete = () =>{
            axios.delete(`/users/${store.state.user.id}`, {params: {password: password.value}}).then((res: any) => {
                        console.log('SUCCESS!!');
                        console.log(res)

                        localStorage.removeItem("token");
                        router.push("/")

                        password.value = ""

                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        console.log(err.response)
                    });  
            dialogVisible.value = false
        }

    return { 
      openDialog, ruleFormRef, ruleForm, rules, 
      nicknameError, inputNickname, emailError, inputEmail,
      showEmailCertification, emailCertificationCode, loading,
      Delete, password, dialogVisible, ChangeInfo,
      infoChangeForm
    }
  },
})
</script>
