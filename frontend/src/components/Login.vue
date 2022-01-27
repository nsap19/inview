<template>
  <div>
    <el-dialog
    title="로그인"
    v-model="modal.login"
    >
    <el-form :model="form">

      <el-form-item label="email" :label-width="formLabelWidth">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="password" :label-width="formLabelWidth">
        <el-input v-model="form.password" autocomplete="off"></el-input>
      </el-form-item>

    </el-form>
    
    <!--slot 해결 하기-->
    <template v-slot:footer>
    <span class="dialog-footer">
      <el-button @click="SET_LOGIN_MODAL(false)"> Cancel </el-button>
      <el-button type='primary' @click="login" > 로그인 </el-button>
    </span>
    </template>

    </el-dialog>
  </div>
</template>

<script>
/* eslint-disable */ 
import { mapState, mapMutations } from 'vuex'
import { userAPI } from '../utils/axios'

export default {
  data(){
    return{
      form:{
        email: "",
        password: ""
      },
      popup:true,
      formLabelWidth: '120px'
    }
  },
  // data를 활용하는 형태, data를 다르게 변형시켜서 보여줘야할 경우 
  // computed를 활용한다 
  //ex) data에서는 50이라고 저장되어있지만 
  // computed에서는 data가 50인경우 오십이라고 인식해서 변경
  computed:{
    ...mapState( ['modal'] )
  },
  methods:{
    ...mapMutations( ['SET_LOGIN_MODAL', 'SET_USER' ] ),

    async login(){
      const {email, password} = this.form;
      if(!email || !password){
        alert("아이디 및 비밀번호를 입력해주세요")
      }
      const result = await userAPI.login(email, password);
      console.log(result);
      // id와 name은 vuex에 저장
      // vuex를 활용할 경우 새로고침을하면 vuex가 초기화된다.
      // 따라서 store/index.js를 보면 vuex-persistedstate라이브러리가
      // 새로고침을 하더라도 vuex가 초기화되지 않게끔 진행해준다. 
      // 로그인 처리 
      if(result && result.data.token){
        // token은 localStorage에 저장
        localStorage.setItem("token", result.data.token);
        // vuex에 유저에 대한 정보를 저장
        this.SET_USER({id: result.data.id, name: result.data.name});
        // 로그인처리가 완료되었기 때문에 modal 종료
        this.SET_LOGIN_MODAL(false);
      }else{
        alert("로그인에 실패하였습니다.")
      }
    }
  }
}
</script>

<style>

</style>