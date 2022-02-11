<template>
    <div class="wrap">
        <Result :tableDatas="[
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
    ]"></Result>
    </div>
</template>

<script lang="ts">

import axios from 'axios'

import { defineComponent, reactive,ref } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

import Result from "./Result.vue"

export default defineComponent({
	name: 'MyProjectList',
    components: {
        Result
    },

    setup(){
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

        const store = useStore()
        const router = useRouter()

        const password = ref('')
    
        return {meetingInfo,getExpirationDate}
    }


});


</script>

<style>

.wrap {
    max-width: 1000px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    margin: 0 auto;
}
</style>