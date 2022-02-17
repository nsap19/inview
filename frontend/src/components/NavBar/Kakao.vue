<template>
  <div class="container">
    <div class="large-12 medium-12 small-12 cell">
      <img
        src="@/assets/kakao_login_medium_wide.png"
        class="w-100"
        @click="kakoLoginBtn"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { useStore } from "vuex";
import { ElMessage } from 'element-plus'
import { onMounted } from '@vue/runtime-core';

export default {
  name: "Kakao",
  props: {
    modelValue: Boolean,
  },
  setup(props, { emit }) {
    onMounted(()=>{
       window.Kakao.init(kakakoKey); //처음에 카카오 로그인 세션을 없애준다.
    })
    const kakakoKey = process.env.VUE_APP_KAKAO_JS_KEY;
    const kakaoInfo = "";
    const store = useStore();
    const kakoLoginBtn = function () {
      if (window.Kakao.Auth.getAccessToken()) {
        window.Kakao.API.request({
          url: "/v1/user/unlink",
          success: function (response) {
          },
          fail: function (error) {
          },
        });
        window.Kakao.Auth.setAccessToken(undefined);
      }

      //로그인 시도
      window.Kakao.Auth.login({
        success: function (response) {
          window.Kakao.API.request({
            url: "/v2/user/me",
            // data: { property_keys: ["kakao_account.email"] },
            success: async function (response) {
              await axios
                .post("oauth/login/kakao", response)
                .then((res) => {
                  // console.log(res.data);
                  localStorage.setItem("token", res.data.token);
                  store.dispatch("setUser", {
                    nickname: res.data.nickname,
                    id: res.data.userId,
                    oauth: true
                  });
                  ElMessage({
                    message: "로그인 되었습니다.",
                    type: "success",
                  });
                  emit('closeDialog')
                })
                .catch((e) => {
                  // console.log(e);
                  ElMessage({
                    message: '오류가 발생했습니다. 다시 시도해주세요.',
                  type: 'warning',
                  });
                  // emit('closeDialog')
                });
            },
            fail: function (error) {
              // console.log(error);
            },
          });
        },
        fail: function (error) {
          // console.log(error);
        },
      });
    };

    return { kakoLoginBtn };
  },
};
</script>

<style></style>