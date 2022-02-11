<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <a :href="`https://kauth.kakao.com/oauth/authorize?client_id=`+KAKAO_API_KEY+`&redirect_uri=`+KAKAO_REDIRECT_URI+`&response_type=code`">
      
      
        <img src="@/assets/kakao_login.png">
      
      </a>

    </div>
  </div>
</template>

<script>
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
};
</script>

<style></style>
