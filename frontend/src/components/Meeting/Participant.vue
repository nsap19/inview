<template>
  <div class="p-2">
    <div 
      v-for="participant in participants" 
      :key="participant.pk" 
      class="d-flex flex-row justify-content-between align-items-center py-2 px-3"
    >
      <span class="fw-bold">{{ participant.nickname }}</span>
      <div>
        <!-- <i class="bi bi-mic-mute-fill p-2"></i>
        <i class="bi bi-camera-video-off-fill p-2"></i> -->
        <el-button type="danger" plain round size="small"
          @click="clickKickParticipant(4, participant.nickname)"
          v-if="this.$store.state.meeting.hostId === this.$store.state.user.id && this.$store.state.user.nickname !== participant.nickname"
        >강퇴</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Mute } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import axios from 'axios'

export default defineComponent({
  name: 'Participant',
  setup() {
    const store = useStore()
    const participants = computed(() => store.state.participants)

    const kickParticipant = function (userId: number) {
      axios.delete(`/meeting/${store.state.meeting.id}/users/${userId}`, 
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

    const clickKickParticipant = function (userId: number, userNickname: string) {
      ElMessageBox.confirm(
        `${userNickname}님을 강퇴하시겠습니까?`,
        '강퇴',
        {
          confirmButtonText: '강퇴',
          cancelButtonText: '취소',
          type: 'warning',
        }
      )
        .then(() => {
          kickParticipant(userId)
          ElMessage({
            type: 'success',
            message: '강퇴되었습니다.',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '취소되었습니다.',
          })
        })
    }

    return { participants, Mute, clickKickParticipant }
  }
})
</script>

<style>

</style>