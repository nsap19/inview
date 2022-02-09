<template>
  <!-- element tag nav 부분 복사하기  -->
  <el-menu
    :default-active="activeIndex"
    text-color="black"
    class="el-menu-demo nav-menu"
    mode="horizontal"
    @select="handleSelect"
  >
    <el-menu-item index="1" @click="$router.push('/')">Main</el-menu-item>
    <el-menu-item index="2" @click="$router.push('/room')">Mypage</el-menu-item>
    <el-menu-item v-if="user.id" @click="$router.push('/post')" index="2">회의 입장</el-menu-item>

    <!-- 로그인 전  -->
    <!-- vuex의 user에서 id가 존재하지 않아야만 보여야한다. -->
    <el-sub-menu v-if="!user.id" index="3" class="login-menu">
      <template v-slot:title>회원가입/로그인</template>
      <el-menu-item index="3-1" @click="SET_REGISTER_MODAL(true)"
        >회원가입</el-menu-item
      >
      <el-menu-item index="3-2" @click="SET_LOGIN_MODAL(true)">로그인</el-menu-item>
    </el-sub-menu> 
    <!-- 로그인 후  -->
    <!-- user가 존재하는 경우  -->
    <el-sub-menu v-else index="3" class="login-menu">
        <el-menu-item index="3-1"> {{user.name}} 님 환영합니다.</el-menu-item>
        <template v-slot:title>내 정보</template>
        <el-menu-item index="3-1">프로필 수정</el-menu-item>
        <el-menu-item index="3-2">회의 목록 </el-menu-item>
        <el-menu-item @click="SET_LOGOUT()" index="3-2">로그아웃</el-menu-item>
    </el-sub-menu>
    <Login></Login>
    <Register></Register>
  </el-menu>
</template>

<script>
import {mapState, mapMutations} from 'vuex'
import Login from './Login.vue'
import Register from "./NavBar/Register.vue";

export default {
  components:{
    Login,
    Register
  },
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
    };
  },
  computed:{
    ...mapState([ 'user' ])
  },
  methods: {
    ...mapMutations(["SET_REGISTER_MODAL", "SET_LOGOUT","SET_LOGIN_MODAL", "SET_LOGOUT" ]),
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style>
.nav-menu {
  display: flex;
}
.login-menu {
  /* NavMenu.vue 뒤에 !important 붙여주기 */
  margin-left: auto !important;
}
</style>
