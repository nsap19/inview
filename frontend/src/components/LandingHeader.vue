<template>
  <div class="header">
    <header class="p-3 mb-3 border-bottom">
      <div class="container d-block d-sm-block d-md-none">
          <br><br><br><br>
      </div>
      <div class="container d-none d-sm-block">
        <div
        >
          <div class="row">
            <div class="col-3">
              <a
                href="/"
                class="d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none"
              >
                <img src="./../assets/logo.png" width="180px" alt="Inview" />
              </a>
            </div>
            <div class="col">
              <ul
                class="nav col-12 col-lg-auto me-lg-auto mb-2 mb-md-0"
              >
                <li>
                  <a href="#" class="nav-link px-2 link-secondary">Overview</a>
                </li>
                <li><a href="#" class="nav-link px-2 link-dark">Home</a></li>
              </ul>
            </div>
            <div class="col">
              <SearchBar></SearchBar>
            </div>
          </div>
        </div>
      </div>
    </header>
  </div>
</template>

<script>
import SearchBar from "./SearchBar.vue";

export default {
  name: "LandingHeader",
  components : {
      SearchBar,
  }
};
</script>

<style></style>
