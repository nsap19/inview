<template>
  <div class="nav_wrap">
    <div>
      <a href="#"><img :src="logo" alt="logo" id="logo"></a>
    </div>
    <div class="search_wrap">
      <input type="text" id="search" placeholder="회의 검색"/>
      <el-button size="large" :icon="Search">Search</el-button>
    </div>
    <div class="login_wrap">
      <el-button type="primary" round id="btn_login" @click="LoginVisible = true">로그인</el-button>
      <a href="#" id="mypage" v-if="isLogin === true">마이페이지</a>
    </div>
  </div>

   <el-dialog
    v-model="LoginVisible"
    title=""
    width="30%"
    :before-close="handleClose"
    class="login_modal_wrap"
    >
    <h1>{{ signwhat }}</h1>
    <el-input v-model="Email" placeholder="abc@example.com" clearable class="login_email"/>
    <el-input
    v-model="pw"
    type="password"
    placeholder="비밀번호를 입력해 주세요 (최소 8자 이상)"
    show-password
    id="login_modal_password"
    />
    <el-input
    v-model="pw_check"
    v-if="password_check === true"
    type="password"
    placeholder="비밀번호를 재입력 해주세요"
    show-password
    id="login_modal_password"
    />
    <a href="#" id="signin" @click="Signup">회원가입</a>
    <a href="#" id="signup" @click="Signin">로그인</a>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="LoginVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submit"
          >Confirm</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script>
  import small_logo from '@/assets/logo_small.png'

  export default {
    data() {
      return {
        signwhat: 'Login',
        logo: small_logo,
        LoginVisible: false,
        pw_check: '',
        password_check: false,
        Email: '',
        pw: '',
      }
    },
    methods: {
     submit: function() {
       const url = '' //요청보낼 서버의 url 을 적어주세요
       var uid = this.Email
       var upw = this.pw
       this.Email = ''
       this.pw = ''
       if(this.password_check === false) {
         let data = { uid, upw }
         //axios.post(url, data)
       } 
       else {
         if(upw !== this.pw_check) {
           alert("패스워드를 다시 확인해주세요")
          } 
          var emailcheck = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
          if(emailcheck.test(uid) === false) {
            alert("이메일 형식을 확인해주세요")
          }
          if(upw < 8) {
            alert("비밀번호 8자리 이상")
          }
          this.pw_check = ''
          let data = { uid, upw }
          //axios.post(url, data)
       }
     },
     Signin: function() {
       this.password_check = false
       this.signwhat = '로그인'
     },
     Signup: function() {
       this.password_check = true
       this.signwhat = '회원가입'
     }
    }
  }
</script>

<style>
  .nav_wrap {
    width: 100vw;
    height: 8%;
    background-color: #F2F2F2;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .search_wrap {
    display: flex;
    flex-direction: row;
  }

  #logo {
    margin-left: 10%;
  }

  #search {
    width: 600px;
    height: 40px;
    border: none;
    border-radius: 50px;
    font-size: 20px;
    padding-left: 3%;
  }

  .login_wrap {
    padding-right: 20px;
  }

  #btn_login {
    width: 130px;
    height: 45px;
    border-radius: 50px;
    font-weight: bold;
    font-size: 20px;
  }

  .login_modal_wrap {
    display: flex;
    justify-content: center;
  }

  .login_email {
    margin-top: 30px;
    width: 100%;
    height: 30%;
  }

  #login_modal_password {
    margin-top: 20px;
  }

  #signin {
    color: blue;
  }

  #signup {
    color: blue;
    margin-left: 10px;
  }

  #mypage {
    color: blue;
  }
</style>