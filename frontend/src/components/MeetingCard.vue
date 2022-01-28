<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>{{ meeting.title }}</span>
        <el-button class="button" type="primary" plain>참가</el-button>
      </div>
    </template>
    <div v-for="(info, index) in infos" :key="index" class="text item">
      <div class="row">
        <div class="col">
          {{ info[0] }}
        </div>
        <div class="col">
          {{ info[1] }}
        </div>
      </div>
    </div>
  </el-card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'MeetingCard',
  props: ['meeting'],
  setup (props) {
    const infos = [
      ['직군', props.meeting.industryName],
      ['회사', props.meeting.companyNameList],
      ['최대 인원', props.meeting.userLimit],
      ['날짜', props.meeting.startTime],
    ]
    return { props, infos }
  },
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: transparent;
  border: none;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  /* width: 480px; */
}
</style>