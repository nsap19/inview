<template>
  <div id="nav" class="container" v-if="this.$route.path !== '/' && !this.$route.path.startsWith('/meeting')">
    <div class="d-flex flex-row justify-content-between align-items-center">
      <!-- 메인 페이지에서 네비바가 어떻게 보일지 미정이라 :style 삭제 안함 -->
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="w-25 p-3">
        <router-link to="/">
          <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100">
        </router-link>
      </div>
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="">
        <Search/>
      </div>
      <div class="d-flex flex-row justify-content-end">
        <CreateMeeting v-model="openCreateMeetingDialog" />
        <div v-if="Object.keys(user).length">
          <el-button class="m-1" :icon="Plus" round @click="openCreateMeetingDialog = true">방 만들기</el-button>
          <el-button class="m-1" round @click="this.$store.dispatch('logout')">로그아웃</el-button>
        </div>
        <div v-else>
          <el-button class="m-1" round @click="openLoginDialog=true">로그인</el-button>
        </div>
        <Login v-model="openLoginDialog" v-on:signup="[openSignupDialog=true, openLoginDialog=false]" />
        <Register v-model="openSignupDialog" v-on:login="[openSignupDialog=false, openLoginDialog=true]" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import Search from '@/components/SearchBar.vue'
import CreateMeeting from "@/components/NavBar/CreateMeeting.vue"
import Login from "@/components/NavBar/Login.vue"
import Register from "@/components/NavBar/Register.vue"
import { useStore } from 'vuex'

export default defineComponent({
  name: "NavBar",
  components: {
    Search, CreateMeeting,
    Login, Register
  },
  setup() {
    const openCreateMeetingDialog = ref(false)
    const openLoginDialog = ref(false)
    const openSignupDialog = ref(false)
    const dosomething = function () {
      alert('!!!')
    }
    const store = useStore()
    const user = computed(() => store.state.user)

    return { Plus, openCreateMeetingDialog, openLoginDialog, openSignupDialog, dosomething, user }
  }
})
</script>

<style scoped>
#nav {
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 20px auto;
  padding: 10px;
}

@media screen and (min-width: 1200px) {
  #nav {
    max-width: 1000px;
  }
}
</style>