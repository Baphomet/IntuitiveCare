<template>
  <nav class="navbar">
    <div class="container">
      <h1 class="logo">IntuitiveCare</h1>
      <div class="links">
        <a href="#/">Operadoras</a>
        <a href="#/estatisticas">Estatísticas</a>
      </div>
    </div>
  </nav>
</template>

<script setup>
// Navbar simples
</script>

<style scoped>
.navbar {
  background: #f1f1fb;
  box-shadow: 0 2px 8px rgba(83,91,242,0.07);
  padding: 0.5rem 0;
  margin-bottom: 2rem;
}
.container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
}
.logo {
  font-size: 2rem;
  color: #535bf2;
  font-weight: bold;
  letter-spacing: 1px;
}
.links a {
  margin-left: 2rem;
  color: #3a3a6a;
  font-weight: 600;
  text-decoration: none;
  transition: color 0.2s;
  font-size: 1.1rem;
}
.links a:hover {
  color: #535bf2;
}

@media (max-width: 700px) {
  .container {
    flex-direction: column;
    align-items: flex-start;
    padding: 0 1rem;
  }
  .logo {
    font-size: 1.3rem;
    margin-bottom: 0.5rem;
  }
  .links a {
    margin-left: 1rem;
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .navbar {
    padding: 0.3rem 0;
    margin-bottom: 1rem;
  }
  .container {
    padding: 0 0.5rem;
  }
  .logo {
    font-size: 1rem;
    margin-bottom: 0.3rem;
  }
  .links a {
    margin-left: 0.5rem;
    font-size: 0.85rem;
  }
}

@media (max-width: 360px) {
  .logo {
    font-size: 0.85rem;
  }
  .links a {
    margin-left: 0.3rem;
    font-size: 0.75rem;
  }
}
</style>
