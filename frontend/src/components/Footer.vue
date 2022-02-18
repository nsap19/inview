<template>
  <div id="footer" v-if="!this.$route.path.startsWith('/meeting')">
    <!-- Footer -->
    <footer class="text-center text-lg-start bg-light text-muted">
      <!-- Section: Social media -->
      <section
        class="d-flex justify-content-center justify-content-lg-between p-4 border-bottom"
      >
        <!-- Left -->
        <div class="me-5 d-none d-lg-block">
          <span>Get connected with us on social networks:</span>
        </div>
        <!-- Left -->

        <!-- Right -->
        <div>
        <a href="#" class="me-3 text-reset">
            <i class="bi bi-github"></i>
          </a>
          <a href="#" class="me-3 text-reset">
            <i class="bi bi-youtube"></i>
          </a>
          <a href="" class="me-3 text-reset">
            <i class="bi bi-google"></i>
          </a>
          <a href="" class="me-4 text-reset">
            <i class="bi bi-twitter"></i>
          </a>
          <a href="" class="me-4 text-reset">
            <i class="#"></i>
          </a>
          <a href="" class="me-4 text-reset">
            <i class="#"></i>
          </a>
          
        </div>
        <!-- Right -->
      </section>
      <!-- Section: Social media -->

      <!-- Section: Links  -->
      <section class="">
        <div class="container text-center text-md-start mt-5">
          <!-- Grid row -->
          <div class="row mt-3">
            <!-- Grid column -->
            <div class="col-md-3 col-lg-4 col-xl-3 mx-auto mb-4">
              <!-- Content -->
              <h6 class="text-uppercase fw-bold mb-4">
                <img src="./../assets/logo.png" width ="200px" alt="Inview">
              </h6>
              <p>
                Interview space for everyone.
              </p>
              <p>
                Look Inside Yourself.
              </p>
            </div>
            <!-- Grid column -->

            <!-- Grid column -->
            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4">
              <!-- Links -->
              <h6 class="text-uppercase fw-bold mb-4">Service</h6>
              <p>
                <a href="#!" class="text-reset">사용자 사이트</a>
              </p>
              <p>
                <a href="#!" class="text-reset">관리자 사이트</a>
              </p>
            </div>
            <!-- Grid column -->

            <!-- Grid column -->
            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4">
              <!-- Links -->
              <h6 class="text-uppercase fw-bold mb-4">Useful links</h6>
              <p>
                <a href="#!" class="text-reset">Help</a>
              </p>
              <p>
                <a href="#!" class="text-reset">이용약관</a>
              </p>
              <p>
                <a href="#!" class="text-reset" style="font-weight: bold;">개인정보 처리방침</a>
              </p>
              
            </div>
            <!-- Grid column -->

            <!-- Grid column -->
            <div class="col-md-5 col-lg-3 col-xl-3 mx-auto mb-md-0 mb-4">
              <!-- Links -->
              <h6 class="text-uppercase fw-bold mb-4">Contact</h6>
              <p><i class="bi bi-house-fill me-1"></i>서울특별시 강남구 테헤란로</p>
              <p>
                <i class="bi bi-mailbox me-1"></i>
                ssafy6a201@google.com
              </p>
              <p><i class="bi bi-telephone-fill me-1"></i> + 82 012 3456 7890</p>
              <p><i class="bi bi-clock me-1"></i> 9 to 6 (주말 및 공휴일 휴무)</p>
            </div>
            <!-- Grid column -->
          </div>
          <!-- Grid row -->
        </div>
      </section>
      <!-- Section: Links  -->

      <!-- Copyright -->
      <div
        class="text-center p-4"
        style="background-color: rgba(0, 0, 0, 0.05)"
      >
        © 2022 Copyright:
        <a class="text-reset fw-bold" href="#"
          >Inview</a
        >
        and icon Copyright:
        <a class="text-reset fw-bold" href="https://icons8.com/"
          >icon8</a
        >
      </div>
      <!-- Copyright -->
    </footer>
    <!-- Footer -->
  </div>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style scoped>
#service ul {
  list-style-type: none;
  margin-left: 20px;
}
ul {
  list-style-type: none;
  /* float: left; */
  display: inline;
  margin-left: 20px;
}
li {
  margin-bottom: 20px;
}

a {
  text-decoration: none !important;
}
</style>
