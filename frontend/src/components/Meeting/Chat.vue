<template>
  <div id="container" ref="container" style="height: 90%; margin: auto 0;">
    <div class="d-flex flex-column justify-content-between h-100">
      <div class="px-2" id="chat1" style="overflow-y: auto;" ref="chatArea">
        <div v-for="(item, idx) in recvList" :key="idx">

          <!-- 날짜 경계선 -->
          <div 
            class="text-center py-3 date" 
            v-if="(0 < idx) && (item.date !== recvList[idx - 1].date)"
          >
            <el-divider>{{ item.date }}</el-divider>
          </div>

          <!-- 내가 보낸 메세지 -->
          <div class="d-flex flex-row justify-content-end mb-1" v-if="item.sender === this.$store.state.user.nickname">
            <div class="">
              <p v-if="idx === 0 || (1 <= idx && recvList[idx - 1].sender !== item.sender)" class="m-1 text-end" style="font-size: 14px;">{{ item.sender }}</p>
              <div class="d-flex flex-row justify-content-end">
                <div class="align-self-end m-1 mb-0" style="font-size:12px; color:grey">
                  {{ item.time }}
                </div>
                <div class="p-3 border d-inline-block" style="border-radius: 15px; background-color: #cee5d0;">
                  <p class="small mb-0 text-break">{{ item.message }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 다른 사용자가 보낸 메세지 -->
          <div class="d-flex flex-row justify-content-start mb-1" v-else>
            <div>
              <p v-if="idx === 0 || (1 <= idx && recvList[idx - 1].sender !== item.sender)" class="m-1" style="font-size: 14px;">{{ item.sender }}</p>
              <div class="d-flex flex-row justify-content-start">
                <div class="p-3 d-inline-block" style="border-radius: 15px; background-color: #f3f0d7;">
                  <p class="small mb-0 text-break">{{ item.message }}</p>
                </div>
                <div class="align-self-end m-1 mb-0" style="font-size:12px; color:grey">
                  {{ item.time }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="form-outline message-input">
        <el-input
          v-model="message"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          placeholder=""
          @keyup="sendMessage"
          resize="none"
        ></el-input>
      </div>
    </div>
  </div>
  
</template>

<script>
/* eslint-disable */
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { mapState } from 'vuex'
import { ElMessage } from 'element-plus'

export default {
  name: 'Chat',
  props: [ 
    'endSignal', 'readySignal', 'startSignal', 'leaveSignal'
  ],
  computed: {
    ...mapState(['meeting', 'user'])
  },
  data() {
    return {
      message: "",
      recvList: [],
      subscribeId: "",
      socketConnected: false,
    }
  },
  watch: {
    endSignal: function() {
      console.log("채팅에서 종료신호 받음")
      // this.disconnect()
    },
    readySignal: function(newValue, oldValue) {
      console.log("ㄹㄷㄹㄷ", newValue, oldValue)
      this.ready(newValue)
    },
    startSignal: function(newValue, oldValue) {
      console.log("start!!!!!! in chat.vue", newValue, oldValue)
      this.start()
    },
    leaveSignal: function(newValue, oldValue) {
      console.log("나감", newValue, oldValue)
      if (this.socketConnected) {
        console.log("소켓좀끝")
        this.disconnect()
      }
    },
    recvList: () => {
      this.scrollToEnd()
    },
    meeting: function(newValue, oldValue) {
      console.log("연결안할꺼냐", newValue, oldValue)
      if (newValue.id) {
        this.connect()
      }
    },
    $route: function(to, form) {
      console.log(to, form)
      if (to.name !== "Meeting") {
        this.disconnect()
      }
    },
  },
  mounted() {
    console.log("소켓아", this.$store.state.meeting)
    console.log("소켓아", this.$store.state.meeting.id)
    if (this.$store.state.meeting.id !== undefined) {
      this.connect()
    }
  },
  updated() {
      // whenever data changes and the component re-renders, this is called.
      this.$nextTick(() => this.scrollToEnd());
  },
  methods: {
    scrollToEnd: function () {
        // scroll to the start of the last message
        this.$el.scrollTop = this.$el.lastElementChild.offsetTop;
        let messageDisplay = this.$refs.chatArea
        messageDisplay.scrollTop = messageDisplay.scrollHeight
    },
    sendMessage (e) {
      if(e.keyCode === 13 && this.user.nickname !== '' && this.message !== ''){
        this.send()
        this.message = ''
      }
    },    
    send() {
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`};
      console.log("Send message:" + this.message)
      if (this.stompClient && this.stompClient.connected) {
        const current_datetime = new Date()
        
        const msg = { 
          meetingId : this.meeting.id,
          sender: this.user.nickname,
          receiver: "",
          message: this.message,
          date: current_datetime.getFullYear() + "년" 
                + ("0" + (1 + current_datetime.getMonth())).slice(-2) + "월" 
                + ("0" + current_datetime.getDate()).slice(-2) + "일" 
                + " " + ['일','월','화','수','목','금','토','일'][current_datetime.getDay()]+"요일",
          time: ("0" + current_datetime.getHours()).slice(-2) + ":" + ("0" + current_datetime.getMinutes()).slice(-2)
        };

        console.log(msg)
        // send(path, message, header)로 메시지를 보낼 수 있다.
        this.stompClient.send("/publish/chat/message", JSON.stringify(msg), headers);
      }
    },    
    connect() {
      const serverURL = `${process.env.VUE_APP_API_URL}/stomp-chat`
      let socket = new SockJS(serverURL);
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()}
      this.stompClient = Stomp.over(socket, options);
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`, meetingId : this.meeting.id};
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        headers,
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          this.socketConnected = true
          console.log('소켓 연결 성공', frame);

          const current_datetime = new Date()
          const message = { 
            meetingId : this.meeting.id,
            sender: this.user.nickname,
            receiver: "",
            date: current_datetime.getFullYear() + "년" 
                  + ("0" + (1 + current_datetime.getMonth())).slice(-2) + "월" 
                  + ("0" + current_datetime.getDate()).slice(-2) + "일" 
                  + " " + ['일','월','화','수','목','금','토','일'][current_datetime.getDay()]+"요일",
            time: ("0" + current_datetime.getHours()).slice(-2) + ":" + ("0" + current_datetime.getMinutes()).slice(-2)
          };
          console.log('데이터', message);
          // send(path, message, header)로 메시지를 보낼 수 있습니다.
          this.stompClient.send('/publish/chat/join', JSON.stringify(message), headers); 

          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub-sub 구조라고 합니다.
          // subscribe(path, callback)로 메시지를 받을 수 있습니다. 
          // callback 첫번째 파라미터의 body로 메시지의 내용이 들어옵니다.
          this.subscribeId = this.stompClient.subscribe('/subscribe/chat/room/' + this.meeting.id, res => {
            console.log('구독으로 받은 메시지 입니다.', res.body)
            console.log(JSON.parse(res.body))
            const command = JSON.parse(res.body).command
            if (command === "UNREADY" || command === "READY" || command === "PARTICIPANT" || command === "OUT") {
              const commandMessage = JSON.parse(res.body).message.trim().split("  ")
              let participants = []
              commandMessage.forEach(element => {
                const info = element.split(" ")
                participants.push({
                  id: info[0],
                  nickname: info[2],
                  ready: info[1] === "T" ? true : false
                })
              });
              // console.log(participants)
              this.$store.dispatch('setParticipants', participants)

              // RUNNING 상태에서 참가자가 1명 남은 경우 이 참가자가 나갈 때 미팅 STATUS 변경
              if (this.startSignal && participants.length === 1) {
                console.log("마지막 한명")
                this.$emit('close')
              }
              if (command === "OUT") {
                console.log(this.user.id, parseInt(JSON.parse(res.body).sender))
                if (this.user.id === parseInt(JSON.parse(res.body).sender)) {
                  console.log("강퇴당함")
                  this.$emit('leave')
                  this.disconnect()
                  this.$router.push({ name: 'Home'})
                  ElMessage.error('강퇴 당하셨습니다.')
                }
              }
            } else if (command === "START") {
              this.$emit('start')
            } else if (command === "HOST") {
              console.log('HOST!!!')
              this.$store.dispatch('setNewHost', parseInt(JSON.parse(res.body).sender))
            } else if (command === null) {
              this.recvList.push(JSON.parse(res.body))
            }
            
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
          this.socketConnected = false
        }
      );        
    },
    disconnect(){
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`};
      if(this.subscribeId != ""){
        const current_datetime = new Date()
        // 연결시 획득한 구독 id를 통해 구독을 정지할 수 있습니다.
        const message = {
          meetingId: this.meeting.id,
          sender: this.user.nickname,
          receiver: "",
          date: current_datetime.getFullYear() + "년" 
                + ("0" + (1 + current_datetime.getMonth())).slice(-2) + "월" 
                + ("0" + current_datetime.getDate()).slice(-2) + "일" 
                + " " + ['일','월','화','수','목','금','토','일'][current_datetime.getDay()]+"요일",
          time: ("0" + current_datetime.getHours()).slice(-2) + ":" + ("0" + current_datetime.getMinutes()).slice(-2)
        };
        console.log("데이터", message);
        this.stompClient.send(
          "/publish/chat/leave",
          JSON.stringify(message),
          headers
        );
        this.subscribeId.unsubscribe();
        this.subscribeId = "";
        this.stompClient.connected = false;
        console.log("구독 해지 완료");
        this.$store.dispatch('deleteMeeting')
      }
    },
    start(){
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`};
      if(this.subscribeId != ""){
        const current_datetime = new Date()
        const message = {
          meetingId: this.meeting.id,
          sender: this.user.nickname,
          receiver: "",
          date: current_datetime.getFullYear() + "년" 
                + ("0" + (1 + current_datetime.getMonth())).slice(-2) + "월" 
                + ("0" + current_datetime.getDate()).slice(-2) + "일" 
                + " " + ['일','월','화','수','목','금','토','일'][current_datetime.getDay()]+"요일",
          time: ("0" + current_datetime.getHours()).slice(-2) + ":" + ("0" + current_datetime.getMinutes()).slice(-2),
          command: "START"
        };
        console.log("데이터", message);
        this.stompClient.send("/publish/chat/command", JSON.stringify(message),headers);
        console.log("미팅 시작!!");
      }
    },
    ready(flag){
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`};
      if(this.subscribeId != ""){
        const current_datetime = new Date()
        const message = {
          meetingId: this.meeting.id,
          sender: this.user.nickname,
          receiver: "",
          date: current_datetime.getFullYear() + "년" 
                + ("0" + (1 + current_datetime.getMonth())).slice(-2) + "월" 
                + ("0" + current_datetime.getDate()).slice(-2) + "일" 
                + " " + ['일','월','화','수','목','금','토','일'][current_datetime.getDay()]+"요일",
          time: ("0" + current_datetime.getHours()).slice(-2) + ":" + ("0" + current_datetime.getMinutes()).slice(-2),
          command: flag ? "READY" : "UNREADY"
        };
        console.log("데이터", message);
        this.stompClient.send("/publish/chat/command", JSON.stringify(message),headers);
        console.log("준비 완료");
      }
    },
  }
}
</script>

<style scoped>
.message-input {
  background-color: #F4F4F5;
  padding: 10px 5px;
  border-radius: 10px;
  margin: 0 5px;
}

#chat1 .form-outline .form-control~.form-notch div {
  pointer-events: none;
  border: 1px solid;
  border-color: #eee;
  box-sizing: border-box;
  background: transparent;
}

#chat1 .form-outline .form-control~.form-notch .form-notch-leading {
  left: 0;
  top: 0;
  height: 100%;
  border-right: none;
  border-radius: .65rem 0 0 .65rem;
}

#chat1 .form-outline .form-control~.form-notch .form-notch-middle {
  flex: 0 0 auto;
  max-width: calc(100% - 1rem);
  height: 100%;
  border-right: none;
  border-left: none;
}

#chat1 .form-outline .form-control~.form-notch .form-notch-trailing {
  flex-grow: 1;
  height: 100%;
  border-left: none;
  border-radius: 0 .65rem .65rem 0;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-leading {
  border-top: 0.125rem solid #39c0ed;
  border-bottom: 0.125rem solid #39c0ed;
  border-left: 0.125rem solid #39c0ed;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-leading,
#chat1 .form-outline .form-control.active~.form-notch .form-notch-leading {
  border-right: none;
  transition: all 0.2s linear;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-middle {
  border-bottom: 0.125rem solid;
  border-color: #39c0ed;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-middle,
#chat1 .form-outline .form-control.active~.form-notch .form-notch-middle {
  border-top: none;
  border-right: none;
  border-left: none;
  transition: all 0.2s linear;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-trailing {
  border-top: 0.125rem solid #39c0ed;
  border-bottom: 0.125rem solid #39c0ed;
  border-right: 0.125rem solid #39c0ed;
}

#chat1 .form-outline .form-control:focus~.form-notch .form-notch-trailing,
#chat1 .form-outline .form-control.active~.form-notch .form-notch-trailing {
  border-left: none;
  transition: all 0.2s linear;
}

#chat1 .form-outline .form-control:focus~.form-label {
  color: #39c0ed;
}

#chat1 .form-outline .form-control~.form-label {
  color: #bfbfbf;
}
</style>