<template>
    <div class="wrap">
        <div class="card-1">
            <div class="card-header">
                <div>
                    <h5>비밀번호 변경</h5>
                </div>
            </div>
            <div class="card-body1">
                <div id="box">
                    <h6>현재 비밀번호</h6>
                    <el-input
                        v-model="current_pw"
                        type="password"
                        placeholder="현재 비밀번호"
                        show-password
                    />
                </div>
                <div id="box">
                    <h6>새 비밀번호</h6>
                    <el-input
                        v-model="new_pw"
                        type="password"
                        placeholder="새 비밀번호"
                        show-password
                    />
                </div>
                <div id="box">
                    <h6>비밀번호 확인</h6>
                    <el-input
                        v-model="new_pw_check"
                        type="password"
                        placeholder="비밀번호 확인"
                        show-password
                    />
                </div>
                <p id="err_msg1">{{ pw_check_err }}</p>
            </div>
            <el-button id="btn_submit" @click="change_pw">제출</el-button>
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
                        v-model="pw_for_delete"
                        type="password"
                        placeholder="현재 비밀번호"
                        show-password
                />
            </div>
            <el-button id="btn_submit"  @click="centerDialogVisible = true">회원탈퇴</el-button>
        </div>
    </div>

    <el-dialog v-model="centerDialogVisible" title="Warning" width="30%" center>
        <span
        >정말 회원 탈퇴를 진행하시겠습니까?</span
        >
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="centerDialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="centerDialogVisible = false"
                >Confirm</el-button
                >
            </span>
        </template>
  </el-dialog>
</template>

<script>

export default {
    data() {
        return {
            current_pw: '',
            new_pw: '',
            new_pw_check: '',
            pw_for_delete: '',
            pw_check_err: '',
        }
    },
    
    methods: {
        change_pw: function() {
            if (this.new_pw != this.new_pw_check) {
                this.pw_check_err = '비밀번호가 틀립니다'
            }
            else {
                this.pw_check_err = ''
                //axios.post(url, this.new_pw)
            }
        }
    }
}

</script>

<style>
html, body {
    width: 100%;
    height: 100%;
}

#app {
    width: 100%;
    height: 100%;
}

.wrap {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.card-1 {
    width: 400px;
    height: 400px;
    margin-top: 50px;
    box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
    position: relative;
}

.card-2 {
    margin-top: 100px;
    width: 400px;
    height: 200px;
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