<template>
  <div class="container">
    <div class="mb-4 m-2">
      <SearchFilterBar/>
    </div>
    <div v-if="meetings.length > 0" class="m-2 mb-3 pt-2">
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
    <div v-else class="text-center p-5">
      검색 결과가 존재하지 않습니다.
    </div>
    <div v-loading="loading" class="loading">
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch, computed, onMounted, ref } from 'vue'
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

    const meetings = computed(() => store.state.searchResult)

    const loading = ref(false)
    watch(meetings, (newValue, oldValue) => {
      // console.log('old', oldValue, 'new', newValue)
      if (!oldValue.length || oldValue.length === newValue.length) {
        loading.value = false
      } else {
        loading.value = true
      }
    })

    let page = 2
    onMounted(() => {
      store.dispatch('search', {
        title: route.query.title,
        industry: route.query.industry,
        company: route.query.company,
        page: 1
      })
      document.addEventListener('scroll', (event) => {
        // console.log(event)
        const {scrollHeight, scrollTop, clientHeight} = document.documentElement
        // console.log(scrollHeight, scrollTop, clientHeight)
        if (scrollHeight - Math.round(scrollTop) === clientHeight) {
          store.dispatch('search', {
            title: route.query.title,
            industry: route.query.industry,
            company: route.query.company,
            page: page
          })
          console.log(meetings)
          page += 1
          console.log(page)
        }
      })
    })

    return { getMeetingQuery, meetings, loading }
  }
})
</script>

<style scoped>
.loading {
  margin: 0 auto;
  height: 80px;
}
</style>