<template>
  <div >
    <!-- <h1>This is an about page</h1>
    <el-button type="text" @click="dialogFormVisible = true">open a Form nested Dialog</el-button> -->

    <el-dialog style="width:50%; margin:0 auto;" title="Inview 회원가입" :v-model:visible="modal?.register">
      <el-form :model="form">
        <el-form-item label="이름" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="이메일" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="비밀번호" :label-width="formLabelWidth">
          <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
       
      </el-form>
          <template v-slot:footer>
      <span class="dialog-footer">
        <el-button @click="SET_REGISTER_MODAL(false)">Cancel</el-button>
        <el-button type="primary" @click="register">회원가입하기</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
/* eslint-disable */ 
import { mapState, mapMutations } from "vuex";
import { userAPI } from '../utils/axios';
export default {
  data() {
    return {
      dialogTableVisible: false,
      dialogFormVisible: false,
      form: {
        name: "",
        email:"",
        password:""
      },
      formLabelWidth: "120px",
    };
  },
  computed: {
    ...mapState(["modal"]),
  },
  methods:{
    ...mapMutations(['SET_REGISTER_MODAL', 'SET_LOGIN_MODAL' ]),
    async register(){
      try {
        const {name, email, password} = this.form;
        if(!name || !email || !password){
          alert("이름, 이메일, 비밀번호를 모두 입력해주세요")
          return;
        }

        const result = await userAPI.register(name, email, password);
        if(result.data.status === "OK"){
          alert("회원가입이 완료되었습니다");
          // 회원가입이 완료되면 자동으로 로그인 modal을 띄운다
          this.SET_LOGIN_MODAL(true);
        }else{
          alert("회원가입이 실패하였습니다.")
        }
      } catch (error) {
        alert("회원가입이 실패하였습니다.")
      }
    }
  }
};




</script>
