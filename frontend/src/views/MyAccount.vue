<template>
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
</template>

<script>
import axios from 'axios'
import { defineComponent, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import Vue3Lottie from 'vue3-lottie'
import Result from "./Result.vue"
import NoResults2JSON from '@/assets/lottie_json/no_results.json'
import NoResultsJSON from '@/assets/lottie_json/no-results_sitting.json'
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
      const getPreMeetings = function (pageNum) {        
        axios({
          url: `/users/${store.state.user.id}/meeting`,
          method: 'GET',
          params: {
            page: pageNum
          },
        }).then(res => {
            console.log(res)
            if (res.data.data) {
              tableDatas.value = res.data.data.content;
              table.value = res.data.data
            }
          }).catch(err => {
            console.log(err)
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
            console.log("미래", res)
            if (res.data.data) {
              futureMeetings.value = res.data.data.content;
            }
          }).catch(err => {
            console.log(err)
          })
      }

      const handleCurrentChange = function(){
        getPreMeetings(pageNumber.value)
      }

      onMounted(() => {
        getPreMeetings(1)
        getFutureMeetings()
      })
      return { NoResults2JSON, handleCurrentChange, table, tableDatas, futureMeetings, NoResultsJSON}
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
</style>