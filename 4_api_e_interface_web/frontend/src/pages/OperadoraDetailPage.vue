<template>
  <section>
    <h2 class="title">Detalhes da Operadora</h2>
    <div v-if="store.loading" class="loading">Carregando...</div>
    <div v-else-if="store.error" class="error">{{ store.error }}</div>
    <div v-else>
      <div class="details-card">
        <div><b>CNPJ:</b> {{ store.operadora?.cnpj }}</div>
        <div><b>Registro:</b> {{ store.operadora?.registro_operadora }}</div>
        <div><b>Razão Social:</b> {{ store.operadora?.razao_social }}</div>
        <div><b>Nome Fantasia:</b> {{ store.operadora?.nome_fantasia }}</div>
        <div><b>Modalidade:</b> {{ store.operadora?.modalidade }}</div>
        <div><b>UF:</b> {{ store.operadora?.uf }}</div>
        <div><b>Data Registro ANS:</b> {{ store.operadora?.data_registro_ans }}</div>
      </div>
      <h3 class="subtitle">Histórico de Despesas</h3>
      <div v-if="store.loadingDespesas" class="loading">Carregando despesas...</div>
      <div v-else-if="store.errorDespesas" class="error">{{ store.errorDespesas }}</div>
      <table v-else-if="store.despesas.length" class="despesas-table">
        <thead>
          <tr>
            <th>Ano</th>
            <th>Trimestre</th>
            <th>Valor</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="d in store.despesas" :key="d.ano + d.trimestre">
            <td>{{ d.ano }}</td>
            <td>{{ d.trimestre }}</td>
            <td>R$ {{ d.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="!store.despesas.length && !store.loadingDespesas" class="empty">Nenhuma despesa encontrada.</div>
    </div>
  </section>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useOperadoraDetailStore } from '../stores/operadoraDetail'

const route = useRoute()
const cnpj = route.params.cnpj
const store = useOperadoraDetailStore()

onMounted(() => {
  store.fetchOperadora(cnpj)
  store.fetchDespesas(cnpj)
})
</script>

<style scoped>
.title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #2c3e50;
}
.subtitle {
  font-size: 1.3rem;
  margin: 2rem 0 1rem 0;
  color: #535bf2;
}
.details-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 1.5rem;
  margin-bottom: 2rem;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 0.7rem 2rem;
}
.despesas-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.despesas-table th, .despesas-table td {
  padding: 0.75rem 1rem;
  text-align: left;
}
.despesas-table th {
  background: #e0e7ff;
  color: #2c3e50;
  font-weight: 600;
}
.despesas-table tr:nth-child(even) {
  background: #f8fafc;
}
.loading {
  text-align: center;
  color: #535bf2;
  font-size: 1.2rem;
  margin: 2rem 0;
}
.error {
  color: #e53e3e;
  background: #fff0f0;
  border: 1px solid #e53e3e;
  border-radius: 6px;
  padding: 1rem;
  margin-top: 1rem;
  text-align: center;
}
.empty {
  text-align: center;
  color: #64748b;
  margin: 2rem 0;
}

@media (max-width: 900px) {
  .title {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }
  .details-card {
    grid-template-columns: 1fr 1fr;
    gap: 0.5rem 1rem;
    padding: 1rem;
    margin-bottom: 1.5rem;
  }
  .subtitle {
    font-size: 1.1rem;
    margin: 1.5rem 0 0.8rem 0;
  }
  .despesas-table th, .despesas-table td {
    padding: 0.5rem 0.5rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 600px) {
  .title {
    font-size: 1.2rem;
    margin-bottom: 0.8rem;
  }
  .details-card {
    grid-template-columns: 1fr;
    gap: 0.5rem;
    padding: 0.8rem;
  }
  .subtitle {
    font-size: 1rem;
    margin: 1rem 0 0.5rem 0;
  }
  .despesas-table {
    margin-bottom: 1rem;
  }
  .despesas-table th, .despesas-table td {
    padding: 0.4rem 0.3rem;
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .title {
    font-size: 1rem;
    margin-bottom: 0.6rem;
  }
  .details-card {
    padding: 0.6rem;
  }
  .details-card > div {
    font-size: 0.85rem;
    line-height: 1.3;
  }
  .subtitle {
    font-size: 0.9rem;
    margin: 0.8rem 0 0.4rem 0;
  }
  .despesas-table {
    font-size: 0.75rem;
  }
  .despesas-table th, .despesas-table td {
    padding: 0.3rem 0.2rem;
    font-size: 0.75rem;
  }
}

@media (max-width: 360px) {
  .title {
    font-size: 0.9rem;
  }
  .details-card {
    padding: 0.4rem;
  }
  .details-card > div {
    font-size: 0.8rem;
  }
  .subtitle {
    font-size: 0.85rem;
  }
  .despesas-table th, .despesas-table td {
    padding: 0.2rem 0.1rem;
    font-size: 0.7rem;
  }
}
</style>
