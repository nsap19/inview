<template>
  <div :class="this.$route.path === '/home' ? 'wrapper-home' : 'wrapper'">
    <div id="nav" class="nav-wrapper container" v-if="this.$route.path !== '/' && !this.$route.path.startsWith('/meeting')">
      <div :class="['row', 'align-items-center', 'justify-content-between', this.$route.path === '/home' ? 'nav-home' : 'nav']">
        <!-- 로고 -->
        <div :style="{visibility: this.$route.path !== '/home' ? 'visible' : 'hidden'}" class="col-5 col-sm-3">
          <router-link to="/home">
            <img alt="INVIEW logo" src="@/assets/logo_shadow.png" class="w-100">
          </router-link>
        </div>

        <!-- 검색바 -->
        <div :style="{visibility: this.$route.path !== '/home' ? 'visible' : 'hidden'}" class="search-bar col text-center d-none d-sm-block">
          <Search/>
        </div>

        <!-- 방 만들기 / 회원 -->
        <div class="d-flex flex-row justify-content-end col-6 col-sm-3 p-0">
          <el-button class="m-1 d-sm-none" :icon="SearchIcon" round plain type="primary" size="large"
            @click="this.$router.push({ name: 'Search', query: {title: '', industry: '', company: ''}})"
          ></el-button>   
          <CreateMeeting v-model="openCreateMeetingDialog" />
          <div v-if="Object.keys(user).length" class="d-flex flex-row">
            <el-button class="m-1 d-none d-sm-block" :icon="Plus" round plain @click="openCreateMeetingDialog = true" size="large"></el-button>   
            <el-dropdown>
              <el-button :icon="Avatar" round plain class="m-1" size="large">
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <router-link to="/myproject" class="text-decoration-none"><el-dropdown-item>회의 목록 조회</el-dropdown-item></router-link>
                  <el-dropdown-item @click="UpdateUserModal">회원 정보 수정</el-dropdown-item>
                <el-dropdown-item  @click="logoutMessage">로그아웃</el-dropdown-item>
              </el-dropdown-menu>
              </template>
          </el-dropdown>
          </div>
          <div v-else>
            <el-button class="m-1" round @click="openLoginDialog=true" plain size="large">로그인</el-button>
          </div>
        <Login v-model="openLoginDialog" v-on:password="[openFindPasswordDialog=true, openLoginDialog=false]" v-on:signup="[openSignupDialog=true, openLoginDialog=false]" />
          <Register v-model="openSignupDialog" v-on:login="[openSignupDialog=false, openLoginDialog=true]" />
         <UpdateUser v-model="openUserUpdateDialog" v-on:updateUser="[openUserUpdateDialog = false]" />
        <FindPassword v-model="openFindPasswordDialog" v-on:password="[openFindPasswordDialog = false]" />
     </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'
import { Plus, Avatar } from '@element-plus/icons-vue'
import { Search as SearchIcon } from '@element-plus/icons-vue'
import Search from '@/components/SearchBar.vue'
import CreateMeeting from "@/components/NavBar/CreateMeeting.vue"
import Login from "@/components/NavBar/Login.vue"
import Register from "@/components/NavBar/Register.vue"
import { useStore } from 'vuex'
import UpdateUser from './UpdateUser.vue'
import FindPassword from "@/components/NavBar/FindPassword.vue"
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: "NavBar",
  components: {
    Search, CreateMeeting,UpdateUser,FindPassword,
    Login, Register
  },
  setup() {
    const openCreateMeetingDialog = ref(false)
    const openLoginDialog = ref(false)
    const openSignupDialog = ref(false)
    const openFindPasswordDialog = ref(false)
    const openUserUpdateDialog = ref(false)

    const store = useStore()
    const user = computed(() => store.state.user)
    const UpdateUserModal = function(){
      openUserUpdateDialog.value = true
    }
    const logoutMessage = function () {
      store.dispatch('logout')
      ElMessage({
        message: '로그아웃이 완료되었습니다.',
        type: 'success',
      })
    }
    return { 
      UpdateUserModal, logoutMessage,
      openFindPasswordDialog,openUserUpdateDialog, openCreateMeetingDialog, openLoginDialog, openSignupDialog, user,
      SearchIcon, Plus, Avatar,  
    }
  }
})
</script>

<style scoped>
.nav-wrapper {
	/* margin: 20px auto; */
  padding: 0.5rem;
}

.nav {
	/* border-radius: 10px; */
	/* box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px; */
	margin: 0 auto;
  padding: 25px 15px;
}

.nav-home {
	margin: 0 auto;
  padding: 25px 15px;
}

@media screen and (min-width: 1200px) {
  .nav-home {
    max-width: 1000px;
  }
}

.logo {
  width: 200px;
}

.wrapper {
  background-color: #F9F9F9;
  box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
}

.wrapper-home {
  background-color: #F9F9F9;
}
</style>