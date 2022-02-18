<template>
  <div class="h-100">
    <!-- pagination -->

    <!-- 디버그용 다운버튼 -->
    <!-- <button @click="createHtmlFile">다운</button>   -->
    <div class="p-2 d-flex justify-content-center">
      <el-pagination 
        background 
        layout="prev, pager, next" 
        :page-count="evaluations.length"
        :pager-count="5"
        :current-page="currentPage"
        @current-change="paginate"
        :small="true"
      >
      </el-pagination>
    </div>

    <!-- 문항과 답변 input -->
    <div v-for="(evaluation, index) in evaluations" :key="index">
      <div v-show="index + 1 === currentPage" class="p-2">
        <div>
          <p class="fw-bold ms-1">{{ index + 1 }}번 면접 문항</p>
          <p class="ms-2"> {{evaluation.question}} </p>
          
        </div>
        <div class="textarea">
          <el-input
            v-model="evaluation.content.value"
            :autosize="{ minRows: 5, maxRows: 10 }"
            resize="none"
            type="textarea"
            placeholder="평가 내용을 적어주세요"
            class="textarea"
          ></el-input>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, Ref, watch } from 'vue'
import { useStore } from 'vuex'
import axios from 'axios'

export default defineComponent({
  name: 'Evaluation',
  props: {
    endSignal: Boolean,
    startSignal: Boolean,
    participantNickname: String
  },
  setup(props) {
    const currentPage = ref(1)
    const paginate = function (page: number) {
      currentPage.value = page
    }
    const questions = [
      {
        pk: 1,
        content: '자기소개를 해보세요.',
      },
      {
        pk: 2,
        content: '우리 회사에 지원한 이유가 무엇인가요?',
      },
      {
        pk: 3,
        content: '본인의 장/단점은 무엇인가요?',
      },
      {
        pk: 4,
        content: '왜 우리가 지원자님을 뽑아야 하나요?',
      },
      {
        pk: 5,
        content: '자신의 인생에서 실패했던 경험을 말해 보세요.',
      },
      {
        pk: 6,
        content: '인생에서 가장 열정적이었던 순간은 언제였나요?',
      },
      {
        pk: 17,
        content: '갈등한 경험과 그 해결 방법을 말해 보세요.'
      },
      {
        pk: 7,
        content: '입사 후 포부가 무엇인가요?',
      },
      {
        pk: 8,
        content: '회사를 알게 된 계기가 무엇인가요?',
      },
      {
        pk: 9,
        content: '본인이 우리 회사에 어떤 도움을 줄 수 있을 것이라 생각하나요?',
      },
      {
        pk: 11,
        content: '입사 후 하고 싶은 업무가 무엇인가요?',
      },
      {
        pk: 10,
        content: '희망 직무에 지원한 이유가 무엇인가요?',
      },
      {
        pk: 19,
        content: '희망 직무에 지원하기 위해 어떤 노력을 하셨나요?'
      },
      {
        pk: 13,
        content: '직무와 관련하여 최근 관심 있는 이슈는 무엇인지 설명해 보세요.'
      },
      {
        pk: 12,
        content: '직무와 관련하여 자신의 강점은 무엇인가요?',
      },
      {
        pk: 14,
        content: '만약 직장상사가 부당한 지시를 할 경우에 어떻게 대처하겠습니까?'
      },
      {
        pk: 15,
        content: '회사 근무를 하면서 가장 중요하다고 생각하는 것이 무엇인가요?'
      },
      {
        pk: 18,
        content: '10년 후 본인의 모습을 구체적으로 말해 보세요.'
      },
      {
        pk: 16,
        content: '인생관이나 좌우명이 있다면 그 이유를 말해 보세요.'
      },
      {
        pk: 20,
        content: '마지막으로 하고 싶은 말 있나요?',
      },
    ]

    const evaluations: { pk: number, question: string, content: Ref<string> }[] = []
    questions.forEach((element) => {
      evaluations.push(
        {
          pk: element.pk,
          question: element.content,
          content: ref('') 
        }
      )
    })

    const store = useStore()
    let userNickname = store.state.user.nickname
    let meetingId = store.state.meeting.id
    const startSignal = computed(() => props.startSignal)
    watch(() => store.state.meeting, (newValue, oldValue) => {
      userNickname = userNickname || store.state.user.nickname
      meetingId = newValue.id || oldValue.id
      if( !newValue.id && props.startSignal ) {
        createHtmlFile(meetingId)
      }
    })

    const makeHtml = function () {
      let htmlCode = "<!DOCTYPE html><html><head><link rel='stylesheet' type='text/css' href='https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css' /><style type='text/css' media='screen, print'>body { font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif; }div { padding: 20px; border-radius: 10px; box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px; margin: 10px }h3 { padding: 0 5px; }p { padding: 0 10px; }</style></head><body><div><h1>"
      htmlCode += `${ userNickname }님의 평가</h1></div>`
      let i = 1
      evaluations.forEach(element => {
        htmlCode += `<div><h3>${i}. ${element.question}</h3><hr>`
        htmlCode += `<p>${ element.content.value }</p></div>`
        i++
      })
      htmlCode += "</body></html>"
      return [htmlCode]
    }

    const createHtmlFile = function (urlMeetingId: number) {
      var htmlCode = null;
      var data = new Blob(makeHtml(), {type: 'text/html'}); 
      if (htmlCode !== null) {  
        window.URL.revokeObjectURL(htmlCode);  
      }  
      htmlCode = window.URL.createObjectURL(data);  
      let formData = new FormData();
      formData.append('file', data, `from_${userNickname}_to_${props.participantNickname}_evaluation.html`);
      axios.post( `/meeting/${urlMeetingId}/upload?archive-type=evaluation`,
        formData,
        {
          headers: 
          {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'multipart/form-data'
          }
        }
      ).then(res => {
      })
      .catch(err => {
      });
      
      // 디버그용 파일 다운로드
      // var a = document.createElement('a');
      // a.download = 'fileName';
      // a.href = htmlCode;
      // a.click();
    }

    // watch(()=>props.endSignal, () => {
    //   console.log("평가에서 신호 안받음?", props.endSignal)
    //   if (props.endSignal == true) {
    //     // createHtmlFile()
    //     console.log('평가에서 종료신호 받음')
    //     createHtmlFile(meetingId)
    //   }
    // })
    return { questions, evaluations, currentPage, paginate, createHtmlFile }
  }
})
</script>

<style scoped>

</style>