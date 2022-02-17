<template>
  <el-card class="meeting-card">
    <template #header>
      <div class="card-header">
        <span class="fw-bold">
          {{ meeting.title }}
        </span>
      </div>
    </template>
    <div v-for="(data, index) in meetingData" :key="index" class="text item">
      <div class="row px-3">
        <div class="col-4">
          {{ data.category }}
        </div>
        <div class="col">
          {{ data.content }}
        </div>
      </div>
    </div>
    <div v-if="props.meeting.participantNicknameList" class="d-flex flex-row justify-content-between align-items-center">
      <p class="m-0 capacity"><i class="bi bi-person"></i> {{ props.meeting.participantNicknameList.length }} / {{ props.meeting.userLimit }}</p>
      <el-button 
        class="button" 
        type="primary" 
        plain round
        @click="clickEnter"
        v-if="props.meeting.participantNicknameList.includes(this.$store.state.user.nickname)"
      >
        <el-icon v-if="props.meeting.isLock"><lock /></el-icon>
        <span>참가중</span>
      </el-button>
      <el-button 
        class="button" 
        type="primary" 
        plain round
        :disabled="props.meeting.participantNicknameList.length < props.meeting.userLimit ? false: true"
        @click="clickJoin"
        v-else
      >
        <el-icon v-if="props.meeting.isLock"><lock /></el-icon>
        <span>참가</span>
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
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: 'MeetingCard',
  props: ['meeting'],
  components: { Lock },
  setup (props) {
    const makePrettyTime = function (time: string) {
      let prettyTime = ''
      // const today = new Date(+new Date() + 3240 * 10000).toISOString().split("T")[0]
      // const tomorrow = new Date(+new Date() + 11880 * 10000).toISOString().replace("T", " ").replace(/\..*/, '').slice(0, 10)
      // const twoDaysAfter = new Date(+new Date() + 20520 * 10000).toISOString().replace("T", " ").replace(/\..*/, '').slice(0, 10)
      // if (time.slice(0, 10) === today) {
      //   prettyTime += '오늘'
      // } else if (time.slice(0, 10) === tomorrow) {
      //   prettyTime += '내일'
      // } else if (time.slice(0, 10) === twoDaysAfter) {
      //   prettyTime += '모레'
      // } else {
      //   if (time.slice(0, 4) !== new Date().getFullYear().toString()) {
      //     prettyTime += time.slice(2, 4) + "년 "
      //   }
      // }
      prettyTime += time.slice(5, 6) === "0" ? time.slice(6, 7) + "월 " : time.slice(5, 7) + "월 "
      prettyTime += time.slice(8, 9) === "0" ? time.slice(9, 10) + "일 " : time.slice(8, 10) + "일 "
      prettyTime += time.slice(11, 12) === "0" ? time.slice(12, 13) + "시 " : time.slice(11, 13) + "시 "
      prettyTime += time.slice(14, 15) === "0" ? time.slice(15, 16) + "분" : time.slice(14, 16) + "분"
      return prettyTime
    }

    const meetingData = [
      {
        category: '직군',
        content: props.meeting.industryName,
      },
      {
        category: '회사',
        content: props.meeting.companyNameList.length ? props.meeting.companyNameList[0] : "상관 없음!",
      },
      {
        category: '시작 시간',
        content: props.meeting.startTime === null ? "아무 때나!" : makePrettyTime(props.meeting.startTime),
      },
      {
        category: '종료 시간',
        content: props.meeting.endTime === null ? "미정!" : makePrettyTime(props.meeting.endTime),
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
      if (store.state.user.id) {
        if (props.meeting.isLock) {
          passwordFormVisible.value = true
        } else {
          joinMeeting(props.meeting.id)
        }
      } else {
        ElMessage({
          message: '로그인이 필요합니다.',
          type: 'warning',
        })
      }
    }

    const clickEnter = function () {
      store.dispatch('setMeeting', props.meeting.id)
      router.push({ name: 'Meeting', params: { meetingUrl: props.meeting.url } })
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
        store.dispatch('setMeeting', meetingId)
        router.push({ name: 'Meeting', params: { meetingUrl: res.data.data.url } })
      }).catch(err => {
        if (err.data.message == "비밀번호가 일치하지 않습니다.") {
          passwordError.value = "비밀번호가 일치하지 않습니다"
        }
      })
    }

    const submitForm = (formEl: InstanceType<typeof ElForm> | undefined) => {
      if (!formEl) return
      formEl.validate((valid) => {
        if (valid) {
          joinMeeting(props.meeting.id)
        } else {
          return false
        }
      })
    }

    return { 
      props, meetingData, passwordFormVisible, passwordForm, rules, ruleFormRef, passwordError,
      Lock, 
      clickJoin, submitForm, joinMeeting, clickEnter
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

.capacity {
  background-color: rgb(237, 241, 238);
  padding: 5px 10px;
  font-size: 14px;
  border-radius: 20px;
}
</style>