<template>
    <div id="container" ref="wholeVideosWrapper" class="meeting-content-main">
        <div id="participants" style="width: 100%; height: 100%" class="d-flex">

        </div>
        <div id="wrapper">
            <div id="join" class="animate join">
                <!-- <h1>Join a Room</h1>
                <form accept-charset="UTF-8">
                    <p>
                    </p>
                    <p>
                    </p>
                    <p class="submit">
                    </p>
                </form> -->
            </div>
            <div id="room">
                <!-- <input type="button" name="commit" value="비디오 참가" id="joinButton" onclick="register(1, 1); return false;" /> -->
                <!-- <input type="button" name="commit" value="비디오 참가" @click="register" /> -->
                <!-- <h2 id="room-header"></h2> -->
            </div>
        </div>
    </div>
    <input type="button" name="commit" value="비디오 참가" id="joinButton" />
    <input type="text" name="userId" value="" id="userId" placeholder="userId" required />
    <input type="text" name="meetingId" value="" id="meetingId" placeholder="meetingId" required />
    <input type="button" id="button-leave" onmouseup="leaveRoom();" value="Leave room" />
</template>

<script>
// import { register } from '../../../public/js/conferenceroom'
import { onMounted, computed, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'Video',
  setup() {
    const wholeVideosWrapper = ref(null)
		let width = ref(0)
		let height = ref(0)
		let windowWidth = ref(0)
		const ratio = 9 / 16  // 비디오 화면 비율 (16: 9)
		const setMargin = 10  // 비디오 margin
		let maxWidth = ref(0)  // 비디오 최대 넓이

    const getArea = function(increment, width, height) {
			let i = 0;
			let w = 0;
			let h = increment * ratio + (setMargin * 2);
			while (i < (participants.length)) {
					if ((w + increment) > width) {
							w = 0;
							h = h + (increment * ratio) + (setMargin * 2);
					}
					w = w + increment + (setMargin * 2);
					i++;
			}
			if (h > height || increment > width) return false;
			else return increment;
    }

		// 비디오의 너비 계산
		const resize = function (width, height) {
			let max = 0
			let i = 1
			while (i < 5000) {
					let area = getArea(i, width, height);
					if (area === false) {
							max = i - 1;
							break;
					}
					i++;
			}
			max = max - (setMargin * 2)  // remove margins
			maxWidth.value = max
		}


    onMounted(() => {
      console.log('mounted!')
      // document.getElementById('userId').value = 1
      // document.getElementById('meetingId').value = 1
      document.getElementById('joinButton').onclick=function(){register(1, 1); return false;};
			
      // 초기 비디오 크기 설정
			// if (wholeVideosWrapper.value) {
			// 	width.value = wholeVideosWrapper.value.offsetWidth
			// 	height.value = wholeVideosWrapper.value.offsetHeight
			// 	resize(width.value, height.value)
			// }

			// 반응형 비디오 크기 설정
			// window.addEventListener('resize', function () {
			// 	if (wholeVideosWrapper.value) {
			// 		width.value = wholeVideosWrapper.value.offsetWidth
			// 		height.value = wholeVideosWrapper.value.offsetHeight
			// 		resize(width.value, height.value)
			// 		windowWidth.value = window.innerWidth
			// 	}
			// })
			// windowWidth.value = window.innerWidth


    })
    const change = function () {
      const participantDiv = document.getElementsByClassName('participant')
      console.log(participantDiv)
      let i = 0
      for(i = 0; i < participantDiv.length; i++) {
        participantDiv[i].style.backgroundColor = 'blue';
      }
    }

    const store = useStore()
    const meeting = computed(() => store.state.meeting)
    const user = computed(() => store.state.user)
    // var script = document.createElement('script');
    // script.src = "../js/kurento-util.js";
    // document.head.appendChild(script); 
    // var script2 = document.createElement('script');
    // script2.src = "../js/conferenceroom.js";
    // document.head.appendChild(script2); 
    // var script3 = document.createElement('script');
    // script3.src = "../js/participant.js";
    // document.head.appendChild(script3); 

    return { wholeVideosWrapper }
  }
}
</script>

<style scoped>
.meeting-content-main {
	/* flex-grow: 1;
	display: flex;
	flex-wrap: wrap;
	justify-content: center; */

	/* overflow: scroll; */
	display: flex;
	align-content: center;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	vertical-align: middle;
	flex: 1;
	border-radius: 10px;
	/* background: rgba(0, 0, 0, 0.3); */

	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
}
</style>