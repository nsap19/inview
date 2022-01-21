<template>
  <div id="app">
    <h1>{{meetingId}}번방</h1>
    <h5>{{subscribeId}}</h5>
    <button @click="disconnect">구독 해지</button><hr/>
    유저이름: 
    <input
      v-model="userName"
      type="text"
    >
    내용: <input
      v-model="message"
      type="text"
      @keyup="sendMessage"
    >
    <div
      v-for="(item, idx) in recvList"
      :key="idx"
    >
      <h3>유저이름: {{ item.userName }}</h3>
      <h3>내용: {{ item.message }}</h3>
    </div>
  </div>
</template>

<script>
// 라이브러리 임포트
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'App',
  data() {
    return {
      meetingId: "1",
      userName: "익명",
      message: "",
      recvList: [],
      subscribeId: "",
    }
  },
  created() {
    // .vue가 생성되면 소켓 연결 시도
    this.connect()
  },
  methods: {
    sendMessage (e) {
      if(e.keyCode === 13 && this.userName !== '' && this.message !== ''){
        this.send()
        this.message = ''
      }
    },    
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          meetingId : this.meetingId,
          userName: this.userName,
          message: this.message 
        };
        // send(path, message, header)로 메시지를 보낼 수 있다.
        this.stompClient.send("/publish/chat/message", JSON.stringify(msg), {});
      }
    },    
    connect() {
      const serverURL = "http://localhost:8080/stomp-chat"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          const message = { 
            meetingId : this.meetingId,
            userName: this.userName,
          };
          console.log('데이터', message);
          // send(path, message, header)로 메시지를 보낼 수 있습니다.
          this.stompClient.send('/publish/chat/join', JSON.stringify(message), {}); 

          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub-sub 구조라고 합니다.
          // subscribe(path, callback)로 메시지를 받을 수 있습니다. 
          // callback 첫번째 파라미터의 body로 메시지의 내용이 들어옵니다.
          this.subscribeId = this.stompClient.subscribe('/subscribe/chat/room/' + this.meetingId, res => {
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
      if(this.subscribeId != ""){
        // 연결시 획득한 구독 id를 통해 구독을 정지할 수 있습니다.
        this.subscribeId.unsubscribe();
        this.subscribeId = "";
        this.stompClient.connected = false;
        console.log("구독 해지 완료");
      }
    }
  }
}
</script>