<template>
  <div style="overflow-y: auto;">
    <!-- 업로드 -->
    <div class="p-2">
      <div class="d-flex flex-row justify-content-between align-items-center">
        <span>공유할 파일을 올리고 전송을 눌러주세요</span>
        <el-button v-on:click="submitUpload">전송</el-button>
      </div>
      <div class="d-flex flex-row justify-content-center">
        <el-upload
          ref="upload"
          :action="actionUrl"
          :limit="1"
          :on-exceed="handleExceed"
          :headers="headers"
          :on-success="onSuccess"
          drag
          :auto-upload="false"
          class="d-flex flex-column justify-content-center mt-2 align-items-center w-100"
        >
          <el-icon class="el-icon--upload"><i class="bi bi-cloud-arrow-up-fill"></i></el-icon>
          <div class="el-upload__text">
            파일을 드래그 하거나 <em>클릭하세요</em>
          </div>
        </el-upload>
      </div>
    </div>
    
    <!-- 다운로드 -->
    <div>
      <div v-for="(file, index) in files" :key="index" @click="downloadFile(file.id)" class="d-flex flex-row justify-content-center py-1 px-2">
        <el-button class="w-75">
          <span class="text-truncate">{{ index + 1 }}. {{ file.archiveName.slice(10, 30) }}</span>
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
      axios({
        url: `/download/meeting/${meetingId.value}/users/${userId.value}`,
        method: 'GET',
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      }).then(res => {
        files.value = res.data.data.filter(function (file: {archiveName: string, archiveType: string, id: number, path: string}) {
          if (file.archiveType === "FILE") {
            return true
          } else {
            return false
          }
        })
      }).catch(err => {
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
      })
    }
    const handleExceed = (files: FileList) => {
      upload.value.clearFiles()
      upload.value.handleStart(files[0])
    }
    const submitUpload = () => {
      upload.value.submit()
    }

    const file = ref()
    const handleFileUpload = async() => {
    }

    const submitFile = function (){
      let formData = new FormData();
      formData.append('file', file.value.files[0]);
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
        getFiles()
      })
      .catch(err => {
      });
    }

    const headers = { Authorization: `Bearer ${localStorage.getItem("token")}` }
    const actionUrl = `${process.env.VUE_APP_API_URL}/meeting/${store.state.meeting.id}/upload?archive-type=file`
    const onSuccess = function () {
      getFiles()
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