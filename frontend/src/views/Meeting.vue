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
		<div class="d-flex flex-row justify-content-end p-3 border meeting-nav">
			<el-button type="warning">준비</el-button>
			<el-button type="danger" @click="endMeeting">나가기</el-button>
		</div>

		<!-- 미팅 메인 -->
		<div class="d-flex flex-row border meeting-content">
			<div class="border meeting-content-main" ref="wholeVideosWrapper" >
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
			</div>

			<!-- 우측 aside -->
			<div v-show="openAside" class="border meeting-content-aside">
				<div class="border d-flex flex-row justify-content-between p-2 align-items-center">
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
		<div class="d-flex flex-row justify-content-end p-3 border meeting-footer">
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

<script lang="ts">
import { defineComponent, ref, onMounted, watch } from 'vue'
import ChooseQuestion from '@/components/Meeting/ChooseQuestion.vue';
import Participant from '@/components/Meeting/Participant.vue';
import Evaluation from '@/components/Meeting/Evaluation.vue';
import Chat from '@/components/Meeting/Chat.vue';
import Memo from '@/components/Meeting/Memo.vue';
import File from '@/components/Meeting/File.vue';
import { ChatDotSquare, CloseBold, MoreFilled, List } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'


export default defineComponent({
	name: 'Meeting',
	components: {
		ChooseQuestion,
		Participant,
		Evaluation,
		Chat,
		Memo,
		File
	},
	setup() {
		const wholeVideosWrapper = ref()

		onMounted(() => {
			// 초기 비디오 크기 설정
			const width = wholeVideosWrapper.value.offsetWidth
			const height = wholeVideosWrapper.value.offsetHeight
			resize(width, height)

			// 반응형 비디오 크기 설정
			window.addEventListener('resize', function () {
				const width = wholeVideosWrapper.value.offsetWidth
				const height = wholeVideosWrapper.value.offsetHeight
				resize(width, height)
			})
		})

		const ratio = 9 / 16  // 비디오 화면 비율 (16: 9)
		const setMargin = 10  // 비디오 margin
		let maxWidth = ref(0)  // 비디오 최대 넓이

    const getArea = function(increment: number, width: number, height: number) {
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
		const resize = function (width: number, height: number) {
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
			// remove margins
			max = max - (setMargin * 2);
			maxWidth.value = max
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

		watch(openAside, (oldVal) => {
			const width = oldVal ? wholeVideosWrapper.value.offsetWidth - 420 : wholeVideosWrapper.value.offsetWidth + 420
			const height = wholeVideosWrapper.value.offsetHeight
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
			wholeVideosWrapper, maxWidth, ratio, setMargin,
			handleClose, endMeeting
		}
	},
})
</script>

<style scoped>
.meeting-container {
	display: flex;
  flex-flow: column;
  height: 100vh;
}

.meeting-nav {
  flex: 0 1 auto;
	flex: 0 1 66px;
  /* The above is shorthand for:
  flex-grow: 0,
  flex-shrink: 1,
  flex-basis: auto
  */
}

.meeting-content {
	flex: 1 1 auto;
	width: 100vw;
	height: 500px;
}

.meeting-footer {
	flex: 0 1 66px;
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
	background: rgba(0, 0, 0, 0.3);
}

.meeting-content-aside {
	width: 420px;
	display: flex;
	flex-direction: column;
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