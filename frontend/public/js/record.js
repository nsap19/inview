

var videoInput;
var videoOutput;
var webRtcPeer;
var state;

const NO_CALL = 0;
const IN_CALL = 1;
const POST_CALL = 2;
const DISABLED = 3;
const IN_PLAY = 4;

// window.onload = function() {
// 	// console = new Console();
// 	console.log('Page loaded ...');
//     participants[this.userId] = participant;

// 	videoInput = participant.getVideoElement();
// 	videoOutput = participant.getVideoElement();
// 	setState(NO_CALL);
// }

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

	videoInput = participant.getVideoElement();
	videoOutput = participant.getVideoElement();
	console.log('Starting video call ...');

	console.log('Creating WebRtcPeer and generating local sdp offer ...');

	var options = {
			localVideo : videoInput,
			remoteVideo : videoOutput,
			mediaConstraints : getConstraints(),
			onicecandidate : onIceCandidate
	}

	webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
			function(error) {
		if (error)
			return console.error(error);
		webRtcPeer.generateOffer(onOffer);
	});

	console.log(webRtcPeer)

}

function onOffer(error, offerSdp) {
	if (error)
		return console.error('Error generating the offer');
	console.info('Invoking SDP offer callback function ' + location.host);
	var message = {
			id : 'start',
			sdpOffer : offerSdp,
			mode :  $('input[name="mode"]:checked').val()
	}
	sendMessage(message);
}

function onError(error) {
	console.error(error);
}

function onIceCandidate(candidate) {
	console.log('Local candidate' + JSON.stringify(candidate));

	var message = {
			id : 'onIceCandidate',
			candidate : candidate,
	};
	sendMessage(message);
}

function startResponse(message) {
	setState(IN_CALL);
	console.log('SDP answer received from server. Processing ...');

	webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
		if (error)
			return console.error(error);
	});
}

function stop() {
	var stopMessageId = 'stop' ;
	console.log('Stopping video while in ' + state + '...');
	setState(POST_CALL);
	if (webRtcPeer) {
		// webRtcPeer.dispose();
		// webRtcPeer = null;

		var message = {
				id : stopMessageId
		}
		sendMessage(message);
	}
	// hideSpinner(videoInput, videoOutput);
}

function play() {
	console.log("Starting to play recorded video...");

	// Disable start button
	setState(DISABLED);
	// showSpinner(videoOutput);

	console.log('Creating WebRtcPeer and generating local sdp offer ...');

	var options = {
			remoteVideo : videoOutput,
			mediaConstraints : getConstraints(),
			onicecandidate : onIceCandidate
	}

	webRtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function(error) {
		if (error)
			return console.error(error);
		webRtcPeer.generateOffer(onPlayOffer);
	});
}

function onPlayOffer(error, offerSdp) {
	if (error)
		return console.error('Error generating the offer');
	console.info('Invoking SDP offer callback function ' + location.host);
	var message = {
			id : 'play',
			sdpOffer : offerSdp
	}
	sendMessage(message);
}

function getConstraints() {
	var mode = $('input[name="mode"]:checked').val();
	var constraints = {
			audio : true,
			video : true
	}

	if (mode == 'video-only') {
		constraints.audio = false;
	} else if (mode == 'audio-only') {
		constraints.video = false;
	}
	
	return constraints;
}


function playResponse(message) {
	setState(IN_PLAY);
	webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
		if (error)
			return console.error(error);
	});
}

function playEnd() {
	setState(POST_CALL);
	// hideSpinner(videoInput, videoOutput);
}

// function sendMessage(message) {
// 	var jsonMessage = JSON.stringify(message);
// 	console.log('Sending message: ' + jsonMessage);
// 	ws.send(jsonMessage);
// }

// /**
//  * Lightbox utility (to display media pipeline image in a modal dialog)
//  */
// $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
// 	event.preventDefault();
// 	$(this).ekkoLightbox();
// });