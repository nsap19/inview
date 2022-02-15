<template>
  <!-- <el-dialog 
    v-model="openDialog"
    width="320px"
  > -->
    <el-form 
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      size="large"
      :hide-required-asterisk="true"
      v-loading="loading"
     >
      <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100 p-2 mb-3">
      <div class="text-center mb-2">
        <span>
          <p>회원탈퇴시 개인정보가 즉시 삭제 처리되며, <br>재가입시 복원되지 않습니다.</p>
          <p>탈퇴신청을 하시겠습니까?</p>
        </span>
      </div>
      <div>
            <span class="dialog-footer">
                <el-button @click="$emit('updateUser')">취소</el-button>
                <el-button type="primary" @click="Delete"
                >확인</el-button
                >
            </span>
      </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, computed, ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import type { ElForm } from 'element-plus'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default defineComponent({
  name: "OutUser",
  props: {
    modelValue: Boolean,
  },
  emits: ['updateUser', 'update:modelValue'],
  setup(props, { emit }) {
    const openDialog = computed({

      get: () => props.modelValue,
      set: (value) => emit("update:modelValue", value),
    });

    watch(() => props.modelValue, (newValue, oldValue) => {
      console.log('props.visible 의 변이가 감지되었을 때 ', {newValue, oldValue})
    })

    // const loading = ref(false)
    const store = useStore()
    const router = useRouter()

    const password = ref('')

//    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    // const ruleForm = reactive({
    //   nickname: store.state.user.nickname,
    //   password:"",
    //   passwordCheck:""
    // })
    // const nicknameError = ref('')
    // const inputNickname = function () {
    //   nicknameError.value = ''
    // }

    // eslint-disable-next-line

    const outUserDialog = ref(false)

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
              alert("회원탈퇴 실패");
              
        });  
        outUserDialog.value = false
    }

    return { 
      openDialog,
//      ruleFormRef, ruleForm, nicknameError, inputNickname, loading,
      Delete, password, outUserDialog,
    }
  },
})
</script>

<style>
</style>