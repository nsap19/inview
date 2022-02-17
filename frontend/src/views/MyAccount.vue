<template>
  <div class="page-load-wrapper" v-if="loadComplete !== 2">
    <div class="page-load">
      <Vue3Lottie :animationData="PageLoadJSON" :width="200" />
    </div>
  </div>
  <div v-show="loadComplete === 2">
    <AccountBanner :meeting="futureMeetings.length !== 0 ? futureMeetings[0] : false" />
    <div class="meetings-wrapper">
      <div class="meetings mt-5">
        <div class="row justify-content-between">
          <p class="fs-4 mb-0 col-12 col-sm-6">지난 면접 연습 기록</p>
          <div class="col-12 col-sm-6 d-flex flex-row justify-content-end align-items-end" v-if="tableDatas.length !== 0">
            <el-tooltip
              effect="customized"
              content="기록은 모든 참가자가 종료한 뒤 조회 가능합니다."
              placement="left"
            > 
                <el-button round class="d-none d-sm-block">기록이 없는 경우</el-button>
            </el-tooltip>
            <el-tooltip
              effect="customized"
              content="기록은 모든 참가자가 종료한 뒤 조회 가능합니다."
              placement="bottom-end"
            > 
                <el-button round class="d-block d-sm-none">기록이 없는 경우</el-button>
            </el-tooltip>
          </div>
        </div>
        <div v-if="tableDatas.length === 0" class="d-flex flex-column align-items-center">
          <Vue3Lottie :animationData="NoResults2JSON" :width="300"  style="margin: 20px 0px 0px 0px"/>
          <p style="margin: 0 auto 50px auto">지난 면접 연습 기록이 존재하지 않습니다.</p>
        </div>
        <div class="p-2" v-else>
          <div class="row row-cols-md-2 row-cols-1 gx-4">
            <div v-for="(v, i) in tableDatas" :key="i">
              <Result :tableDatas="v" ></Result>
            </div>
          </div>
          <div class="d-flex flex-row justify-content-center my-3">
              <el-pagination background @current-change="handleCurrentChange" layout="prev, pager, next" :total="table.totalElements" :page-size="6"></el-pagination>
          </div>
        </div>
      </div>
    </div>
    <div class="meetings-wrapper">
      <div class="meetings mt-5 pb-5" style="min-height: 400px">
        <p class="fs-4">참가 예정 면접 연습</p>
        <div v-if="futureMeetings.length === 0" class="d-flex flex-column align-items-center mt-5">
          <Vue3Lottie :animationData="NoResultsJSON" :width="300"  style="margin: 20px 0px 0px 0px"/>
          <p class="mt-3">참가 예정 면접 연습방이 존재하지 않습니다.</p>
        </div>
        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4" v-else>
          <div v-for="(meeting, index) in futureMeetings" :key="index" class="col">
            <MeetingCard :meeting="meeting" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { defineComponent, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import Vue3Lottie from 'vue3-lottie'
import Result from "./Result.vue"
import NoResults2JSON from '@/assets/lottie_json/no_results.json'
import NoResultsJSON from '@/assets/lottie_json/no-results_sitting.json'
import PageLoadJSON from '@/assets/lottie_json/page_load.json'
import AccountBanner from '@/components/AccountBanner.vue'
import MeetingCard from '@/components/MeetingCard.vue'

export default defineComponent({
	name: 'MyAccount',
    components: {
      Result, Vue3Lottie, AccountBanner, MeetingCard
    },
    setup(){
      const store = useStore()
      const tableDatas = ref([])
      const pageNumber = ref(1)
      const table = ref([])
      const loadComplete = ref(0)
      const getPreMeetings = function (pageNum) {        
        axios({
          url: `/users/${store.state.user.id}/meeting`,
          method: 'GET',
          params: {
            page: pageNum
          },
        }).then(res => {
            loadComplete.value++
            if (res.data.data) {
              tableDatas.value = res.data.data.content;
              table.value = res.data.data
            }
          }).catch(err => {
          })
      }

      const futureMeetings = ref([])
      const getFutureMeetings = function () {
        axios.get(`/users/${store.state.user.id}/futureMeeting`,{
          headers: 
            {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            }
          }).then(res=>{
            loadComplete.value++
            if (res.data.data) {
              futureMeetings.value = res.data.data.content;
            }
          }).catch(err => {
          })
      }

      const handleCurrentChange = function(){
        getPreMeetings(pageNumber.value)
      }

      onMounted(() => {
        getPreMeetings(1)
        getFutureMeetings()
      })
      return { 
        NoResults2JSON, handleCurrentChange, table, tableDatas, futureMeetings, NoResultsJSON,
        loadComplete, PageLoadJSON
      }
    }


});


</script>

<style scoped>
.meetings {
  max-width: 1000px;
  margin: 0 auto;
  padding: 10px;
}

.meetings-wrapper {
  background-color: #F9F9F9;
  margin-top: 2rem;
}

.page-load {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 7rem auto;
  background-color: #F9F9F9;
}

.page-load-wrapper {
  height: 100% ;
  background-color: #F9F9F9;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  position: absolute;
  z-index: 2;
  right: 0;
  left: 0;
  top: 0;
}
</style>