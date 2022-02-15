<template>
    <div class="wrap">
        <Result :tableDatas="v" v-for="(v, i) in tableDatas" :key="i"></Result>
        <div class="example-pagination-block">
            <el-pagination  background @current-change="handleCurrentChange" layout="prev, pager, next" :total="table.length" :page-size="4"></el-pagination>
        </div>
    </div>
</template>

<script lang="ts">

import axios from 'axios'

import { defineComponent, reactive,ref, onMounted } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

import Result from "./Result.vue"

export default defineComponent({
	name: 'MyProjectList',
    components: {
        Result
    },

    setup(){
        const store = useStore()
      const tableDatas = ref([])

      const table = ref([])
        onMounted(() => {
            axios.get(`/users/${store.state.user.id}/meeting`,{
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }).then(res=>{

                            table.value = res.data.data;
                            tableDatas.value = [];
                            
                            tableDatas.value = [];
                            for (let i = 0; i < 4; i++){
                                 if(table.value[i]){
                                     tableDatas.value.push(table.value[i])
                                    }              
                                 }
                            
            })

         })
         const handleCurrentChange = function(val: number){
            console.log("adad",table.value, tableDatas.value,val)
            tableDatas.value = [];
            for (let i = (val-1)*4; i < (val-1)*4 + 4; i++){
                if(table.value[i]){
                    tableDatas.value.push(table.value[i])
                }
            }
            console.log("tableDatas: ", tableDatas.value)
         }


        return {handleCurrentChange, table,tableDatas}
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