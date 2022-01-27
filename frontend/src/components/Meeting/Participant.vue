<template>
  <div>
    <div 
      v-for="participant in participants" 
      :key="participant.pk" 
      class="d-flex flex-row justify-content-between p-2"
    >
      <p>{{ participant.nickname }}</p>
      <div>
        <i class="bi bi-mic-mute-fill p-2"></i>
        <i class="bi bi-camera-video-off-fill p-2"></i>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Mute } from '@element-plus/icons-vue'

export default defineComponent({
  name: 'Participant',
  setup() {
    const participants = [
      { pk: 1, nickname: '바나나알러지원숭이'},
      { pk: 2, nickname: '파리바게뜨구름크림빵'},
      { pk: 3, nickname: '히어로오브더스톰'},
      { pk: 4, nickname: '남아일언중천금'},
      { pk: 5, nickname: '크리스마스'},
      { pk: 6, nickname: '어'},
    ]

    return { participants, Mute }
  }
})
</script>

<style>

</style>