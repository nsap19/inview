<template>
  <div class="d-flex flex-row justify-content-end p-3 meeting-footer">
    <el-button :icon="VideoCamera" size="large" circle name="commit" id="joinButton"></el-button>
    <!-- <input type="button" name="commit" value="비디오 참가" id="joinButton" /> -->
    <input type="text" style="display: none;" name="userId" :value="this.$store.state.user.id" id="userId" />
    <input type="text" style="display: none;" name="meetingId" :value="this.$store.state.meeting.id" id="meetingId" />
    <input type="text" style="display: none;" name="userNickname" :value="this.$store.state.user.nickname" id="userNickname" />
    <el-dropdown size='large' class="mx-3">
      <el-button type="primary" size="large" circle :icon="List"></el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item 
            v-for="(participant, index) in participants"
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

    <el-button :icon="ChatDotSquare" size="large" class="me-3" circle @click="[openAside=!(openAside && asideCategory==='chat'), asideCategory=(!openAside || (openAside && asideCategory !=='chat')) ? 'chat':'']"></el-button>
    
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
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { ChatDotSquare, MoreFilled, List, VideoCamera } from '@element-plus/icons-vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'MeetingFooter',
  props: ['openAside', 'asideCategory'],
  setup(props, { emit }) {
    const openAside = computed({
      get: () => props.openAside,
      set: (value) => emit("update:openAside", value),
    });
    const asideCategory = computed({
      get: () => props.asideCategory,
      set: (value) => emit("update:asideCategory", value),
    });

    const store = useStore()
    const participants = computed(() => store.state.participants)
    return { 
      openAside, asideCategory, participants,
      ChatDotSquare, MoreFilled, List, VideoCamera, 
    }
  }
})
</script>

<style>

</style>