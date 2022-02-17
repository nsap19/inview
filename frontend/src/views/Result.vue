<template>
  <el-card class="result-container">
    <div class="d-flex flex-column justify-content-between">
      <p class="fs-5 fw-bold mb-0">{{ props.tableDatas.title }}</p>
    </div>

    <el-divider content-position="left" class="result mb-1">상세 내역</el-divider>
    <div class="d-flex flex-column justify-content-end align-items-end">
      <p class="m-0" style="font-size: 14px;">시작 | {{ props.tableDatas.startTime.slice(0, 16) }}</p>
      <p class="m-0" style="font-size: 14px;">종료 | {{ props.tableDatas.endTime.slice(0, 16) }}</p>
    </div>
    <div class="row p-1" v-for="(data, index) in tableData" :key="index">
      <div class="col-4 col-sm-3">
        <span>{{ data.category }}</span>
      </div>
      <div class="col" v-if="typeof(data.content) === 'object'">
          <span class="pe-2" v-for="(participant, index) in data.content" :key="index">
            {{ participant }}
          </span>
      </div>
      <div class="col" v-else>
        <span>{{ data.content }}</span>
      </div>
    </div>

    <el-divider content-position="left" class="result mb-1">결과 다운로드</el-divider>
    <p class="text-end mb-1 mt-2" style="font-size: 14px;">유효 기간 | {{ expirationDate }}</p>
    <div>
      <div>
        <el-button plain round type="primary" class="m-1" :disabled="expired" @click="download('VIDEO')">면접 영상</el-button>
        <el-button plain round type="primary" class="m-1" :disabled="expired" @click="download('CHAT')">채팅</el-button>
        <el-button plain round type="primary" class="m-1" :disabled="expired" @click="download('MEMO')">메모</el-button>
        <el-button plain round type="primary" class="m-1" :disabled="expired" @click="download('FILE')">공유 파일</el-button>
        <el-button plain round type="primary" class="m-1" :disabled="expired" @click="download('EVALUATION')">받은 면접 평가</el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
import { defineComponent, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import dayjs from 'dayjs'

import axios from "axios"

export default defineComponent({
  name: "Result",
   props: {
    tableDatas: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const store = useStore()
    const route = useRoute()

    const meetingId = route.params.meetingId
    const userName = route.params.userName
    const getExpirationDate = function () {
      if (props.tableDatas.endTime == null) {
        return ''
      } else {
        const date = new Date(props.tableDatas.endTime)
        date.setDate(new Date(props.tableDatas.endTime).getDate() + 7)
        return date.getFullYear() + "-"
              + ("0" + (1 + date.getMonth())).slice(-2) + "-" 
              + ("0" + date.getDate()).slice(-2) + " " 
              + date.getHours() + ":" + date.getMinutes()
      }
    }
    const expirationDate = getExpirationDate()

    const download = function (type){
      axios.get(`/users/${store.state.user.id}/meeting/${props.tableDatas.id}`).then(res=>{
        for ( let v of res.data.data.archives){
          // console.log(v.archiveType, type)
          if(v.archiveType == type){
            if( props.tableDatas.endTime != null && dayjs().isAfter(getExpirationDate())){
              alert("다운로드 유효 기간이 지났습니다.")
            } else {
              axios.get(`/download/meeting/${props.tableDatas.id}/users/${store.state.user.id}/${v.archiveId}?archive-type=${v.archiveType}`, {
                headers: 
                  {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                  }
                }).then(res=>{
                  const FILE = window.URL.createObjectURL(new Blob([res.data]));
                  const fileName = res.headers['content-disposition'].slice(22)
                  const fileUrl = document.createElement('a');
                  fileUrl.href = FILE;
                  fileUrl.setAttribute('download', fileName.slice(0, fileName.length - 1));
                  document.body.appendChild(fileUrl);
                  fileUrl.click();
                })
            }
            
            // var element = document.createElement('a');
            // element.setAttribute('href',v.path);
            // document.body.appendChild(element);
            // element.click();
          }
        }
      })
    }

    const tableData = ref([
      {
        category: '직군',
        content: props.tableDatas.industryName,
      },
      {
        category: '회사',
        content: props.tableDatas.companyNameList.length ? props.tableDatas.companyNameList : "상관 없음!",
      },
      // {
      //   category: '시작 시간',
      //   content: props.tableDatas.startTime,
      // },
      // {
      //   category: '종료 시간',
      //   content: props.tableDatas.endTime,
      // },
      {
        category: '참가자',
        content: props.tableDatas.participantNicknameList,
      },
      // {
      //   category: '다운로드 유효 기간',
      //   content: getExpirationDate(),
      // },
    ]);

    // console.log("test",props.tableDatas, tableData.value)
    const expired = dayjs().isAfter(getExpirationDate())
    return {download,  meetingId, userName, props, tableData, expirationDate, getExpirationDate, expired }
  }
})
</script>

<style scoped>
.result-container{
  border-radius: 10px;
	padding: 20px;
  margin: 10px auto;
  background-color: white;
}

@media screen and (min-width: 1200px) {
  .result-container {
    max-width: 1000px;
  }
}
</style>

