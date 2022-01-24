<template>
  <div class="container">
    <h1>회의 제목</h1>
    <p>진행 시간</p>
    <p>참가자 </p>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="date" label="Date" width="180" />
      <el-table-column prop="name" label="Name" width="180" />
      <el-table-column prop="address" label="Address" />
  </el-table>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: "Result",
  setup() {
    const meetingId = 1
    const userName = '어쩌구'

    const meetingInfo = {
      meetingId: 1,
      title : '일이삼사오육칠팔구십일이삼사오육칠팔구십',
      startTime: ''
    }

    const tableData = [
      {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
      },
      {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
      },
      {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
      },
      {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
      },
    ]

    return { meetingId, userName, tableData }
  }
})
</script>

<style>

</style>