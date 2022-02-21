<template>
  <div class="d-flex flex-row justify-content-end p-3 meeting-footer">
    <div class="col-4 col-md-4 text-center" v-show="startSignal">
      <el-button type="primary" size="large" circle id="mute">
        <i class="bi bi-mic" id="micOn"></i>
        <i class="bi bi-mic-mute" id="micOff" style="display: none"></i>
      </el-button>
      <el-button type="primary" id ="camera" size="large" circle>
        <i class="bi bi-camera-video" id ="cameraOn"></i>
        <i class="bi bi-camera-video-off" id ="cameraOff" style="display: none"></i>
      </el-button>
      <el-button type="primary" id ="record" size="large" circle v-show="!record" @click="record=true">
        <i class="bi bi-record-fill"></i>
      </el-button>
      <el-button type="primary" id ="stopRecording" size="large" circle v-show="record" @click="record=false" style="background: linear-gradient(140deg, rgba(243, 240, 215, 1) -10%, rgba(78, 115, 81, 0.8) 50%), url(https://grainy-gradients.vercel.app/noise.svg);">
        <i class="bi bi-record-fill blink"></i>
      </el-button>
      <input type="button" name="commit" value="비디오 참가" id="joinButton" style="display: none" />
      <input type="text" style="display: none;" name="userId" :value="this.$store.state.user.id" id="userId" />
      <input type="text" style="display: none;" name="meetingId" :value="this.$store.state.meeting.id" id="meetingId" />
      <input type="text" style="display: none;" name="userNickname" :value="this.$store.state.user.nickname" id="userNickname" />
    </div>
    
    <div class="col-8 col-md-4 text-end">
      <el-dropdown size='large' class="mx-3">
        <el-button type="primary" size="large" circle :icon="List"></el-button>
        <template #dropdown>
          <el-dropdown-menu v-if="participantsExceptMe.length">
            <el-dropdown-item 
              v-for="(participant, index) in participantsExceptMe"
              :key="index"
              @click="[
                openAside=!(openAside && asideCategory==='evaluation' + participant.nickname), 
                asideCategory=(!openAside || (openAside && asideCategory !=='evaluation')) ? 'evaluation' + participant.nickname:''
              ]"
            >
              <span class="fw-bold">{{ participant.nickname }}</span>
              님의 평가지
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <el-button size="large" class="me-3" type="primary" circle 
        @click="[openAside=!(openAside && asideCategory==='chat'), asideCategory=(!openAside || (openAside && asideCategory !=='chat')) ? 'chat':'']"
      ><i class="bi bi-chat-dots"></i></el-button>
      
      <el-dropdown trigger="click" size='large'>
        <el-button type="primary" size="large" circle :icon="MoreFilled"></el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="[openAside=!(openAside && asideCategory==='memo'), asideCategory=(!openAside || (openAside && asideCategory !=='memo')) ? 'memo':'']">메모</el-dropdown-item>
            <el-dropdown-item @click="[openAside=!(openAside && asideCategory==='timer'), asideCategory=(!openAside || (openAside && asideCategory !=='timer')) ? 'timer':'']">타이머</el-dropdown-item>
            <el-dropdown-item @click="[openAside=!(openAside && asideCategory==='file'), asideCategory=(!openAside || (openAside && asideCategory !=='file')) ? 'file':'']">파일전송</el-dropdown-item>
            <el-dropdown-item @click="[openAside=!(openAside && asideCategory==='participant'), asideCategory=(!openAside || (openAside && asideCategory !=='participant')) ? 'participant':'']">참가자</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref } from 'vue'
import { ChatDotSquare, MoreFilled, List, VideoCamera, Microphone, Mute } from '@element-plus/icons-vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'MeetingFooter',
  props: ['openAside', 'asideCategory', 'startSignal'],
  setup(props, { emit }) {
    const openAside = computed({
      get: () => props.openAside,
      set: (value) => emit("update:openAside", value),
    })
    const asideCategory = computed({
      get: () => props.asideCategory,
      set: (value) => emit("update:asideCategory", value),
    })

    const store = useStore()
    const participants = computed(() => store.state.participants)
    const startSignal = computed(() => props.startSignal)

    const participantsExceptMe = computed(() => store.state.participants.filter((participant: { id: any }) => {
      return parseInt(participant.id) !== store.state.user.id
    }))
    const record = ref(false)
    return { 
      openAside, asideCategory, participants, startSignal, record, participantsExceptMe,
      ChatDotSquare, MoreFilled, List, VideoCamera, Microphone, Mute,
    }
  }
})
</script>

<style>
.blink {
  animation: blinker 1.5s cubic-bezier(.5, 0, 1, 1) infinite alternate;  
  color: #FF7878;
}
@keyframes blinker {  
  from { opacity: 1; }
  to { opacity: 0; }
}
</style>