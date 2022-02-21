

var videoInput;
var videoOutput;
var webRtcPeer;
var state;

const NO_CALL = 0;
const IN_CALL = 1;
const POST_CALL = 2;
const DISABLED = 3;
const IN_PLAY = 4;

window.onbeforeunload = function() {
	ws.close();
}

function setState(nextState) {
	switch (nextState) {
	case NO_CALL:
		$('#start').attr('disabled', false);
		$('#stop').attr('disabled', true);
		$('#play').attr('disabled', true);
		break;
	case DISABLED:
		$('#start').attr('disabled', true);
		$('#stop').attr('disabled', true);
		$('#play').attr('disabled', true);
		break;
	case IN_CALL:
		$('#start').attr('disabled', true);
		$('#stop').attr('disabled', false);
		$('#play').attr('disabled', true);
		break;
	case POST_CALL:
		$('#start').attr('disabled', false);
		$('#stop').attr('disabled', true);
		$('#play').attr('disabled', false);
		break;
	case IN_PLAY:
		$('#start').attr('disabled', true);
		$('#stop').attr('disabled', false);
		$('#play').attr('disabled', true);
		break;	
	default:
		onError('Unknown state ' + nextState);
	return;
	}
	state = nextState;
}

function start() {
    var participant = participants[this.userId];

	videoInput = participant.getVideoElement().captureStream();
	// console.log('Starting video call ...');

	// console.log('Creating WebRtcPeer and generating local sdp offer ...');

	var options = {
			localVideo : videoInput,
			remoteVideo : videoInput,
			mediaConstraints : getConstraints(),
			onicecandidate : onIceCandidate
	}

	webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
			function(error) {
		if (error)
			return console.error(error);
		participant.rtcPeer.generateOffer(onOffer);
	});

	console.log(participant.rtcPeer)

}

function onOffer(error, offerSdp) {
	if (error)
		return console.error('Error generating the offer');
	// console.info('Invoking SDP offer callback function ' + location.host);
	var message = {
			id : 'start',
			sdpOffer : offerSdp,
	}
	sendMessage(message);
}

function onError(error) {
	console.error(error);
}

function onIceCandidate(candidate) {
	// console.log('Local candidate' + JSON.stringify(candidate));

	var message = {
			id : 'onIceCandidate',
			candidate : candidate,
			userId: userId
	};
	sendMessage(message);
}

function startResponse(message) {
	setState(IN_CALL);
	// console.log('SDP answer received from server. Processing ...');

	webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
		if (error)
			return console.error(error);
	});
}

function stop() {
	var stopMessageId = 'stop' ;
	// console.log('Stopping video while in ' + state + '...');
	setState(POST_CALL);
	if (webRtcPeer) {
		var message = {
				id : stopMessageId
		}
		sendMessage(message);
	}
}

function getConstraints() {
	var constraints = {
			audio : true,
			video : true
	}
	
	return constraints;
}

