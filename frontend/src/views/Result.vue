<template>
  <div class="container result-container">
    <div>
      <span class="fs-3 mb-0">{{ props.tableDatas.title }}</span>
    </div>
    <div>
      <el-button @click="download('')">받은 면접 평가 다운로드</el-button>
      <el-button @click="download('')">면접 영상 다운로드</el-button>
      <el-button @click="download('CHAT')">채팅 내역 다운로드</el-button>
      <el-button @click="download('')">메모 다운로드</el-button>
      <el-button @click="download('')">공유 파일 다운로드</el-button>
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

<script>
import { defineComponent, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

import axios from "axios"

export default defineComponent({
  name: "Result",
   props: {
    tableDatas: {
      type: Array,
      required: true
    }
  },
  setup(props) {
    const store = useStore()
    const route = useRoute()

    const meetingId = route.params.meetingId
    const userName = route.params.userName
  
    const getExpirationDate = function () {
      const date = new Date(props.tableDatas.endTime)
      date.setDate(new Date(props.tableDatas.endTime).getDate() + 7)
      return date.getFullYear() + "-"
             + ("0" + (1 + date.getMonth())).slice(-2) + "-" 
             + ("0" + date.getDate()).slice(-2) + " " 
             + date.getHours() + ":" + date.getMinutes()
    }

    const download = function (type){
      axios.get(`users/${store.state.user.id}/meeting/${props.tableDatas.meetingId}`).then(res=>{
        console.log("download",res.data.data.archives)

        for ( let v of res.data.data.archives){
          if(v.archiveType == type){
            console.log("s", v)

             var element = document.createElement('a');
            element.setAttribute('href',v.path);
            document.body.appendChild(element);
             element.click();
          }
        }
        
      })
    }

    const tableData = ref([
      {
        category: '직군',
        content: props.tableDatas.industry.industryName,
      },
      {
        category: '회사',
        content: props.tableDatas.company,
      },
      {
        category: '시작 시간',
        content: props.tableDatas.startTime,
      },
      {
        category: '종료 시간',
        content: props.tableDatas.endTime,
      },
      {
        category: '참가자',
        content: props.tableDatas.participants,
      },
      {
        category: '다운로드 유효 기간',
        content: getExpirationDate(),
      },
    ]);

    console.log("test",props.tableDatas, tableData.value)
    return {download,  meetingId, userName, props, tableData }
  }
})
</script>

<style scoped>
.result-container{
  border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	padding: 20px;
  margin: 10px auto;
}

@media screen and (min-width: 1200px) {
  .result-container {
    max-width: 1000px;
  }
}
</style>

