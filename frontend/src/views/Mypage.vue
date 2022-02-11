<template>
    <div class="wrap">
        <div class="card-1" >
            <div class="card-header">
                <div>
                    <h5>닉네임 변경</h5>
                </div>
            </div>
            <div class="card-body1">
              
                <div id="box">
                    <h6>변경 닉네임</h6>
                    <el-input
                        v-model="nicknameChangeForm.nickname"
                        type="text"
                        placeholder="닉네임"
                    />
                </div>
                <div id="box">
                    <h6>비밀번호</h6>
                    <el-input
                        v-model="nicknameChangeForm.password"
                        type="password"
                        placeholder="비밀번호"
                        show-password
                    />
                </div>
                <p id="err_msg1">{{ nicknameChangeErrorMessage}}</p>
            </div>
            <el-button id="btn_submit" @click="ChangeNickname">제출</el-button>
        </div>
        <div class="card-3" >
            <div class="card-header">
                <div>
                    <h5>비밀번호 변경</h5>
                </div>
            </div>
            <div class="card-body1">
                
                 <div id="box">
                    <h6>현재 닉네임</h6>
                    <el-input
                        v-model="passwordChangeForm.nickname"
                        type="password"
                        placeholder="닉네임"
                    />
                </div>
                <div id="box">
                    <h6>새 비밀번호</h6>
                    <el-input
                        v-model="passwordChangeForm.password"
                        type="password"
                        placeholder="새 비밀번호"
                        show-password
                    />
                </div>
                <div id="box">
                    <h6>비밀번호 확인</h6>
                    <el-input
                        v-model="passwordChangeForm.passwordCheck"
                        type="password"
                        placeholder="비밀번호 확인"
                        show-password
                    />
                </div>
                <p id="err_msg1">{{ passwordChangeErrorMessage }}</p>
            </div>
            <el-button id="btn_submit" @click="ChangePassword">제출</el-button>
        </div>
        <div class="card-2">
            <div class="card-header2">
                <div>
                    <h5>회원 탈퇴</h5>
                </div>
            </div>
            <div class="card-body2">
                <div>
                    <h6>현재 비밀번호</h6>
                </div>
                <el-input
                        v-model="password"
                        type="password"
                        placeholder="현재 비밀번호"
                        show-password
                />
            </div>
            <el-button id="btn_submit"  @click="dialogVisible = true">회원탈퇴</el-button>
        </div>
    </div>

    <el-dialog v-model="dialogVisible" title="Warning" width="30%" center>
        <span
        >정말 회원 탈퇴를 진행하시겠습니까?</span
        >
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="Delete"
                >Confirm</el-button
                >
            </span>
        </template>
  </el-dialog>
</template>

<script lang="ts">

import axios from 'axios'

import { defineComponent, reactive,ref } from 'vue'
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

export default defineComponent({
	name: 'Mypage',

    setup(){
        const store = useStore()
        const router = useRouter()

        const password = ref('')

        const nicknameChangeForm = reactive({
            nickname: '',
            password: ''
        })

        const passwordChangeForm = reactive({
            password: '',
            passwordCheck: '',
            nickname: ''
        })
        
        const passwordChangeErrorMessage = ref('')
        const nicknameChangeErrorMessage = ref('')

        const dialogVisible = ref(false)
        const ChangeNickname = ()=>{
          const { nickname, password } = nicknameChangeForm
           if(password == ""|| nickname == ""){
               nicknameChangeErrorMessage.value = "빈칸을 모두 입력해주세요."
            }
           
            else {
                const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
                if(passwordPattern.test(password) === false){
                    nicknameChangeErrorMessage.value = '비밀번호는 8자를 넘으며 숫자, 영문 대문자, 특수 문자를 포함해야 합니다'
                }
                else{
                   nicknameChangeErrorMessage.value = ''
                    axios.put( `/users/${store.state.user.id}`, nicknameChangeForm,
                        {
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }
                    ).then((res: any) => {
                        console.log('SUCCESS!!');
                        console.log(res)
                        
                        resetNicknameForm()
                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        console.log(err.response)
                    });  
                }

            }
        }
        const ChangePassword = ()=>{
          const {  nickname, password, passwordCheck } = passwordChangeForm
           if(password == "" || passwordCheck == ""){
               passwordChangeErrorMessage.value = "빈칸을 모두 입력해주세요."
            }
            else if (password != passwordCheck) {
                passwordChangeErrorMessage.value = '비밀번호가 같지 않습니다.'
            }
            else {
                const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
                if(passwordPattern.test(password) === false){
                    passwordChangeErrorMessage.value = '비밀번호는 8자를 넘으며 숫자, 영문 대문자, 특수 문자를 포함해야 합니다'
                }
                else{
                   passwordChangeErrorMessage.value = ''
                    axios.put( `/users/${store.state.user.id}`, {
                        nickname: nickname,
                        password: password,
                    },
                        {
                         headers: 
                            {
                                Authorization: `Bearer ${localStorage.getItem("token")}`,
                            }
                        }
                    ).then((res: any) => {
                        console.log('SUCCESS!!');
                        console.log(res)
                        
                        resetPasswordForm()
                        


                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        console.log(err.response)
                        
                    });  
                    //reset
                }

            }
        }
       
        const resetPasswordForm = () =>{
            passwordChangeForm.password =''
            passwordChangeForm.nickname = ''
            passwordChangeForm.passwordCheck = ''
        }

        const resetNicknameForm = () => {
            nicknameChangeForm.nickname = ''
            nicknameChangeForm.password = ''
        }

        const Delete = () =>{
            axios.delete(`/users/${store.state.user.id}`, {params: {password: password.value}}).then((res: any) => {
                        console.log('SUCCESS!!');
                        console.log(res)

                        localStorage.removeItem("token");
                        router.push("/")

                        password.value = ""

                    }).catch((err: any) => {
                        console.log('FAILURE!!');
                        console.log(err.response)
                    });  
            dialogVisible.value = false
        }
        return{Delete, password, dialogVisible, ChangeNickname, ChangePassword, passwordChangeErrorMessage,nicknameChangeErrorMessage,passwordChangeForm ,nicknameChangeForm }

    }
});


</script>

<style>

.wrap {
    max-width: 1000px;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    margin: 0 auto;
}

.card-1 {
    width: 400px;
    min-height: 350px;
    margin-top: 50px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-2 {
    margin-top: 100px;
    width: 400px;
    min-height: 200px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-3 {
    margin-top: 100px;
    width: 400px;
    min-height: 400px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-body2 {
    padding: 10px;
}

.card-header2 {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 2%;
}

.card-header {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
}

.card-body1 {
    padding: 2%;
}

.change {
    position: absolute;
    right: 10px;
}

#change {
    justify-self: end;
}

#box {
    margin: 10px;
}

#btn_submit {
    position: absolute;
    bottom: 10px;
    right: 10px;
    width: 80px;
    height: 30px;
}

#err_msg1 {
    color: red;
    text-align: center;
}

</style>