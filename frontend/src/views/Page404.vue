<template>
  <div class="container text-center d-flex w-100 h-100 p-3 mx-auto flex-column">
    <div class="row mt-3 mb-3">
      <div class="col"></div>
      <div class="col">
        <Vue3Lottie :animationData="NoResultsJSON" :width="width" />
      </div>
      <div class="col">
      </div>
    </div>
    <div class="row mb-3">
      <h2><b>Page Not Found</b></h2>
      <p>We're sorry. the page you requested could not be found</p>
      <p>please go back to the homepage</p>
    </div>
    <div class="row mb-3">
      <div class="col-4"></div>
      <div class="col-4">
          <a href="/home" class="button4">GO HOME</a>
      </div>
      <div class="col-4"></div>
    </div>
  </div>
</template>

<script>
import Vue3Lottie from "vue3-lottie";
import NoResultsJSON from "@/assets/lottie_json/404.json";
import { onMounted, ref } from "vue";

export default {
  name: "Page404",
  components: {
    Vue3Lottie,
  },
  setup() {
    onMounted(() => {
      window.addEventListener("resize", handleResize);
    });
    const width = ref(window.innerWidth / 4);
    const height = ref(window.innerHeight / 4);

    const handleResize = function (event) {
      width.value = window.innerWidth / 4;
      height.value = window.innerHeight / 4;
      console.log(width.value);
    };
    return { NoResultsJSON, handleResize, width, height };
  },
};
</script>

<style lang="scss" scoped>
a {
  text-decoration: none !important;
}
p { color: #111111; margin: 2px 0px 2px 0px; }

.button1 {
  background-color: #cee5d0;
  color: white;
  font-size: calc(2vmin);
  border-radius: 8px;
  padding: 14px 40px;
  width : calc(3vmin);
}

.button2 {
  background-color: #f3f0d7;
  color: gray;
  font-size: calc(2vmin);
  border-radius: 8px;
  padding: 14px 40px;
  width : calc(3vmin);
}

.button3 {
  background-color: #e0c097;
  color: white;
  font-size: calc(2vmin);
  border-radius: 8px;
  padding: 14px 40px;
  width : calc(3vmin);
}

.button4 {
  background-color: #ff7878;
  color: white;
  font-size: calc(2vmin);
  border-radius: 8px;
  padding: 14px 40px;
  width : calc(3vmin);
}

</style>
