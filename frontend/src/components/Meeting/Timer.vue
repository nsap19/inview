<template>
  <div class="container">
    <div class="row">
      <Vue3Lottie
        @click="startCountdown"
        :disabled="counting[0]"
        :animationData="StartImg"
        :width="300"
      />
    </div>
    <div class="row">
      <div v-show="counting[0]">
          <div class="col">
              <Vue3Lottie ref="timer1" :animationData="TimerImg" :width="100" :autoPlay="false" />
          </div>
          <div class="col">
              {{ time[0] }}
          </div>
      </div>
      <span v-if="!counting[0]"> finish!!</span>
    </div>
    <div class="row">
      <div v-show="counting[1]">
        <Vue3Lottie ref="timer2" :animationData="TimerImg" :width="100" :autoPlay="false" /> Time
        Remaining:
        {{ time[1] }}
      </div>
      <span v-if="!counting[1]"> finish!!</span>
    </div>
    <div class="row">
      <div v-show="counting[2]">
        <Vue3Lottie ref="timer3" :animationData="TimerImg" :width="100" :autoPlay="false" />Time
        Remaining:
        {{ time[2] }}
      </div>
      <span v-if="!counting[2]"> finish!!</span>
    </div>
  </div>
</template>

<script>
import Vue3Lottie from "vue3-lottie";
import StartImg from "@/assets/lottie_json/Start.json";
import TimerImg from "@/assets/lottie_json/Timer.json";

const TEN_MINUTES = 60 * 10;
const second30 = 30;
export default {
  name: "Timer",
  components: {
    Vue3Lottie,
  },
  filters: {
    second60(value) {
      return value + second30;
    },
    second90(value) {
      return value + second30 * 2;
    },
    minutesAndSeconds(value) {
      var minutes = Math.floor(parseInt(value, 10) / 60);
      var seconds = parseInt(value, 10) - minutes * 60;
      return `${minutes}:${seconds}`;
    },
  },
  mounted() {
    setInterval(() => {
      for (var i = 0; i < this.time.length; i++) {
        this.time[i]--;
        if (this.time[i] == 0) {
          this.counting[i] = false;
        }
      }
    }, 1000);
  },
  data() {
    return {
      TimerImg,
      StartImg,
      timer: TEN_MINUTES,
      counting: [false, false, false],
      time: [second30, second30 * 2, second30 * 3],
      timerName :['timer1', 'timer2', 'timer3'],
    };
  },
  methods: {
    startCountdown: function () {
        
      for (var i = 0; i < this.time.length; i++) {
            this.counting[i] = false;
     }
     if(this.$refs['timer1'] != undefined && this.$refs['timer2'] != undefined && this.$refs['timer3'] != undefined){
              this.$refs['timer3'].stop();
              this.$refs['timer2'].stop();
              this.$refs['timer1'].stop();          
     }
      for (var i = 0; i < this.time.length; i++) {
        this.time[i] = second30 * (i + 1);
        this.counting[i] = true;
      }
      if(this.$refs['timer1'] != undefined && this.$refs['timer2'] != undefined && this.$refs['timer3'] != undefined){
         this.$refs['timer3'].play();
         this.$refs['timer2'].play();
         this.$refs['timer1'].play();          
      }
    },
  },
};
</script>

<style></style>
