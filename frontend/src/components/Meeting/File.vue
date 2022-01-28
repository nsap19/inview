<template>
  <div style="overflow-y: auto;">
    <!-- 다운로드 -->
    <div>
      <div v-for="(file, index) in files" :key="index" @click="downloadFile(file.id)" class="d-flex flex-row py-1 px-2">
        <el-button class="w-100">
          <span class="text-truncate">{{ index + 1 }} {{ file.archiveName.slice(33) }}</span>
        </el-button>
      </div>
    </div>

    <!-- 업로드 -->
    <div style="position: sticky; bottom: 0; background-color: lightgrey">
      {{typeof(upload.value)}}
      <el-upload
        ref="upload"
        class="upload-demo"
        action="http://localhost:8080/meeting/1/upload/"
        :limit="1"
        :on-exceed="handleExceed"
        :auto-upload="false"
      >
        <template #trigger>
          <el-button type="primary">select file</el-button>
        </template>
        <el-button class="ml-3" type="success" @click="submitUpload"
          >upload to server</el-button
        >
        <template #tip>
          <div class="el-upload__tip" style="color: red">
            limit 1 file, new file will cover the old file
          </div>
        </template>
      </el-upload>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import axios from 'axios'

export default defineComponent({
  name: "File",
  setup() {
    const upload = ref()

    const files = ref([])

    onMounted(() => {
      getFiles()
    })

    const getFiles = function () {      
      axios({
        url: 'http://localhost:8080/download/meeting/1/user/1',
        method: 'GET',
      })
      .then(res => {
        files.value = res.data.data
      })
    }

    const downloadFile = function (fileId: number) {
      axios({
        url: `http://localhost:8080/download/meeting/1/user/1/${fileId}?archiveType=file`, // File URL Goes Here
        method: 'GET',
        responseType: 'blob',
      })
      .then(res => {
        const FILE = window.URL.createObjectURL(new Blob([res.data]));
        const fileName = res.headers['content-disposition'].slice(22)

        const fileUrl = document.createElement('a');
        fileUrl.href = FILE;
        fileUrl.setAttribute('download', fileName.slice(0, fileName.length - 1));
        document.body.appendChild(fileUrl);
        fileUrl.click();
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
    return { upload, downloadFile, handleExceed, submitUpload, files }
  }
})

</script>

<style scoped>

</style>