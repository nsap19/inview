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

var participants = {};
var userId;

// 음소거, 카메라 on/off 기능
let muted = false;
let cameraOff = false;
let myStream;

function handleMuteClick(){
	var participant = participants[userId];
	const myStream = participant.getVideoElement().captureStream();
	myStream.getAudioTracks().forEach((track)=>(track.enabled = !track.enabled));
	if(!muted){ //마이크 끄기
		document.getElementById("micOn").style.display = 'none'
		document.getElementById("micOff").style.display = 'block'
		muted = true;
		participant.rtcPeer.audioEnabled = false;
	}else{ //마이크 켜기
		document.getElementById("micOn").style.display = 'block'
		document.getElementById("micOff").style.display = 'none'
		muted = false;
		participant.rtcPeer.audioEnabled = true;
	}
}
function handleCameraClick(){
	var participant = participants[userId];
	const myStream = participant.getVideoElement().captureStream();
	myStream.getVideoTracks().forEach((track)=>(track.enabled = !track.enabled));
	if(cameraOff){//카메라 켜기
		document.getElementById("cameraOn").style.display = 'block'
		document.getElementById("cameraOff").style.display = 'none'
		cameraOff = false;
		participant.rtcPeer.videoEnabled = true;
	} else{//카메라 끄기
		document.getElementById("cameraOn").style.display = 'none'
		document.getElementById("cameraOff").style.display = 'block'
		cameraOff = true;
		participant.rtcPeer.videoEnabled = false;		
	}
}

const serverURL = "https://i6a201.p.ssafy.io/api/groupcall";
let ws = new SockJS(serverURL);

window.onbeforeunload = function() {
	ws.close();
};

ws.onmessage = function(message) {
	var parsedMessage = JSON.parse(message.data);
	// console.info('Received message: ' + message.data);

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
		if(parsedMessage.userId){
		participants[parsedMessage.userId].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
	        if (error) {
		      console.error("Error adding candidate: " + error);
		      return;
	        }
	    });
		}
		else{
			webRtcPeer.addIceCandidate(parsedMessage.candidate, function(error) {
				if (error)
					return console.error('Error adding candidate: ' + error);
			});
		}
	    break;
		case 'startResponse':
		startResponse(parsedMessage);
		break;
	case 'playResponse':
		playResponse(parsedMessage);
		break;
	case 'playEnd':
		playEnd();
		break;
	case 'error':
		setState(NO_CALL);
		onError('Error message from server: ' + parsedMessage.message);
		break;
	case 'stopped':
		break;
	case 'paused':
		break;
	case 'recording':
		break;
	default:
		setState(NO_CALL);
	
		console.error('Unrecognized message', parsedMessage);
	}
}

function register() {
	var meetingId = document.getElementById('meetingId').value;
	let token = localStorage.getItem("token");
	let userNickname = document.getElementById('userNickname').value

	var message = {
		id : 'joinRoom',
		accessToken : token,
		meetingId : meetingId,
		userNickname: userNickname
	}

	sendMessage(message);
}

function onNewParticipant(request) {
	// console.log("onNewParticipant에서", request)
	receiveVideo(request.userId, request.userNickname);
}

function receiveVideoResponse(result) {
	// console.log("receive:"+result.userId)
	participants[result.userId].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
		if (error) return console.error (error);
	})

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
	
	userId = msg.userId;
	meetingId = msg.meetingId;
	userNickname = msg.userNickname;
	var participant = new Participant(userId, userNickname);
	participants[userId] = participant;
	var video = participant.getVideoElement();

	video.srcObject = myStream;

	var options = {
	      localVideo: video,
	      mediaConstraints: constraints,
	      onicecandidate: participant.onIceCandidate.bind(participant),
		  configuration: {
			iceServers: [
				{
				"urls": 'turn:3.35.231.64:3478?transport=udp',
				"username": 'myuser',
				"credential": 'mypassword'
			}]
		}
	    }

	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
		function (error) {
		  if(error) {
			  return console.error(error);
		  }
		  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});
	for (let [userNickname, userId] of Object.entries(msg.infos)) {
		receiveVideo(userId, userNickname)
	}
}

async function getMedia() {
    try {
      myStream = await navigator.mediaDevices.getUserMedia({
      	audio: true,
      	video: true,
      })
    } catch (e) {
      console.log(e);
    }
}

getMedia();

function leaveRoom() {
	sendMessage({
		id : 'leaveRoom'
	});

	for ( var key in participants) {
		participants[key].dispose();
	}

	cameraOff = true
	ws.close();
}

function receiveVideo(userId, nickname) {
	// console.log("receiveVideo에서", userId, nickname)
	var participant = new Participant(userId, nickname);
	participants[userId] = participant;
	var video = participant.getVideoElement();
	
	var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant),
	  configuration: {
		iceServers: [
			{
			"urls": 'turn:3.35.231.64:3478?transport=udp',
			"username": 'myuser',
			"credential": 'mypassword'
		}]
	}
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
	// console.log('Participant ' + request.userId + ' left');
	var participant = participants[request.userId];
	participant.dispose();
	delete participants[request.userId];
}

function sendMessage(message) {
	var jsonMessage = JSON.stringify(message);
	// console.log('Sending message: ' + jsonMessage);
	ws.send(jsonMessage);
}
