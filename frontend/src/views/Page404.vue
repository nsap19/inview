<template>
  <div>
        <Vue3Lottie :animationData="NoResultsJSON" :width="300" />
  </div>
</template>

<script>
import Vue3Lottie from 'vue3-lottie'
import NoResultsJSON from '@/assets/lottie_json/404.json'

export default {
    name : "Page404",
    components: {
    Vue3Lottie
  },
  setup () {

       return { NoResultsJSON}
  }
}
</script>

<style>

</style>