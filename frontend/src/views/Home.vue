<template>
  <div class="home container text-center">
    <img alt="INVIEW logo" src="../assets/logo.png" class="w-50 m-4">
    <div class="py-4 search-bar">
      <SearchBar/>
    </div>
  </div>
  <div class="container recent-result">
    <h4>최근 개설 방</h4>
    <div class="row row-cols-1 row-cols-md-3 g-4">
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
import { defineComponent } from 'vue';
import SearchBar from '@/components/SearchBar.vue';
import MeetingCard from '@/components/MeetingCard.vue'

export default defineComponent({
  name: 'Home',
  components: {
    SearchBar, MeetingCard
  },
  setup() {
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

    return { meetings }
  }
});
</script>

<style scoped>
.home {
  margin: 20vw auto;
}

.search-bar {
  width: 400px;
  margin: 0 auto;
  transform: scale(1.3);
}

.recent-result {
  margin: 10px auto;
}

@media screen and (min-width: 1200px) {
  .recent-result {
    max-width: 1000px;
  }
}
</style>