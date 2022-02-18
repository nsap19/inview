<template>
  <div>
    <Banner/> 
    <div class="mt-5 mb-1 m-3">
      <SearchFilterBar/>
    </div>
    <div class="result">
      <div v-if="meetings.length > 0" class="result-cards">
        <div class="mx-3">
          <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <div 
              v-for="meeting in meetings" 
              :key="meeting.id" 
              class="col"
            > 
              <MeetingCard :meeting="meeting" />
            </div>
          </div>
        </div>
      </div>
      <div v-else class="text-center p-5 d-flex flex-column align-items-center">
        <div>
          <Vue3Lottie :animationData="NoResultsJSON" :width="300" />
        </div>
        <p>검색 결과가 존재하지 않습니다.</p>
      </div>
      <div ref="createButton" class="create-button" v-if="this.$store.state.user.id">
        <el-button class="m-1" :icon="Plus" round plain @click="openCreateMeetingDialog = true" size="large"
        >방 만들기</el-button>   
      </div>
      <CreateMeeting v-model="openCreateMeetingDialog" />
    </div>
  </div>
</template>

<script>
import { defineComponent, watch, computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import SearchFilterBar from '@/components/SearchFilterBar/SearchFilterBar.vue'
import MeetingCard from '@/components/MeetingCard.vue'
import { useStore } from 'vuex'
import Banner from '@/components/Banner.vue'
import CreateMeeting from "@/components/NavBar/CreateMeeting.vue"
import { Plus } from '@element-plus/icons-vue'
import Vue3Lottie from 'vue3-lottie'
import NoResultsJSON from '@/assets/lottie_json/no_results.json'

export default defineComponent({
  name: 'SearchResult',
  components: {
    SearchFilterBar,
    MeetingCard, Banner, CreateMeeting, Vue3Lottie
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

    const meetings = computed(() => store.state.searchResult)

    const loading = ref(false)
    watch(meetings, (newValue, oldValue) => {
      if (!oldValue.length || oldValue.length === newValue.length) {
        loading.value = false
      } else {
        loading.value = true
      }
    })

    const makeQuery = function (page) {
      let query = {page: page}
      if (route.query.title && route.query.title.trim()) {
        query['title'] = route.query.title.trim()
      }
      if (route.query.industry && route.query.industry.trim()) {
        query['industryList'] = route.query.industry.trim()
      }
      if (route.query.company && route.query.company.trim()) {
        query['companyList'] = route.query.company.trim()
      }
      return query
    }

    let page = 2
    onMounted(() => {
      store.dispatch('search', makeQuery(1))
      document.addEventListener('scroll', (event) => {
        const {scrollHeight, scrollTop, clientHeight} = document.documentElement
        if (scrollHeight - Math.round(scrollTop) === clientHeight) {
          store.dispatch('search', makeQuery(page))
          page += 1
        }
      })
    })
    const openCreateMeetingDialog = ref(false)
    const createButton = ref(null)
    return { getMeetingQuery, meetings, loading, openCreateMeetingDialog, Plus, NoResultsJSON, createButton }
  }
})
</script>

<style scoped>
.loading {
  margin: 0 auto;
  height: 80px;
}

.result {
  background-color: #F9F9F9;
  padding-top: 2rem;
}

.create-button {
  position: sticky;
  bottom: 10px;
  background-color: transparent;
  text-align: right;
  margin-right: 20px;
}

.result-cards {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px 0;
}
</style>