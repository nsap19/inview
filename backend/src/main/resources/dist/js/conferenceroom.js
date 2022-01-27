/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

// var ws = new WebSocket('ws://' + location.host + '/groupcall');
var participants = {};
var userId;


const serverURL = "http://localhost:8080/groupcall";
let ws = new SockJS(serverURL);
// this.stompClient = Stomp.over(ws);
// console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
// this.stompClient.connect(
//   {},
//   frame => {
// 	// 소켓 연결 성공
// 	this.connected = true;
// 	console.log('소켓 연결 성공', frame);

// 	// send(path, message, header)로 메시지를 보낼 수 있습니다.
// 	// this.stompClient.send('/publish/video-chat/join', JSON.stringify(message), {}); 

// 	// this.subscribeId = this.stompClient.subscribe('/subscribe/chat/room/' + this.meetingId, res => {
// 	//   console.log('구독으로 받은 메시지 입니다.', res.body);

// 	//   // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
// 	//   this.recvList.push(JSON.parse(res.body))
// 	// });
//   },
//   error => {
// 	// 소켓 연결 실패
// 	console.log('소켓 연결 실패', error);
// 	this.connected = false;
//   }
//   )

  
window.onbeforeunload = function() {
	ws.close();
};

ws.onmessage = function(message) {
	var parsedMessage = JSON.parse(message.data);
	console.info('Received message: ' + message.data);

	switch (parsedMessage.id) {
	case 'existingParticipants':
		onExistingParticipants(parsedMessage);
		break;
	case 'newParticipantArrived':
		onNewParticipant(parsedMessage);
		break;
	case 'participantLeft':
		onParticipantLeft(parsedMessage);
		break;
	case 'receiveVideoAnswer':
		receiveVideoResponse(parsedMessage);
		break;
	case 'iceCandidate':
		participants[parsedMessage.userId].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
	        if (error) {
		      console.error("Error adding candidate: " + error);
		      return;
	        }
	    });
	    break;
	default:
		console.error('Unrecognized message', parsedMessage);
	}
}

function register() {
	userId = document.getElementById('userId').value;
	var meetingId = document.getElementById('meetingId').value;

	document.getElementById('room-header').innerText = 'ROOM ' + meetingId;
	document.getElementById('join').style.display = 'none';
	document.getElementById('room').style.display = 'block';

	var message = {
		id : 'joinRoom',
		userId : userId,
		meetingId : meetingId,
	}

	// stompClient.send("/publish/video-chat/join",
    //     {},
    //     JSON.stringify({message: message, type: 'JOIN'})
    // )

	sendMessage(message);
}

function onNewParticipant(request) {
	receiveVideo(request.userId);
}

function receiveVideoResponse(result) {
	participants[result.userId].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
		if (error) return console.error (error);
	});
}

function callResponse(message) {
	if (message.response != 'accepted') {
		console.info('Call not accepted by peer. Closing call');
		stop();
	} else {
		webRtcPeer.processAnswer(message.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}
}

function onExistingParticipants(msg) {
	var constraints = {
		audio : true,
		video : {
			mandatory : {
				maxWidth : 320,
				maxFrameRate : 15,
				minFrameRate : 15
			}
		}
	};

	console.log(userId + " registered in room " + meetingId);
	var participant = new Participant(userId);
	participants[userId] = participant;
	var video = participant.getVideoElement();

	var options = {
	      localVideo: video,
	      mediaConstraints: constraints,
	      onicecandidate: participant.onIceCandidate.bind(participant)
	    }
	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
		function (error) {
		  if(error) {
			  return console.error(error);
		  }
		  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});

	msg.data.forEach(receiveVideo);
}

function leaveRoom() {
	sendMessage({
		id : 'leaveRoom'
	});

	for ( var key in participants) {
		participants[key].dispose();
	}

	document.getElementById('join').style.display = 'block';
	document.getElementById('room').style.display = 'none';

	ws.close();
}

function receiveVideo(userId) {
	var participant = new Participant(userId);
	participants[userId] = participant;
	var video = participant.getVideoElement();

	var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function (error) {
			  if(error) {
				  return console.error(error);
			  }
			  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});;
}

function onParticipantLeft(request) {
	console.log('Participant ' + request.userId + ' left');
	var participant = participants[request.userId];
	participant.dispose();
	delete participants[request.userId];
}

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	console.log('Sending message: ' + jsonMessage);
	ws.send(jsonMessage);
}
