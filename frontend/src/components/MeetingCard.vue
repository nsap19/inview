<template>
  <el-card class="meeting-card">
    <template #header>
      <div class="card-header">
        <span>
          {{ meeting.title }}
        </span>
      </div>
    </template>
    <div v-for="(data, index) in meetingData" :key="index" class="text item">
      <div class="row" v-if="data.content">
        <div class="col-4">
          {{ data.category }}
        </div>
        <div class="col">
          {{ data.content }}
        </div>
      </div>
    </div>
    <div v-if="props.meeting.participantNicknameList" class="d-flex flex-row justify-content-between align-items-center">
      <p class="m-0"><i class="bi bi-person"></i> {{ props.meeting.participantNicknameList.length }} / {{ props.meeting.userLimit }}</p>
      <el-button 
        class="button" 
        type="primary" 
        plain 
        :disabled="props.meeting.participantNicknameList.length < props.meeting.userLimit ? false: true"
        @click="clickJoin"
      >
        <el-icon v-if="props.meeting.isLock"><lock /></el-icon>
        <span>{{props.meeting.isLock}}참가</span>
      </el-button>
    </div>
  </el-card>

  <el-dialog v-model="passwordFormVisible" title="비밀번호를 입력해주세요" width="30%" top="30vh">
    <el-form ref="ruleFormRef" :model="passwordForm" :rules="rules">
      <el-form-item prop="password" :error="passwordError">
        <el-input v-model="passwordForm.password" autocomplete="off" size="large"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="passwordFormVisible = false">취소</el-button>
        <!-- <el-button type="primary" @click="submitForm(ruleFormRef)" -->
        <el-button type="primary" @click="joinMeeting(props.meeting.id)"
          >입장</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from 'vue'
import { Lock } from '@element-plus/icons-vue'
import type { ElForm } from 'element-plus'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'MeetingCard',
  props: ['meeting'],
  components: { Lock },
  setup (props) {
    const meetingData = [
      {
        category: '직군',
        content: props.meeting.industryName,
      },
      {
        category: '회사',
        content: props.meeting.companyNameList,
      },
      {
        category: '시작 시간',
        content: props.meeting.startTime,
      },
      {
        category: '종료 시간',
        content: props.meeting.endTime,
      },
    ]

    const passwordFormVisible = ref(false)

    const ruleFormRef = ref<InstanceType<typeof ElForm>>()
    const passwordForm = reactive({
      password: ''
    })

    // eslint-disable-next-line
    const validPassword = (rule: any, value: any, callback: any) => {
      if (passwordForm.password !== props.meeting.password) {
        callback(new Error('비밀번호가 일치하지 않습니다'))
      } else {
        callback()
      }
    }
    const rules = {
      password: [
        { 
          validator: validPassword,
          trigger: "blur",
        }
      ]
    }
    const passwordError = ref('')
    const clickJoin = function () {
      if (props.meeting.isLock) {
        passwordFormVisible.value = true
      } else {
        joinMeeting(props.meeting.id)
      }
    }
    
    const router = useRouter()
    const store = useStore()
    const joinMeeting = function (meetingId: number) {
      axios({
        url: `/meeting/${meetingId}/join`,
        method: 'POST',
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        params: { password: passwordForm.password },
      }).then(res => {
        console.log(res, 'joinMeeting')
        store.dispatch('setMeeting', meetingId)
        router.push({ name: 'Meeting', params: { meetingUrl: res.data.data.url } })
      }).catch(err => {
        console.log(err)
        if (err.data.message == "비밀번호가 일치하지 않습니다.") {
          passwordError.value = "비밀번호가 일치하지 않습니다"
        }
      })
    }

    const submitForm = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          console.log('submit!')
          joinMeeting(props.meeting.id)
        } else {
          console.log('error submit!')
          return false
        }
      })
    }

    return { 
      props, meetingData, passwordFormVisible, passwordForm, rules, ruleFormRef, passwordError,
      Lock, 
      clickJoin, submitForm, joinMeeting
    }
  },
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: transparent;
  border: none;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.meeting-card {
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
}
</style>