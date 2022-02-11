<template>
  <div class="waiting">
    <div class="d-flex flex-column justify-content-between align-items-center h-100 w-100">
      <div class="w-100">
        <div class="waiting-participant" v-for="(participant, index) in participants" :key="index">
          <span class="">{{ participant }}</span>
          <el-button round class="" type="text">준비</el-button>
        </div>  
      </div>
      <div class="w-100 d-flex flex-row">
        <div class="col waiting-participant">
          <el-button round style="margin: 0 auto;" type="text"
            v-if="this.$store.state.meeting.hostId === this.$store.state.user.id" @click="clickDeleteMeeting">방 삭제</el-button>
          <el-button round style="margin: 0 auto;" type="text" v-else>나가기</el-button>
        </div>
        <div class="col waiting-participant">
          <el-button round style="margin: 0 auto;" type="text" 
            v-if="this.$store.state.meeting.hostId === this.$store.state.user.id">시작</el-button>
          <el-button round style="margin: 0 auto;" type="text" v-else>준비</el-button>
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
  setup() {
    const participants = ['일이삼사오육칠팔구십', '일이삼사오육칠팔구', '일이삼사오육칠팔', '일이삼사오육칠', '일', '일이']
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
            message: '삭제되었습니다.',
          })
          deleteMeeting()
          store.dispatch('deleteMeeting')
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
        '방을 나가시겠습니까?',
        '방 나가기',
        {
          confirmButtonText: '나가기',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
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
    return { participants, clickDeleteMeeting, clickLeaveMeeting }
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
  padding: 20px;
  /* width: 100%; */
  /* height: 100%; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  line-height: 2;
}
</style>