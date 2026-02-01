
<script setup>
import Navbar from './components/Navbar.vue'
</script>


<template>
  <div class="app-bg">
    <Navbar />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.app-bg {
  min-height: 100vh;
  min-width: 100vw;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #f8fafc 0%, #e0e7ff 100%);
  display: flex;
  flex-direction: column;
}
.main-content {
  flex: 1;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem 1rem 3rem 1rem;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
@media (max-width: 900px) {
  .main-content {
    max-width: 100vw;
    padding: 1rem 0.5rem 2rem 0.5rem;
  }
}

@media (max-width: 600px) {
  .app-bg {
    height: auto;
  }
  .main-content {
    padding: 0.8rem 0.3rem 1.5rem 0.3rem;
  }
}

@media (max-width: 400px) {
  .main-content {
    padding: 0.5rem 0.2rem 1rem 0.2rem;
  }
}
</style>
