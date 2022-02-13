<template>
  <div id="nav" :class="['container', this.$route.path === '/' ? 'nav-home' : 'nav']" v-if="!this.$route.path.startsWith('/meeting')">
    <div class="row align-items-center">
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="col-3">
        <router-link to="/">
          <img alt="INVIEW logo" src="@/assets/logo.png" class="w-100">
        </router-link>
      </div>
      <div :style="{visibility: this.$route.path !== '/' ? 'visible' : 'hidden'}" class="col">
        <Search/>
      </div>
      <div class="d-flex flex-row justify-content-end col-3">
        <CreateMeeting v-model="openCreateMeetingDialog" />
        <div v-if="Object.keys(user).length">
          <el-button class="m-1" :icon="Plus" round plain @click="openCreateMeetingDialog = true" type="primary">방 만들기</el-button>   
           <el-dropdown>
            <el-button type="primary" round plain class="m-1">
              회원정보
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <router-link to="/myproject" class="text-decoration-none"><el-dropdown-item>회의 목록 조회</el-dropdown-item></router-link>
                <router-link to="/mypage" class="text-decoration-none"><el-dropdown-item>회원 정보 수정</el-dropdown-item></router-link>
              <el-dropdown-item  @click="this.$store.dispatch('logout')">로그아웃</el-dropdown-item>
             </el-dropdown-menu>
            </template>
         </el-dropdown>
        </div>
        <div v-else>
          <el-button class="m-1" round @click="openLoginDialog=true" plain type="primary">로그인</el-button>
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

    const store = useStore()
    const user = computed(() => store.state.user)

    return { Plus, openCreateMeetingDialog, openLoginDialog, openSignupDialog, user }
  }
})
</script>

<style scoped>
.nav {
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 20px auto;
  padding: 20px;
  height: 90px;
}

@media screen and (min-width: 1200px) {
  .nav {
    max-width: 1000px;
  }
}

.nav-home {
	/* border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px; */
	margin: 20px auto;
  padding: 10px;
}

@media screen and (min-width: 1200px) {
  .nav-home {
    max-width: 1000px;
  }
}

.logo {
  width: 200px;
}
</style>