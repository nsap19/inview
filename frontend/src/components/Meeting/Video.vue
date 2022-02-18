<template>
    <div id="container" ref="wholeVideosWrapper" class="meeting-content-main">
        <div id="participants" style="" class="text-center">
        </div>
    </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'Video',
  setup() {
    const wholeVideosWrapper = ref(null)

    return { wholeVideosWrapper }
  }
}
</script>

<style scoped>
.meeting-content-main {
	display: flex;
	align-content: center;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	vertical-align: middle;
	flex: 1;
	border-radius: 10px;
	box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
	margin: 5px;
}
</style>