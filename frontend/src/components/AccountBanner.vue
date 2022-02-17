<template>
  <div class="banner">
    <div class="d-flex flex-column flex-sm-row justify-content-center align-items-center h-100" v-if="meeting">
      <div class="d-flex flex-column justify-content-center">
        <p class="fs-3 m-0">{{ meeting.startTiem }}에 예정된</p>
        <p class="fs-3">면접 연습이 있습니다.</p>
        <el-button round plain size="large" style="width: 90%" @click="clickEnter">
          입장하기 <span class="px-2"></span> <i class="bi bi-chevron-right"></i>
        </el-button>
        <CreateMeeting v-model="openCreateMeetingDialog" />
      </div>
      <div style="display: inline-block;">
        <Vue3Lottie :animationData="LaptopJSON" :width="300" />
      </div>
    </div>
    <div class="d-flex flex-column flex-sm-row justify-content-center align-items-center h-100" v-else>
      <div style="display: inline-block;">
        <Vue3Lottie :animationData="SearchJSON" class="search-image" />
      </div>
      <div class="d-flex flex-column justify-content-center search-content">
        <p class="search-text m-0">참가 예정 면접방이 없습니다. </p>
        <p class="search-text">새로운 면접방을 찾아보세요!</p>
        <el-button round plain size="large" style="width: 90%" @click="goToSearchResult">
          면접 연습 찾아보기 <span class="px-2"></span> <i class="bi bi-chevron-right"></i>
        </el-button>
        <CreateMeeting v-model="openCreateMeetingDialog" />
      </div>
    </div>
  </div>
</template>

<script>
import Vue3Lottie from 'vue3-lottie'
import LaptopJSON from '@/assets/lottie_json/togather.json'
import SearchJSON from '@/assets/lottie_json/laptop.json'
import CreateMeeting from "@/components/NavBar/CreateMeeting.vue"
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'AccountBanner',
  components: {
    Vue3Lottie, CreateMeeting
  },
  props: ['meeting'],
  setup(props) {
    const openCreateMeetingDialog = ref(false)
    const meeting = computed(() => props.meeting)
    const router = useRouter()
    const clickEnter = function () {
      store.dispatch('setMeeting', props.meeting.id)
      router.push({ name: 'Meeting', params: { meetingUrl: props.meeting.url } })
    }

    const goToSearchResult = () => {
      router.push({ name: 'Search', query: { title: '', industry: '', company: '' }})
      store.dispatch('search', { title: '', industry: '', company: '' })
    }
    return {
      LaptopJSON, openCreateMeetingDialog, meeting, SearchJSON,
      clickEnter, goToSearchResult,
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

.search-content {
  transform: translateX(-15%);
}

.search-image {
  width: 400px !important;
}

.search-text {
  font-size: calc(1.3rem + .6vw);
}

@media screen and (max-width: 576px) {
  .search-content {
    transform: translateX(0%);
  }
}

@media screen and (max-width: 750px) {
  .search-image {
    width: 300px !important;
  }
  .search-text {
    font-size: calc(1.275rem + .3vw);
  }
}
</style>