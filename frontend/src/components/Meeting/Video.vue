<template>
    <div id="container">
        <div id="wrapper">
            <div id="join" class="animate join">
                <input style="display: none;" type="text" name="userId" value="" id="userId" placeholder="userId" required />
                <input style="display: none;" type="text" name="meetingId" value="" id="meetingId" placeholder="meetingId" required />
                <!-- <h1>Join a Room</h1>
                <form accept-charset="UTF-8">
                    <p>
                    </p>
                    <p>
                    </p>
                    <p class="submit">
                    </p>
                </form> -->
                <input type="button" name="commit" value="비디오 참가" id="joinButton" />
            </div>
            <div id="room">
                <!-- <input type="button" name="commit" value="비디오 참가" id="joinButton" onclick="register(1, 1); return false;" /> -->
                <!-- <input type="button" name="commit" value="비디오 참가" @click="register" /> -->
                <h2 id="room-header"></h2>
                <div id="participants"></div>
                <input type="button" id="button-leave" onmouseup="leaveRoom();" value="Leave room" />
            </div>
        </div>
    </div>
</template>

<script>
// import { register } from '../../../public/js/conferenceroom'
import { onMounted, computed } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'Video',
  setup() {
    onMounted(() => {
      console.log('mounted!')
      document.getElementById('userId').value = meeting.id
      document.getElementById('meetingId').value = user.id
      document.getElementById('joinButton').onclick=function(){register(user.id, meeting.id); return false;};
    })

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

    return {  }
  }
}
</script>

<style>

</style>