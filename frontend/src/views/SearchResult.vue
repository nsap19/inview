<template>
  <div class="container">
    <div class="mb-4">
      <SearchFilterBar/>
    </div>
    <div v-infinite-scroll="load" class="row row-cols-1 row-cols-md-3 g-4">
      <div 
        v-for="meeting in meetings" 
        :key="meeting.id" 
        class="col"
      > 
        <MeetingCard :meeting="meeting" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import SearchFilterBar from '@/components/SearchFilterBar/SearchFilterBar.vue'
import MeetingCard from '@/components/MeetingCard.vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'SearchResult',
  components: {
    SearchFilterBar,
    MeetingCard
  },
  setup () {
    const route = useRoute()
    const store = useStore()

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
        title: '일이삼사오육칠팔구십일이삼사오육칠팔구십',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '2020-01-01 13:53',
        endTime: '2020-01-01 14:59',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: '123'
      },
      { 
        meeting_id: 2,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 3,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 4,
        title: '제목은 제목제목',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 5,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 6,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 7,
        title: '제목은 제목제목',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 8,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 9,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 10,
        title: '제목은 제목제목',
        industry: 'IT',
        company: '',
        userLimit: 6,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 11,
        title: '두번째제목은 제목제목',
        industry: '반도체',
        company: '삼성전자',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
      { 
        meeting_id: 12,
        title: '세번째제목은 제목제목',
        industry: '공기업',
        company: '한전',
        userLimit: 3,
        startTime: '202201141500',
        endTime: '',
        url: '',
        participants : ['참가자1', '참가자2', '참가자3'],
        password: ''
      },
    ]

    // const meetings = computed(() => store.state.searchResult)
    // const meetingsLength = computed(() => store.state.searchResult.length)
    // console.log(meetingsLength)
    const count = ref(1)
    const load = () => {
      count.value += 3
    }
    return { getMeetingQuery, meetings, count, load }
  }
})
</script>

<style>

</style>