<template>
  <div class="container result-container">
    <div>
      <span class="fs-3 mb-0">{{ meetingInfo.title }}</span>
    </div>
    <div>
      <el-button>받은 면접 평가 다운로드</el-button>
      <el-button>면접 영상 다운로드</el-button>
      <el-button>채팅 내역 다운로드</el-button>
      <el-button>메모 다운로드</el-button>
      <el-button>공유 파일 다운로드</el-button>
    </div>
    <hr>
    <div class="row p-1" v-for="(data, index) in tableData" :key="index">
      <div class="col-2">
        <span>{{ data.category }}</span>
      </div>
      <div class="col" v-if="typeof(data.content) === 'object'">
        <div class="row" v-for="(participant, index) in data.content" :key="index">
          <span>{{ participant }}</span>
        </div>
      </div>
      <div class="col" v-else>
        <span>{{ data.content }}</span>
      </div>
      <div class="col">

      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: "Result",
  setup() {
    const route = useRoute()

    const meetingId = route.params.meetingId
    const userName = route.params.userName
    const meetingInfo = {
      meetingId: 1,
      title : '일이삼사오육칠팔구십일이삼사오육칠팔구십',
      startTime: '2020-01-01 13:53',
      endTime: '2020-01-01 14:59',
      participants : ['참가자1', '참가자2', '참가자3'],
      industry: 'IT',
      company: '네이버'
    }

    const getExpirationDate = function () {
      const date = new Date(meetingInfo.endTime)
      date.setDate(new Date(meetingInfo.endTime).getDate() + 7)
      return date.getFullYear() + "-"
             + ("0" + (1 + date.getMonth())).slice(-2) + "-" 
             + ("0" + date.getDate()).slice(-2) + " " 
             + date.getHours() + ":" + date.getMinutes()
    }

    const tableData = [
      {
        category: '직군',
        content: meetingInfo.industry,
      },
      {
        category: '회사',
        content: meetingInfo.company,
      },
      {
        category: '시작 시간',
        content: meetingInfo.startTime,
      },
      {
        category: '종료 시간',
        content: meetingInfo.endTime,
      },
      {
        category: '참가자',
        content: meetingInfo.participants,
      },
      {
        category: '다운로드 유효 기간',
        content: getExpirationDate(),
      },
    ]
    return { meetingId, userName, meetingInfo, tableData }
  }
})
</script>

<style scoped>
.result-container{
  border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	padding: 20px;
  margin: 10px;
}

@media screen and (min-width: 1200px) {
  .result-container {
    max-width: 1000px;
  }
}
</style>