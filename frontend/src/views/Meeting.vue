<template>
	<div class="meeting-container">
		<!-- 면접 질문 선택 모달 -->
		<el-dialog
			v-model="dialogVisible"
			title="면접 질문 선택"
			width="80%"
			:close-on-click-modal="false"
			:close-on-press-escape="false"
			:show-close="false"
		>
			<div style="max-height: 500px; overflow-y: auto">
				<el-scrollbar height="480px">
					<ChooseQuestion />
				</el-scrollbar>
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-button type="primary" @click="handleClose"
						>선택</el-button
					>
				</span>
			</template>
		</el-dialog>

		<!-- 미팅 네비바 -->
		<MeetingNavBar />

		<!-- 미팅 메인 -->
		<div class="meeting-content">
			<!-- <div class="meeting-content-main" ref="wholeVideosWrapper" >
				<div 
					class="video-wrapper"
					v-for="participant in participants" 
					:key="participant"
					:style="{margin: setMargin + 'px', width: maxWidth + 'px', height: maxWidth * ratio + 'px'}"
					> 
					<div class="video">영상</div> 
					<div class="video-info-wrapper">
						<div class="video-info">
							{{ participant }}
						</div>
					</div>
				</div>
			</div> -->
			<Video/>

			<!-- 우측 aside -->
			<div 
				v-show="openAside" 
				class="meeting-content-aside" 
				:style="600 < windowWidth ? {'width': '420px'} : {'width': windowWidth - 10 + 'px'}"
			>
				<!-- {{windowWidth}} -->
				<div class="d-flex flex-row justify-content-between p-2 align-items-center">
					<span v-if="asideCategory.slice(0, 10) === 'evaluation'">{{ asideCategory.slice(10) }}님의 면접 평가</span>
					<span v-else>{{ categoryKorName[asideCategory] }}</span>
					<el-button :icon="CloseBold" circle @click="[openAside=!openAside, asideCategory='']" type="text" ></el-button>
				</div>
				
				<Participant v-show="asideCategory === 'participant'" />
				<div v-for="participant in participants" :key="participant">
					<Evaluation 
						:userId="participant" 
						:endSignal="endSignal" 
						v-show="asideCategory === 'evaluation' + participant.toString()" />
				</div>
				<Chat :endSignal="endSignal" v-show="asideCategory === 'chat'" />
				<Memo v-model="memo" v-show="asideCategory === 'memo'" />
				<File v-show="asideCategory === 'file'" />
			</div>
		</div>

		<!-- meeting footer -->
		<div class="d-flex flex-row justify-content-end p-3 meeting-footer">
			<input type="button" name="commit" value="비디오 참가" id="joinButton" />
			<input type="text" name="userId" :value="this.$store.state.user.id" id="userId" placeholder="userId" required />
			<input type="text" name="meetingId" :value="this.$store.state.meeting.id" id="meetingId" placeholder="meetingId" required />
			<el-dropdown size='large' class="mx-3">
				<el-button type="primary" size="large" circle :icon="List"></el-button>
				<template #dropdown>
					<el-dropdown-menu>
						<el-dropdown-item 
							@click="[
								openAside=!(openAside && asideCategory==='evaluation' + participant.toString()), 
								asideCategory=(openAside) ? 'evaluation' + participant.toString():''
							]"
							v-for="participant in participants"
							:key="participant"
						>{{ participant }}님의 평가지</el-dropdown-item>
					</el-dropdown-menu>
				</template>
			</el-dropdown>

			<el-button :icon="ChatDotSquare" size="large" class="me-3" circle @click="[openAside=!(openAside && asideCategory==='chat'), asideCategory=(openAside) ? 'chat':'']"></el-button>
			
			<el-dropdown trigger="click" size='large'>
				<el-button type="primary" size="large" circle :icon="MoreFilled"></el-button>
				<template #dropdown>
					<el-dropdown-menu>
						<el-dropdown-item @click="[openAside=!(openAside && asideCategory==='memo'), asideCategory=(openAside) ? 'memo':'']">메모</el-dropdown-item>
						<el-dropdown-item @click="[openAside=!(openAside && asideCategory==='timer'), asideCategory=(openAside) ? 'timer':'']">타이머</el-dropdown-item>
						<el-dropdown-item @click="[openAside=!(openAside && asideCategory==='file'), asideCategory=(openAside) ? 'file':'']">파일전송</el-dropdown-item>
						<el-dropdown-item @click="[openAside=!(openAside && asideCategory==='participant'), asideCategory=(openAside) ? 'participant':'']">참가자</el-dropdown-item>
					</el-dropdown-menu>
				</template>
			</el-dropdown>
		</div>

	</div>
</template>

<script>
import { defineComponent, ref, onMounted, watch } from 'vue'
import ChooseQuestion from '@/components/Meeting/ChooseQuestion.vue';
import Participant from '@/components/Meeting/Participant.vue';
import Evaluation from '@/components/Meeting/Evaluation.vue';
import Chat from '@/components/Meeting/Chat.vue';
import Memo from '@/components/Meeting/Memo.vue';
import File from '@/components/Meeting/File.vue';
import MeetingNavBar from '@/components/Meeting/MeetingNavBar.vue'
import { ChatDotSquare, CloseBold, MoreFilled, List } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import Video from '@/components/Meeting/Video.vue'


export default defineComponent({
	name: 'Meeting',
	components: {
		MeetingNavBar,
		ChooseQuestion,
		Participant,
		Evaluation,
		Chat,
		Memo,
		File,
		Video
	},
	setup() {
		const wholeVideosWrapper = ref(null)

		let width = ref(0)
		let height = ref(0)
		let windowWidth = ref(0)

		onMounted(() => {
			document.getElementById('joinButton').onclick=function(){register(1, 1); return false;};
			// var script = document.createElement('script');
			// script.src = "../js/kurento-util.js";
			// document.head.appendChild(script); 
			// var script2 = document.createElement('script');
			// script2.src = "../js/conferenceroom.js";
			// document.head.appendChild(script2); 
			// var script3 = document.createElement('script');
			// script3.src = "../js/participant.js";
			// document.head.appendChild(script3); 

			// 초기 비디오 크기 설정
			// if (wholeVideosWrapper.value) {
			// 	width.value = wholeVideosWrapper.value.offsetWidth
			// 	height.value = wholeVideosWrapper.value.offsetHeight
			// 	resize(width.value, height.value)
			// }

			// 반응형 비디오 크기 설정
			window.addEventListener('resize', function () {
				console.log('window 크기 변경 resize좀 제발')
				// const width = document.getElementById('container').offsetWidth
				let width =  openAside.value ? window.innerWidth - 420 : document.getElementById('container').offsetWidth
				// let width = oldVal ? document.getElementById('container').offsetWidth - 420 : document.getElementById('container').offsetWidth + 420
				if (window.innerWidth < 600) {
					width = window.innerWidth
				}
				const height = document.getElementById('container').offsetHeight
				resize(width, height)
				// if (wholeVideosWrapper.value) {
				// 	width.value = wholeVideosWrapper.value.offsetWidth
				// 	height.value = wholeVideosWrapper.value.offsetHeight
				// 	resize(width.value, height.value)
				// 	windowWidth.value = window.innerWidth
				// }
				windowWidth.value = window.innerWidth
			})
		})

		const ratio = 9 / 16  // 비디오 화면 비율 (16: 9)
		const setMargin = 10  // 비디오 margin
		let maxWidth = ref(0)  // 비디오 최대 넓이

    const getArea = function(increment, width, height) {
			let i = 0;
			let w = 0;
			let h = increment * ratio + (setMargin * 2);
			while (i < (document.getElementsByClassName('participant')).length) {
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
			console.log('resize한다', width, height)
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
			console.log("max", max)
			resizer(max)
		}

		function resizer(width) {
			console.log('resizer한다', width)
			const participant = document.getElementsByClassName('participant')
			for (var s = 0; s < participant.length; s++) {
					let element = participant[s];
					// custom margin
					element.style.margin = setMargin + "px"
					// calculate dimensions
					element.style.width = width + "px"
					element.style.height = (width * ratio) + "px"
			}
		}

		const openAside = ref(false)
		const asideCategory = ref('')
		const categoryKorName = {
			'chat': '채팅',
			'memo': '메모',
			'participant': '참가자',
			'file': '파일 전송',
			'timer': '타이머'
		}
		const dialogVisible = ref(false)

		watch(windowWidth, (oldVal, newVal) => {
			if (newVal <= 600) {
				console.log('ohoho')
			}
		})

		watch(openAside, (oldVal) => {
			let width = oldVal ? document.getElementById('container').offsetWidth - 420 : document.getElementById('container').offsetWidth + 420
			let height = document.getElementById('container').offsetHeight
			if (window.innerWidth < 600) {
				width = window.innerWidth
				height = oldVal ? (window.innerHeight - 150) / 2 : window.innerHeight - 150
			}
			console.log(width)
			resize(width, height)
		})

		// 이후 ChooseQuestion 컴포넌트로 옮길 것
		const handleClose = () => {
			ElMessageBox.confirm(
				'선택하신 면접 질문으로 모의 면접을 구성합니다.',
				'면접 질문 확정',
				{
					confirmButtonText: '확정',
					cancelButtonText: '다시 고르기',
				}
				)
				.then(() => {
					console.log('면접 질문 선택 완료')
					console.log('what')
					dialogVisible.value = false
					console.log(dialogVisible)
				})
				// .catch(() => {
				// 	// catch error
				// 	console.log('다시 면접 질문 고르기')
				// 	console.log(dialogVisible)
				// })	
		}

		const memo = ref("")

		const endSignal = ref(false)  

		const participants = [142, 123, 2354, 12354326423, 4234, 1]

		const endMeeting = function () {
			endSignal.value = !endSignal.value
			// 메모 저장은 Memo 컴포넌트에서 하는게 더 논리적일듯
		}


		return { 
			ChatDotSquare, CloseBold, MoreFilled, List, 
			openAside, asideCategory, dialogVisible, memo, endSignal, participants, categoryKorName,
			wholeVideosWrapper, maxWidth, ratio, setMargin, width, height, windowWidth,
			handleClose, endMeeting,
		}
	},
})
</script>

<style scoped>
.meeting-container {
	display: flex;
  flex-flow: column;
  height: 100vh;
	/* background-color: #EAE9E0; */
	background-color: #F4F4F5;
}

.meeting-content {
	flex: 1 1 auto;
	/* width: 100vw; */
	height: 500px;
	display: flex;
	flex-direction: row;
}

@media screen and (max-width: 600px) {
	.meeting-content {
		flex-direction: column;
		height: 250px;
	}
}

.meeting-footer {
	flex: 0 1 66px;
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
}

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

.meeting-content-aside {
	width: 420px;
	display: flex;
	flex-direction: column;
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
}

@media screen and (max-width: 600px) {
	.meeting-content-aside {
		height: 50%;
	}
	.meeting-content-main {
		height: 160px;
	}
}

.video-wrapper {
	/* background-color: lightslategray; */
	/* background-clip: content-box; */

  /* width: 33%;
	margin: 1px;
	display: flex;
	flex-direction: column;
	justify-content: center;
	position: relative; */

	position: relative;
	vertical-align: middle;
	align-self: center;
	border-radius: 10px;
	overflow: hidden;
	display: inline-block;
	box-shadow: var(--shadow-dark);
	background: #fff;
	animation: show 0.4s ease;
}

/* @media (max-width: 1024px) {
	.video-wrapper {
		width: 49%;
		height: 33%;
  }
} */

.video {
	/* background: lightsteelblue;
	aspect-ratio: 16/9;
	width: 100%;
	margin: auto 0; */

	position: absolute;
	right: 0;
	object-fit: cover;
	bottom: 0;
	width: 100%;
	height: 100%;
	background: #000;
	border-radius: 10px;
	overflow: hidden;
	left: 0;
	top: 0;
	background-size: cover;
	overflow: hidden;
	-webkit-transition: margin-top 1s ease-in-out;
	-moz-transition: margin-top 1s ease-in-out;
	-o-transition: margin-top 1s ease-in-out;
	transition: margin-top 1s ease-in-out;
}

.video-info-wrapper {
	position: absolute;
	display: none;
	/* background: black; */
	aspect-ratio: 16/9;
	width: 100%;
	margin: auto 0;
	background-clip: content-box;
	padding: 1px;
}

.video:hover + .video-info-wrapper, .video-info-wrapper:hover {
	display: block;
}

.video-info {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	text-align: center;
	background: #667267;
	/* background-clip: content-box; */
	/* margin: 1px; */
	padding: 5px;
	color: #FEFEFE;
}
</style>