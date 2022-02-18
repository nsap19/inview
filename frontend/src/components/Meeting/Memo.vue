<template>
  <el-input
    v-model="textarea"
    type="textarea"
    placeholder="작성하신 메모는 면접 종료 후 다운로드 받을 수 있습니다."
    resize="none"
    class="textarea p-2"
  />
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue'
import { useStore } from 'vuex'
import axios from 'axios'

export default defineComponent({
  name: "Memo",
  props: {
    endSignal: Boolean,
  },
  setup(props, { emit }) {
    const textarea = ref('')

    const store = useStore()
    const userId = store.state.user.id
    const userNickname = store.state.user.nickname
    let meetingId = store.state.meeting.id
    watch(() => store.state.meeting, (newValue, oldValue) => {
      meetingId = newValue.id || oldValue.id
    })
    const makeHtml = function () {
      let htmlCode = "<!DOCTYPE html><html><head><link rel='stylesheet' type='text/css' href='https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css' /><style type='text/css' media='screen, print'>body { font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', sans-serif; }div { padding: 20px; border-radius: 10px; box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px; margin: 10px }h3 { padding: 0 5px; }p { padding: 0 10px; }</style></head><body><div><h1>"
      htmlCode += `${ userNickname }님의 메모</h1></div>`
      htmlCode += `<div><p>${ textarea.value }</p></div>`
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
      formData.append('file', data, 'memo.html');
      axios.post( `/meeting/${urlMeetingId}/upload?archive-type=memo&user-id=${userId}`,
        formData,
        {
          headers: 
          {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'multipart/form-data'
          }
        }
      )
      .then(res => {
      })
      .catch(err => {
      });
    }

    watch(()=>props.endSignal, () => {
      if (props.endSignal == true) {
        createHtmlFile(meetingId)
      }
    })
    return { textarea, createHtmlFile }
  }
})
</script>

<style scoped>
.textarea {
  height: 100%;
  width: 100%;
  -webkit-box-sizing: border-box; /* Safari/Chrome, other WebKit */
  -moz-box-sizing: border-box;    /* Firefox, other Gecko */
  box-sizing: border-box;         /* Opera/IE 8+ */
}
</style>