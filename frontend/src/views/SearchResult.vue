<template>
  <div class="container px-5">
    <div class="mb-4">
      <SearchFilterBar/>
    </div>

    <div class="row row-cols-1 row-cols-md-3 g-4">
      <div 
        v-for="meeting in meetings" 
        :key="meeting.meeting_id" 
        class="col"
      > 
        <MeetingCard :meeting="meeting" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { useRoute } from 'vue-router'
import SearchFilterBar from '@/components/SearchFilterBar.vue'
import MeetingCard from '@/components/MeetingCard.vue'

export default defineComponent({
  name: 'SearchResult',
  components: {
    SearchFilterBar,
    MeetingCard
  },
  setup () {
    const route = useRoute()

    const getMeetingQuery = {
      title: route.query.title,
      industry: route.query.industry,
      company: route.query.company
    }

    function fetchQuery () {
      getMeetingQuery.title = route.query.title,
      getMeetingQuery.industry = route.query.industry,
      getMeetingQuery.company = route.query.company
    }

    watch(route, () => {
      fetchQuery()
    })

    const meetings = [
      { 
        meeting_id: 1,
        title: '제목은 제목제목',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
      { 
        meeting_id: 2,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
      { 
        meeting_id: 3,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
      { 
        meeting_id: 4,
        title: '제목은 제목제목',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
      { 
        meeting_id: 5,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
      { 
        meeting_id: 6,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: ''
      },
    ]

    return { getMeetingQuery, meetings }
  }
})
</script>

<style>

</style>