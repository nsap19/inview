<template>
  <div class="banner">
    <div class="d-flex flex-column flex-sm-row justify-content-center align-items-center h-100">
      <div class="d-flex flex-column justify-content-center">
        <p class="fs-3 m-0">{{ this.$store.state.user.nickname }}님, 어쩌구저쩌구에 예정된</p>
        <p class="fs-3">참가 예정 면접 연습이 있습니다.</p>
        <el-button round plain size="large" @click="openCreateMeetingDialog=true" style="width: 90%">
          면접 연습방 만들기 <span class="px-2"></span> <i class="bi bi-chevron-right"></i>
        </el-button>
        <CreateMeeting v-model="openCreateMeetingDialog" />
      </div>
      <div style="display: inline-block;">
        <Vue3Lottie :animationData="LaptopJSON" :width="300" />
      </div>
    </div>
  </div>
</template>

<script>
import Vue3Lottie from 'vue3-lottie'
import LaptopJSON from '@/assets/lottie_json/togather.json'
import CreateMeeting from "@/components/NavBar/CreateMeeting.vue"
import { ref } from 'vue'

export default {
  name: 'AccountBanner',
  components: {
    Vue3Lottie, CreateMeeting
  },
  setup() {
    const openCreateMeetingDialog = ref(false)
    return {
      LaptopJSON, openCreateMeetingDialog
    }
  }
}
</script>

<style>
.banner {
  height: 250px;
  background: linear-gradient(to right, rgba(206, 229, 208, 0.5) 0%, rgba(243, 240, 215, 0.5) 20%, rgba(224, 192, 151, 0.5), 80%, rgba(255, 120, 120, 0.5) 100%), url(https://grainy-gradients.vercel.app/noise.svg);
}

@media screen and (max-width: 576px) {
  .banner {
    height: 400px;
  }
}
</style>