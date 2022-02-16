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

const PARTICIPANT_MAIN_CLASS = "participant main";
const PARTICIPANT_CLASS = "participant";

/**
 * Creates a video element for a new participant
 *
 * @param {int} userId - the name of the new participant, to be used as tag
 *                        name of the video element.
 *                        The tag of the new element will be 'video<name>'
 * @return
 */
function Participant(userId, userNickname) {
  console.log('participant에서 받은', userId, userNickname)
  this.userId = userId;
  var container = document.createElement("div");
  container.className = isPresentMainParticipant()
    ? PARTICIPANT_CLASS
    : PARTICIPANT_MAIN_CLASS;
  container.id = userId;
  var span = document.createElement("span");
  const video = document.createElement("video");
  var rtcPeer;

	video.setAttribute('style', 'width: 100%; height: 100%;')
	container.setAttribute('style', 'position: relative; vertical-align: middle; align-self: center; border-radius: 10px; overflow: hidden; display: inline-block; box-shadow: var(--shadow-dark); background: #fff; animation: show 0.4s ease;')
	const setMargin = 10
	const ratio = 9/16

  function getArea(increment) {
    let i = 0;
    let w = 0;
    let h = increment * ratio + setMargin * 2;

    const width = document.getElementById("container").offsetWidth;
    const height = document.getElementById("container").offsetHeight;
    // console.log('getArea', width, height)

    while (i < document.getElementsByClassName("participant").length) {
      if (w + increment > width) {
        w = 0;
        h = h + increment * ratio + setMargin * 2;
      }
      w = w + increment + setMargin * 2;
      i++;
    }
    if (h > height || increment > width) return false;
    else return increment;

  }

  function resize() {
    let max = 0;
    let i = 1;
    while (i < 5000) {
      let area = getArea(i);
      if (area === false) {
        max = i - 1;
        break;
      }
      i++;
    }
    max = max - setMargin * 2; // remove margins
    resizer(max);
  }

  function resizer(width) {
    const participant = document.getElementsByClassName("participant");
    for (var s = 0; s < participant.length; s++) {
      let element = participant[s];
      // custom margin
      element.style.margin = setMargin + "px";
      // calculate dimensions
      element.style.width = width + "px";
      element.style.height = width * ratio + "px";
    }
  }

  container.appendChild(video);
  const nicknameWrapper = document.createElement('div')
  nicknameWrapper.className = 'nickname-wrapper'
	const nickname = document.createElement('div')
	nickname.className = 'nickname'
	nickname.textContent = userNickname
	nicknameWrapper.appendChild(nickname)
	container.appendChild(nicknameWrapper)

	// container.appendChild(span);
	container.onclick = switchContainerClass;
	document.getElementById('participants').appendChild(container);

  span.appendChild(document.createTextNode(userId));

  video.id = "video-" + userId;
  video.autoplay = true;
  video.controls = false;
  resize();

  getMedia(userId);
  
  this.getElement = function () {
    return container;
  };

  this.getVideoElement = function () {
    return video;
  };

  function switchContainerClass() {
    if (container.className === PARTICIPANT_CLASS) {
      var elements = Array.prototype.slice.call(
        document.getElementsByClassName(PARTICIPANT_MAIN_CLASS)
      );
      elements.forEach(function (item) {
        item.className = PARTICIPANT_CLASS;
      });

      container.className = PARTICIPANT_MAIN_CLASS;
    } else {
      container.className = PARTICIPANT_CLASS;
    }
  }

  function isPresentMainParticipant() {
    return document.getElementsByClassName(PARTICIPANT_MAIN_CLASS).length != 0;
  }

  this.offerToReceiveVideo = function (error, offerSdp, wp) {
    if (error) return console.error("sdp offer error");
    console.log("Invoking SDP offer callback function");
    var msg = { id: "receiveVideoFrom", sender: userId, sdpOffer: offerSdp };
    sendMessage(msg);
  };

  this.onIceCandidate = function (candidate, wp) {
    console.log("Local candidate" + JSON.stringify(candidate));

    var message = {
      id: "onIceCandidate",
      candidate: candidate,
      userId: userId,
    };
    sendMessage(message);
  };

  Object.defineProperty(this, "rtcPeer", { writable: true });

  this.dispose = function () {
    console.log("Disposing participant " + this.userId);
    this.rtcPeer.dispose();
    container.parentNode.removeChild(container);
  };
}