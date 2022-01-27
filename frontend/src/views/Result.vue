<template>
  <div class="container px-5">
    <h1>{{ meetingInfo.title }}</h1>
    <h4> {{ meetingInfo.industry }}</h4>
    <h4>{{ meetingInfo.company }}</h4>
    <p>{{ meetingInfo.startTime }} - {{ meetingInfo.endTime }}</p>
    <p>{{ meetingInfo.participants }}</p>
    <p>다운로드 유효 기간: </p>

    <el-button>받은 면접 평가 다운로드</el-button>
    <el-button>면접 영상 다운로드</el-button>
    <el-button>채팅 내역 다운로드</el-button>
    <el-button>메모 다운로드</el-button>
    <el-button>공유 파일 다운로드</el-button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: "Result",
  setup() {
    const route = useRoute()

    const meetingId = route.params.meetingID
    const userName = route.params.userName

    const meetingInfo = {
      meetingId: 1,
      title : '일이삼사오육칠팔구십일이삼사오육칠팔구십',
      startTime: '2020-01-01 13:53',
      endTime: '2020-01-01 14:59',
      participants : ['참가자1', '참가자2', '참가자3'],
      industry: 'IT',
      company: ['네비어', '카카오']
    }

    return { meetingId, userName, meetingInfo }
  }
})
</script>

<style>

</style>