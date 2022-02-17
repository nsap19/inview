<template>
  <div class="d-flex flex-row justify-content-between p-3 meeting-nav align-items-center">
    <div>
      <span class="fw-bold fs-4 ps-2">{{ this.$store.state.meeting.title }}</span>
    </div>
    <div v-if="startSignal" class="close-button">
      <!-- <el-button type="danger" v-if="this.$store.state.user.id === this.$store.state.meeting.hostId" @click="clickCloseMeeting">종료</el-button> -->
      <el-button type="danger" round @click="clickLeaveMeeting">종료</el-button>
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
  props: {
    startSignal: Boolean,
    closeSignal: Boolean,
  },
  setup(props, { emit }) {
    const startSignal = computed(() => props.startSignal)
    const closeSignal = computed({
      get: () => props.closeSignal,
      set: (value) => emit("update:modelValue", value),
    });
    const store = useStore()
    const router = useRouter()
    const meetingId = computed(() => store.state.meeting.id)

    const closeMeeting = function () {
      console.log("여기 들어오나요??")
      axios.post(`meeting/${meetingId.value}/close`,  {} ,
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }}
      ).then(res => {
        console.log(res)
        router.push({ name: 'Home'})
      }).catch(err => {
        console.log(err.response)
      })
    }

    const clickCloseMeeting = function () {
      ElMessageBox.confirm(
        '종료하시겠습니까?',
        '종료',
        {
          confirmButtonText: '종료',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          emit('closeMeeting')
          closeMeeting()
          console.log("여기 들어오나요??")
          store.dispatch('deleteMeeting')
          router.push({ name: 'Home'})
          ElMessage({
            type: 'success',
            message: '종료되었습니다.',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다.',
          })
        })
    }

    const clickLeaveMeeting = function () {
      ElMessageBox.confirm(
        '면접 연습을 종료하시겠습니까?',
        '면접 연습 종료',
        {
          confirmButtonText: '종료',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          console.log(startSignal.value)
          // if (closeSignal.value) {
          //   closeMeeting()
          // }
          if (startSignal.value && store.state.participants.length === 1) {
            console.log("마지막 남은 사람도 나왔습니다")
            closeMeeting()
          }
          emit('leaveMeeting')
          store.dispatch('deleteMeeting')
          router.push({ name: 'Home'})
          ElMessage({
            type: 'success',
            message: '퇴장하셨습니다.',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다.',
          })
        })
    }
    return { clickCloseMeeting, clickLeaveMeeting, startSignal, closeSignal }
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