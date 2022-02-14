<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
    
      
        <img src="@/assets/kakao_login.png" @click="Login()" style="cursor:pointer">
      
    
    </div>
  </div>
</template>

<script>
import axios from "axios"
export default {
  name: "App",
  created() {
    console.log(process.env); // true
    console.log(process.env.VUE_APP_KAKAO_API_KEY);
  },
  data() {
    return {
      KAKAO_API_KEY: process.env.VUE_APP_KAKAO_API_KEY,
      KAKAO_REDIRECT_URI: process.env.VUE_APP_KAKAO_REDIERCT_URI,
    };
  },
  methods:{
    Login(){
      axios.get(`https://kauth.kakao.com/oauth/authorize?client_id=${this.KAKAO_API_KEY}&redirect_uri=${this.KAKAO_REDIRECT_URI}&response_type=code`).then((data)=>{
        console.log(data)
      })
    }
  }
};
</script>

<style></style>
