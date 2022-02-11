// const socket = io();

const myFace = document.getElementsById("video");
const muteBtn = document.getElementById("mute");
const cameraBtn = document.getElementById("camera off");


let myStream; // stream = video + audio; stream은 track(e.g. 비디오, 오디오, 자막)를 제공해줌
let muted = false; // 음소거 여부 추적 (음소거 안 된 채로 시작)
let cameraOff = false; // 카메라 on/off 추적 (카메라 on으로 시작)

// async function getCameras() {
//     try {
//         const devices = await navigator.mediaDevices.enumerateDevices(); // enumerateDevices() -> 모든 장치(+미디어 장치) 정보를 줌
//         const cameras = devices.filter(device => device.kind === "videoinput") // videoinput이라는 kind를 가진 device만 가져오기
//     } catch(e) {
//         console.log(e);
//     }
// }

// async function getMedia() {
//     try {
//         myStream = await navigator.mediaDevices.getUserMedia( // getUserMedia() -> 유저의 카메라와 오디오를 가져옴
//             {
//                 audio: true,
//                 video: true,
//             }
//         );
//         myFace.srcObject = myStream; // stream을 myFace에 넣기
//         await getCameras();
//     } catch (e) {
//         console.log(e);
//     }
// }
// // navigator.mediaDevices.getUserMedia : 유저의 유저미디어 string을 줌
// getMedia();

function handleMuteClick(){
    myStream
        .getAudioTracks()
        .forEach((track) => (track.enabled = !track.enabled));
    if(!muted){
        muteBtn.innerText = "Unmute"
        muted = true;
    } else {
        muteBtn.innerText = "Mute"
        muted = false;
    }
}
function handleCameraClick(){
    myStream
        .getVideoTracks()
        .forEach((track) => (track.enabled = !track.enabled));
    if(cameraOff){
        cameraBtn.innerText = "Turn Cmaera Off";
        cameraOff = false;
    } else {
        cameraBtn.innerText = "Turn Camera On";
        cameraOff = true;
    }
}

muteBtn.addEventListener("click", handleMuteClick);
cameraBtn.addEventListener("click", handleCameraClick);