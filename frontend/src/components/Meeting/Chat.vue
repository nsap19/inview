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

export default {
  name: 'Chat',
  props: [ 
    'endSignal'
  ],
  computed: {
    ...mapState(['meeting', 'user'])
  },
  data() {
    return {
      // meetingId: "",
      // userName: "",
      message: "",
      recvList: [
        // {"meetingId":"1","sender":"익명","time": "23:11","date":"2021년01월23일 일요일","message":"익명님이 입장하셨습니다.","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "23:12","date":"2021년01월23일 일요일","message":"일이삼사오육칠팔구십님이 입장하셨습니다.","type":null, "receiver":""},
        // {"meetingId":"1","sender":"익명","time": "23:13","date":"2021년01월23일 일요일","message":"2321","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "23:14","date":"2021년01월23일 일요일","message":"456133","type":null, "receiver":""},
        // {"meetingId":"1","sender":"익명","time": "23:15","date":"2021년01월23일 일요일","message":"rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "00:01","date":"2021년01월24일 월요일","message":"월요일 시작","type":null, "receiver":""},
        // {"meetingId":"1","sender":"익명","time": "01:11","date":"2021년01월24일 월요일","message":"111111111sfdddddddddddd","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "02:11","date":"2021년01월24일 월요일","message":"s","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "03:11","date":"2021년01월24일 월요일","message":"대통령·국무총리·국무위원·행정각부의 장·헌법재판소 재판관·법관·중앙선거관리위원회 위원·감사원장·감사위원 기타 법률이 정한 공무원이 그 직무집행에 있어서 헌법이나 법률을 위배한 때에는 국회는 탄핵의 소추를 의결할 수 있다. 헌법재판소에서 법률의 위헌결정, 탄핵의 결정, 정당해산의 결정 또는 헌법소원에 관한 인용결정을 할 때에는 재판관 6인 이상의 찬성이 있어야 한다. 이 헌법시행 당시에 이 헌법에 의하여 새로 설치될 기관의 권한에 속하는 직무를 행하고 있는 기관은 이 헌법에 의하여 새로운 기관이 설치될 때까지 존속하며 그 직무를 행한다. 국가는 농·어민과 중소기업의 자조조직을 육성하여야 하며, 그 자율적 활동과 발전을 보장한다.","type":null, "receiver":""},
        // {"meetingId":"1","sender":"일이삼사오육칠팔구십","time": "04:11","date":"2021년01월24일 월요일","message":"sssss","type":null, "receiver":""},
        // {"meetingId":"1","sender":"헐","time": "05:11","date":"2021년01월24일 월요일","message":"sss","type":null, "receiver":""},
      ],
      subscribeId: "",
    }
  },
  watch: {
    endSignal: function(newVal, oldVal) {
      console.log(newVal, oldVal)
      this.disconnect()
    },
    recvList: () => {
      this.scrollToEnd()
    }
  },
  created() {
    // .vue가 생성되면 소켓 연결 시도
    this.connect()
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
      const serverURL = "http://localhost:8080/stomp-chat"
      let socket = new SockJS(serverURL);
      let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()}
      this.stompClient = Stomp.over(socket, options);
      let headers = {Authorization: `Bearer ${localStorage.getItem("token")}`, meetingId : this.meetingId};
      console.log('headers' + headers)
      console.log(this.meeting.id)
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        headers,
        frame => {
          // 소켓 연결 성공
          this.connected = true;
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
            console.log('구독으로 받은 메시지 입니다.', res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            this.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
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
      }
    }
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