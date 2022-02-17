<template>
  <AccountBanner/>
  <div class="meetings-wrapper">
    <div class="meetings mt-5">
      <p class="fs-4 mb-0">지난 면접 연습 기록</p>
      <div v-if="tableDatas.length == 0" class="d-flex flex-column align-items-center">
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
    <div class="meetings mt-5">
      <p class="fs-4 m-0">참가 예정 면접 연습</p>
      <div v-if="futureMeetings.length === 0">
        <p>참가 예정인 면접 연습 기록이 존재하지 않습니다.</p>
      </div>
      <div v-else>
        <MeetingCard />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { defineComponent,ref } from 'vue'
import { useStore } from 'vuex'
import Vue3Lottie from 'vue3-lottie'
import Result from "./Result.vue"
import NoResults2JSON from '@/assets/lottie_json/no_results2.json'
import AccountBanner from '@/components/AccountBanner.vue'
import MeetingCard from '@/components/MeetingCard.vue'

export default defineComponent({
	name: 'MyProjectList',
    components: {
      Result, Vue3Lottie, AccountBanner, MeetingCard
    },
    setup(){
      const store = useStore()
      const tableDatas = ref([])
      const pageNumber = ref(1)
      const table = ref([])
      const getPreMeetings = function (pageNum) {        
        axios.get(`/users/${store.state.user.id}/meeting?page=${pageNum}`,{
          headers: 
            {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            }
          }).then(res=>{
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
        axios.get(`/users/${store.state.user.id}/futureMeeting?page=${pageNum}`,{
          headers: 
            {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            }
          }).then(res=>{
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


      return { NoResults2JSON, handleCurrentChange, table, tableDatas, futureMeetings}
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