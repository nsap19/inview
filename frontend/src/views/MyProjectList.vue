<template>
    <div class="wrap">
        <template v-if="tableDatas.length == 0">
        <Vue3Lottie :animationData="NoResults2JSON" :width="300"  style="margin: 20px 0px 0px 0px"/>
        <p style="margin: 0 auto 50px auto">지난 면접 연습 목록이 존재하지 않습니다.</p>
        </template>
        <template v-else>
        <Result :tableDatas="v" v-for="(v, i) in tableDatas" :key="i"></Result>
        <div class="example-pagination-block">
            <el-pagination  background @current-change="handleCurrentChange" layout="prev, pager, next" :total="table.totalElements" :page-size="6"></el-pagination>
        </div>
        </template>
    </div>
</template>

<script>
import axios from 'axios'
import { defineComponent,ref } from 'vue'
import { useStore } from 'vuex'
import Vue3Lottie from 'vue3-lottie'
import Result from "./Result.vue"
import NoResults2JSON from '@/assets/lottie_json/no_results2.json'

export default defineComponent({
	name: 'MyProjectList',
    components: {
        Result,Vue3Lottie
    },

    setup(){
        const store = useStore()
        const tableDatas = ref([])

        const pageNumber = ref(1)

        const table = ref([])
            axios.get(`/users/${store.state.user.id}/meeting?page=${pageNumber.value}`,{
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }).then(res=>{
                            if(res.data.data){
                                tableDatas.value = res.data.data.content;
                                table.value = res.data.data
                            }
                            
            })

         const handleCurrentChange = function(val){
             pageNumber.value = val
              axios.get(`/users/${store.state.user.id}/meeting?page=${pageNumber.value}`,{
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }).then(res=>{

                           tableDatas.value = res.data.data.content;
                           table.value = res.data.data
                            
            })
             
         }


        return {NoResults2JSON,handleCurrentChange, table,tableDatas}
    }


});


</script>

<style>
.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}
.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
.wrap {
    max-width: 1000px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    margin: 0 auto;
}
</style>