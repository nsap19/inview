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
import { defineComponent, computed } from 'vue'
import { Mute } from '@element-plus/icons-vue'
import { useStore } from 'vuex'

export default defineComponent({
  name: 'Participant',
  setup() {
    const store = useStore()
    const participants = computed(() => store.state.participants)

    return { participants, Mute }
  }
})
</script>

<style>

</style>