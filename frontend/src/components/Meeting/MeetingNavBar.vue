<template>
  <div class="d-flex flex-row justify-content-between p-3 meeting-nav">
    <div>
      <span>{{ this.$store.state.meeting.title }}</span>
    </div>
    <div>
      <el-button type="warning">준비</el-button>
      <el-button type="danger" @click="clickDeleteMeeting">삭제</el-button>
      <el-button type="danger" @click="clickCloseMeeting">나가기</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import axios from 'axios'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'MeetingNavBar',
  setup() {
    const store = useStore()
    const router = useRouter()
    const meetingId = computed(() => store.state.meeting.id)

    const deleteMeeting = function () {
      axios.delete(`/meeting/${meetingId.value}`, 
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }}
      ).then(res => {
        console.log(res)
        router.push({ name: 'Home'})
      }).catch(err => {
        console.log(err.response)
      })
    }

    const closeMeeting = function () {
      console.log(localStorage.getItem("token"))
      axios.post(`/meeting/${meetingId.value}/close`, 
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }}
      ).then(res => {
        console.log(res)
        router.push({ name: 'Home'})
      }).catch(err => {
        console.log(err.response)
      })
    }

    const clickDeleteMeeting = function () {
      ElMessageBox.confirm(
        '방을 삭제하시겠습니까? 방의 정보가 삭제됩니다.',
        '방 삭제',
        {
          confirmButtonText: '삭제',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          ElMessage({
            type: 'success',
            message: '삭제되었습니다',
          })
          deleteMeeting()
          store.dispatch('deleteMeeting')
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다',
          })
        })
    }
    const clickCloseMeeting = function () {
      ElMessageBox.confirm(
        '방을 나가시겠습니까?',
        '방 나가기',
        {
          confirmButtonText: '나가기',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          closeMeeting()
          store.dispatch('deleteMeeting')
          router.push({ name: 'Home'})
          ElMessage({
            type: 'success',
            message: '종료되었습니다',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다',
          })
        })
    }
    return { clickDeleteMeeting, clickCloseMeeting }
  }
})
</script>

<style scoped>
.meeting-nav {
  flex: 0 1 auto;
	flex: 0 1 66px;
  /* The above is shorthand for:
  flex-grow: 0,
  flex-shrink: 1,
  flex-basis: auto
  */
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
}
</style>