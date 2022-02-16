<template>
  <div class="waiting">
    <div class="d-flex flex-column justify-content-between align-items-center h-100 w-100">
      <div class="w-100">
        <!-- {{participants}} -->
        <div class="waiting-participant" v-for="(participant, index) in participants" :key="index">
          <span class="fw-bold">{{ participant.nickname }}</span>
          <div class="w-25 text-end">
            <img v-if="participant.ready" alt="ready" src="@/assets/ready.png" class="w-100 ready">
            <img v-else alt="unready" src="@/assets/unready.png" class="w-100 ready">
          </div>
          <!-- <el-button round class="" type="text">
          </el-button> -->
        </div>  
      </div>
      <div class="w-100 d-flex flex-row">
        <div class="col waiting-participant">
          <el-button round style="margin: 0 auto;" type="text" @click="clickLeave">
            <span class="fs-5 fw-bold">나가기</span>
          </el-button>
          <el-button round style="margin: 0 auto;" type="text"
            v-if="this.$store.state.meeting.hostId === this.$store.state.user.id" @click="clickDeleteMeeting">
            <span class="fs-5 fw-bold">방 삭제</span>
          </el-button>
          <el-button round style="margin: 0 auto;" type="text" v-else @click="clickLeaveMeeting">
            <span class="fs-5 fw-bold">참가 취소</span>
          </el-button>
        </div>
        <div class="col waiting-participant">
          <el-button round style="margin: 0 auto;" type="text" 
            v-if="this.$store.state.meeting.hostId === this.$store.state.user.id" @click="startMeeting">
            <span class="fs-5 fw-bold">시작</span>
          </el-button>
          <el-button round style="margin: 0 auto;" type="text" v-else @click="$emit('ready')">
            <span class="fs-5 fw-bold">준비</span>
          </el-button>
        </div>
      </div>
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
  name: 'Waiting',
  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()
    const participants = computed(() => store.state.participants)
    const meetingId = computed(() => store.state.meeting.id)

    const deleteMeeting = function () {
      axios.delete(`/meeting/${meetingId.value}`, 
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }}
      ).then(res => {
        console.log(res)
        ElMessage({
          type: 'success',
          message: '삭제되었습니다.',
        })
        store.dispatch('deleteMeeting')
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
          deleteMeeting()
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다.',
          })
        })
    }

    const leaveMeeting = function () {
      axios.delete(`/meeting/${store.state.meeting.id}/cancle`, 
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }}
      ).then(res => {
        console.log(res)
        axios.get(`/meeting/${store.state.meeting.id}`).then(res => {
          console.log("새 미팅 정보")
          console.log(res)
        })
      }).catch(err => {
        console.log(err.response)
      }) 
    }

    const clickLeaveMeeting = function () {
      ElMessageBox.confirm(
        '참가 신청을 철회하시겠습니까?',
        '참가 신청 철회',
        {
          confirmButtonText: '참가 철회',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          router.push({ name: 'Home'})
          leaveMeeting()
          // store.dispatch('deleteMeeting')
          emit('leave')
          ElMessage({
            type: 'success',
            message: '참가를 취소하셨습니다.',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다.',
          })
        })
    }

    const clickLeave = function () {
      ElMessageBox.confirm(
        '방을 나가시겠습니까? 참가 기록은 유지되며 이후 다시 입장하실 수 있습니다.',
        '나가기',
        {
          confirmButtonText: '나가기',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          router.push({ name: 'Home'})
          emit('leave')
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

    const emitReadySignal = function () {
      emit('ready')
    }

    const startMeeting = function () {
      const numOfParticipants = participants.value.length
      const numOfReady = participants.value.filter((participant: { ready: boolean }) => participant.ready).length
      // console.log(numOfParticipants, numOfReady)
      if (numOfParticipants === numOfReady) {
        emit('start')
      } else {
        ElMessage({
          message: '모든 참가자가 준비해야 합니다.',
          type: 'warning',
        })
      }
    }


    return { participants, clickDeleteMeeting, clickLeaveMeeting, emitReadySignal, startMeeting, clickLeave }
  }
})
</script>

<style>
.waiting {
	display: flex;
	align-content: center;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	vertical-align: middle;
	flex: 1;
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
  padding: 20px;
}

.waiting-participant {
  box-shadow: inset 3px 3px 8px 0 rgba(0, 0, 0, 0.2),
              inset -6px -6px 10px 0 rgba(255, 255, 255, 0.5);

  /* box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px; */
  border-radius: 10px;
  margin: 10px;
  padding: 18px;
  /* width: 100%; */
  /* height: 100%; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  line-height: 2;
}

.ready {
  max-width: 120px;
}
</style>