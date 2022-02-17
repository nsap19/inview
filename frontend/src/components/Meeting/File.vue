<template>
  <div style="overflow-y: auto;">
    <!-- 업로드 -->
    <div class="p-2">
      <div class="d-flex flex-row justify-content-between align-items-center">
        <span>공유할 파일을 올려주세요.</span>
        <el-button v-on:click="submitFile()">전송</el-button>
      </div>
      <input type="file" ref="file" multiple="multiple" v-on:change="handleFileUpload()" class="file-input"/>
      <div class="d-flex flex-row justify-content-center">
        <el-upload
          ref="upload"
          :action="actionUrl"
          :limit="1"
          :on-exceed="handleExceed"
          :auto-upload="false"
          :headers="headers"
          :on-success="onSuccess"
          drag
          class="d-flex flex-column justify-content-center"
        >
          <!-- <template #trigger>
            <el-button type="primary">select file</el-button>
          </template> -->
          <el-icon class="el-icon--upload"><i class="bi bi-cloud-arrow-up-fill"></i></el-icon>
          <div class="el-upload__text">
            파일을 드래그 하거나 <em>클릭하세요</em>
          </div>
          <template #tip>
            <el-button class="mt-3" type="primary" plain round @click="submitUpload"
              >업로드</el-button
            >
            <!-- <div class="el-upload__tip" style="color: red">
              limit 1 file, new file will cover the old file
            </div> -->
          </template>
        </el-upload>
      </div>
    </div>
    
    <!-- 다운로드 -->
    <div>
      <div v-for="(file, index) in files" :key="index" @click="downloadFile(file.id)" class="d-flex flex-row py-1 px-2">
        <el-button class="w-100">
          <span class="text-truncate">{{ index + 1 }}. {{ file.archiveName.slice(10) }}</span>
        </el-button>
      </div>
    </div>

  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useStore } from 'vuex'

export default defineComponent({
  name: "File",
  setup() {
    const store = useStore()
    const meetingId = computed(() => store.state.meeting.id)
    const userId = computed(() => store.state.user.id)
    const upload = ref()

    const files = ref([])

    onMounted(() => {
      getFiles()
    })

    const getFiles = function () {      
      console.log('get Files')
      axios({
        url: `/download/meeting/${meetingId.value}/users/${userId.value}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      }).then(res => {
        console.log(meetingId.value, userId.value)
        console.log(res)
        files.value = res.data.data.filter(function (file: {archiveName: string, archiveType: string, id: number, path: string}) {
          if (file.archiveType === "FILE") {
            return true
          } else {
            return false
          }
        })
      }).catch(err => {
        console.log(err)
      })
    }

    const downloadFile = function (fileId: number) {
      axios({
        url: `/download/meeting/${meetingId.value}/users/${userId.value}/${fileId}?archive-type=file`, // File URL Goes Here
        method: 'GET',
        responseType: 'blob',
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      })
      .then(res => {
        const FILE = window.URL.createObjectURL(new Blob([res.data]));
        const fileName = res.headers['content-disposition'].slice(22)

        const fileUrl = document.createElement('a');
        fileUrl.href = FILE;
        fileUrl.setAttribute('download', fileName.slice(0, fileName.length - 1));
        document.body.appendChild(fileUrl);
        fileUrl.click();
      }).catch(err => {
        console.log(err)
        console.log(err.response)
      })
    }
    const handleExceed = (files: FileList) => {
      upload.value.clearFiles()
      upload.value.handleStart(files[0])
    }
    const submitUpload = () => {
      upload.value.submit()

      // 파일 전송 후 파일 목록이 사라지지 않아서 작성한 코드
      // 오류가 발생하는데 이후 해결하기로...
      // upload.value.uploadFiles = []
    }

    const file = ref()
    const handleFileUpload = async() => {
        // debugger;
        console.log("selected file",file.value.files[0])
        //Upload to server
    }

    const submitFile = function (){
      let formData = new FormData();
      formData.append('file', file.value.files[0]);
      for (let value of formData.values()) {
        console.log(value);
      }
      axios.post( `/meeting/${meetingId.value}/upload?archive-type=file`,
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
        console.log('SUCCESS!!');
        console.log(res)
        getFiles()
      })
      .catch(err => {
        console.log('FAILURE!!');
        console.log(err.response)
      });
    }

    const headers = { Authorization: `Bearer ${localStorage.getItem("token")}` }
    const actionUrl = `${process.env.VUE_APP_API_URL}meeting/${store.state.meeting.id}/upload/`
    const onSuccess = function () {
      alert("!!")
    }
    return { upload, downloadFile, handleExceed, submitUpload, files, headers, file, handleFileUpload, submitFile, actionUrl, onSuccess }
  }
})

</script>

<style scoped>
.file-input {
  background-color: white;
}
</style>