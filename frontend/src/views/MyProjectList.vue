<template>
    <div class="wrap">
        <Result :tableDatas="v" v-for="(v, i) in tableDatas" :key="i"></Result>
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
        onMounted(() => {
            axios.get(`/users/${store.state.user.id}/meeting`,{
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }).then(res=>{
                            tableDatas.value = res.data.data
            })

         })


        return {tableDatas}
    }


});


</script>

<style>

.wrap {
    max-width: 1000px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    margin: 0 auto;
}
</style>