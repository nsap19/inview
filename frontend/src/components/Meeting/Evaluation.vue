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
    <div v-for="(evaluation, index) in evaluations" :key="evaluation.pk">
      <div v-show="index + 1 === currentPage" class="p-2">
        <div>
          <p class="fw-bold">{{ index + 1 }}번 면접 문항</p>
          <p> {{evaluation.question}} </p>
          
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
        content: '모든 국민은 법률이 정하는 바에 의하여 국방의 의무를 진다.',
      },
      {
        pk: 2,
        content: '법관은 탄핵 또는 금고 이상의 형의 선고에 의하지 아니하고는 파면되지 아니하며, 징계처분에 의하지 아니하고는 정직·감봉 기타 불리한 처분을 받지 아니한다.',
      },
      {
        pk: 3,
        content: '모든 국민은 통신의 비밀을 침해받지 아니한다. 모든 국민은 보건에 관하여 국가의 보호를 받는다.',
      },
      {
        pk: 4,
        content: '정부는 회계연도마다 예산안을 편성하여 회계연도 개시 90일전까지 국회에 제출하고, 국회는 회계연도 개시 30일전까지 이를 의결하여야 한다.',
      },
      {
        pk: 5,
        content: '군인·군무원·경찰공무원 기타 법률이 정하는 자가 전투·훈련등 직무집행과 관련하여 받은 손해에 대하여는 법률이 정하는 보상외에 국가 또는 공공단체에 공무원의 직무상 불법행위로 인한 배상은 청구할 수 없다.',
      },
      {
        pk: 6,
        content: '대통령이 궐위되거나 사고로 인하여 직무를 수행할 수 없을 때에는 국무총리, 법률이 정한 국무위원의 순서로 그 권한을 대행한다.',
      },
      {
        pk: 7,
        content: '대통령은 국무회의의 의장이 되고, 국무총리는 부의장이 된다.',
      },
      {
        pk: 8,
        content: '헌법재판소 재판관의 임기는 6년으로 하며, 법률이 정하는 바에 의하여 연임할 수 있다.',
      },
      {
        pk: 9,
        content: '국방상 또는 국민경제상 긴절한 필요로 인하여 법률이 정하는 경우를 제외하고는, 사영기업을 국유 또는 공유로 이전하거나 그 경영을 통제 또는 관리할 수 없다.',
      },
      {
        pk: 10,
        content: '모든 국민의 재산권은 보장된다. 그 내용과 한계는 법률로 정한다.',
      },
      {
        pk: 11,
        content: '훈장등의 영전은 이를 받은 자에게만 효력이 있고, 어떠한 특권도 이에 따르지 아니한다.',
      },
      {
        pk: 12,
        content: '대통령이 궐위된 때 또는 대통령 당선자가 사망하거나 판결 기타의 사유로 그 자격을 상실한 때에는 60일 이내에 후임자를 선거한다.',
      },
      {
        pk: 13,
        content: '국회의원과 정부는 법률안을 제출할 수 있다. 대통령은 조약을 체결·비준하고, 외교사절을 신임·접수 또는 파견하며, 선전포고와 강화를 한다.'
      },
      {
        pk: 14,
        content: '감사원은 세입·세출의 결산을 매년 검사하여 대통령과 차년도국회에 그 결과를 보고하여야 한다.',
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