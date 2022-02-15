<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <a
        :href="
          `https://kauth.kakao.com/oauth/authorize?client_id=` +
          KAKAO_API_KEY +
          `&redirect_uri=` +
          KAKAO_REDIRECT_URI +
          `&response_type=code`
        "
      >
        <img src="@/assets/kakao_login_medium_wide.png" class="w-100">
      </a>
    </div>
    <ul>
      <li @click="kakaoLogin">
        <a href="javascript:void(0)">
          <span>카카오 로그인</span>
        </a>
      </li>
      <li @click="kakoLoginBtn">
        <span>카카오 로그인2</span>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  created() {
    console.log(process.env); // true
    console.log(process.env.VUE_APP_KAKAO_API_KEY);
  },
  mounted: function () {
    this.$nextTick(function () {
      // Code that will run only after the
      // entire view has been rendered
      if (this.kakaoInfo != "") {
        var data = JSON.parse(this.kakaoInfo);

        alert("카카오로그인 성공 \n accessToken : " + data["accessToken"]);
        alert(
          "user : \n" +
            "email : " +
            data["email"] +
            "\n nickname : " +
            data["nickname"]
        );
      }
    });
  },
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

    //https://archijude.tistory.com/425
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
              const user_join_type = "k";
              console.log(response);
              const kakaoemail = response.kakao_account.email;
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
