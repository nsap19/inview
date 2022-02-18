<template>
  <div>
    <el-dialog v-model="openDialog" width="320px" :title="korTitle[updateWhat]">
      <div v-if="updateWhat === 'delete'">
        <p class="text-center m-0">회원 탈퇴시 개인정보가 즉시 삭제 처리되며,</p>
        <p class="text-center m-0">재가입시 복원되지 않습니다. </p>
        <p class="text-center">탈퇴를 원하신다면 비밀번호를 입력해주세요.</p>
      </div>
      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        size="large"
        :hide-required-asterisk="true"
        v-loading="loading"
      >
        <el-form-item prop="nickname" :error="nicknameError" v-if="updateWhat === 'nickname'">
          <el-input
            v-model="ruleForm.nickname"
            autocomplete="off"
            @input="inputNickname"
            placeholder="새로운 닉네임을 입력하세요"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
            :placeholder="updateWhat === 'nickname' || updateWhat === 'delete' ? '비밀번호를 입력해주세요' : '새로운 비밀번호를 입력해주세요'"
          ></el-input>
        </el-form-item>
        <el-form-item prop="passwordCheck" v-if="updateWhat === 'password'">
          <el-input
            type="password"
            v-model="ruleForm.passwordCheck"
            autocomplete="off"
            placeholder="비밀번호를 다시 입력해주세요"
          ></el-input>
        </el-form-item>
        <div
          class="d-flex flex-column align-self-center"
          style="margin: 10px auto"
        >
          <el-button plain round type="primary" @click="outUser" v-if="updateWhat === 'delete'"
          >탈퇴</el-button>
          <el-button plain round type="primary" @click="ChangeInfo(ruleFormRef)" v-else
          >수정</el-button>
        </div>
        <el-divider></el-divider>
        <div class="d-flex flex-column justify-content-center align-items-center">
          <div class="mb-1" v-if="updateWhat === 'password' || updateWhat==='delete'">
            <el-button plain round type="primary" @click="updateWhat='nickname'">닉네임 수정</el-button>
          </div>
          <div class="" v-if="(updateWhat === 'nickname' || updateWhat==='delete') && !this.$store.state.user.oauth">
            <el-button plain round type="primary" @click="updateWhat='password'">비밀번호 수정</el-button>
          </div>
          <div class="row">
            <el-button type="text" @click="updateWhat='delete'" v-if="updateWhat!=='delete'">회원 탈퇴</el-button>
          </div>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: "UpdateUser",
  props: {
    modelValue: Boolean,
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({
      get: () => props.modelValue,
      set: (value) => {
        ruleForm.password = '',
        ruleForm.passwordCheck = '',
        emit("update:modelValue", value);
      },
    });

    const store = useStore()
    const updateWhat = ref("nickname")
    watch(updateWhat, (val) => {
      resetInfoForm()
    })

    const korTitle = {
      "nickname": "닉네임 변경",
      "password": "비밀번호 변경",
      "delete": "회원 탈퇴"
    }
    const resetInfoForm = () =>{
      ruleForm.nickname = ""
      ruleForm.password =''
      ruleForm.passwordCheck =''
    }

    const loading = ref(false)
    const router = useRouter()

    const password = ref('')

    const ruleFormRef = ref<InstanceType<typeof ElForm>>()

    const ruleForm = reactive({
      nickname: "",
      password:"",
      passwordCheck:""
    })
    const nicknameError = ref('')
    const inputNickname = function () {
      nicknameError.value = ''
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
      password: [{ validator: validatePassword, trigger: 'blur' }],
      passwordCheck: [{ validator: validatePasswordCheck, trigger: 'blur' }],
    }

    const ChangeInfo = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (updateWhat.value === "nickname") {
        ruleForm.passwordCheck = ruleForm.password
      } else {
        ruleForm.nickname = store.state.user.nickname
      }
      if (!formEl) return
      formEl.validate((valid) => {
      if (valid) {
        const { nickname, password } = ruleForm
          axios.put( `/users/${store.state.user.id}`, 
            { nickname: nickname, password: password },
            { headers: {Authorization: `Bearer ${localStorage.getItem("token")}`} }
          ).then((res: any) => {
              ElMessage({
                message: '회원 정보가 수정되었습니다.',
                type: 'success',
              })
              resetInfoForm()
              store.dispatch('setUser', { nickname: nickname, id: store.state.user.id });
              openDialog.value = false
          }).catch((err: any) => {
              if (err.response.data.message === "이미 등록된 닉네임입니다.") {
                nicknameError.value = "이미 등록된 닉네임입니다"
              } else {
                ElMessage({
                  message: '오류가 발생했습니다. 다시 시도해주세요.',
                  type: 'warning',
                })
              }
          })
        } else {
          return false
        }
      })
    } 

    const outUser = () => {
      axios.delete(`/users/${store.state.user.id}?password=${ruleForm.password}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      }).then((res: any) => {
        localStorage.removeItem("token");
        router.push("/")
      }).catch((err: any) => {
      });
    }

    return {
      openDialog, ruleFormRef, ruleForm, rules,
      nicknameError, inputNickname, loading,
      password, ChangeInfo,outUser,
      updateWhat, korTitle
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
