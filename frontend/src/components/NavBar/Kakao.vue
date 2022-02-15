<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <img src="@/assets/kakao_login_medium_wide.png" class="w-100" @click="kakoLoginBtn">
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  // mounted: function () {
  //   this.$nextTick(function () {
  //     // Code that will run only after the
  //     // entire view has been rendered
  //     if (this.kakaoInfo != "") {
  //       var data = JSON.parse(this.kakaoInfo);

  //       alert("카카오로그인 성공 \n accessToken : " + data["accessToken"]);
  //       alert(
  //         "user : \n" +
  //           "email : " +
  //           data["email"] +
  //           "\n nickname : " +
  //           data["nickname"]
  //       );
  //     }
  //   });
  // },
  data() {
    return {
      kakaoInfo: "",
      KAKAO_API_KEY: process.env.VUE_APP_KAKAO_API_KEY,
      KAKAO_REDIRECT_URI: process.env.VUE_APP_KAKAO_REDIERCT_URI,
    };
  },
  methods: {
    kakaoLogin() {
      axios
        .get("/oauth/login/kakao")
        .then((res) => {
          console.log(res);
          // location.href = res;
        })
        .catch();
    },

    kakaoLogin2() {
      axios
        .get("/oauth/kakao")
        .then((res) => {
          console.log(res);
        })
        .catch();
    },

    kakoLoginBtn() {
      var self = this; //다른 method 사용하기 위해 사용
      window.Kakao.init("08176443547157e6d25360abeded1e0b"); //처음에 카카오 로그인 세션을 없애준다.
      if (window.Kakao.Auth.getAccessToken()) {
        window.Kakao.API.request({
          url: "/v1/user/unlink",
          success: function (response) {
            console.log(response);
          },
          fail: function (error) {
            console.log(error);
          },
        });
        window.Kakao.Auth.setAccessToken(undefined);
      }

      //로그인 시도
      window.Kakao.Auth.login({
        success: function (response) {
          window.Kakao.API.request({
            url: "/v2/user/me",
            data: { property_keys: ["kakao_account.email"] },
            success: async function (response) {
              await axios.post('http://localhost:8080/api/oauth/login/kakao', response)
              .then(res=>{
                console.log(res.data)
                localStorage.setItem("token", res.data.token);
                this.$store.dispatch('setUser', { nickname: res.data.nickname, id: res.data.userId });
              }).catch(e=>{
                console.log(e);
              })
              // console.log(response);
              // const kakaoemail = response.kakao_account.email;
              // console.log(response.kakao_account.email);
            },
            fail: function (error) {
              console.log(error);
            },
          });
        },
        fail: function (error) {
          console.log(error);
        },
      });
    },
  },
};
</script>

<style></style>
