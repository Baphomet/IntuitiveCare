<template>
  <section>
    <h2 class="title">Estatísticas Gerais</h2>
    <div v-if="store.loading" class="loading">Carregando...</div>
    <div v-else-if="store.error" class="error">{{ store.error }}</div>
    <div v-else>
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-label">Total de Despesas</div>
          <div class="stat-value">R$ {{ store.estatisticas?.total_despesas?.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Média de Despesas</div>
          <div class="stat-value">R$ {{ store.estatisticas?.media_despesas?.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</div>
        </div>
      </div>
      <h3 class="subtitle">Top 5 Operadoras por Crescimento Percentual</h3>
      <table v-if="store.estatisticas?.top_5_crescimento?.length" class="top-table">
        <thead>
          <tr>
            <th>Nome Fantasia</th>
            <th>CNPJ</th>
            <th>Crescimento (%)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="op in store.estatisticas.top_5_crescimento" :key="op.cnpj_real">
            <td>{{ op.nome_fantasia || 'Nome não disponível' }}</td>
            <td>{{ op.cnpj_real }}</td>
            <td>{{ op.crescimento_percentual.toFixed(2) }}%</td>
          </tr>
        </tbody>
      </table>
      <div v-if="!store.estatisticas?.top_5_crescimento?.length" class="empty">Nenhuma operadora encontrada no top 5.</div>
      <h3 class="subtitle">Distribuição de Despesas por UF</h3>
      <table v-if="store.estatisticas?.distribuicao_uf?.length" class="top-table">
        <thead>
          <tr>
            <th>UF</th>
            <th>Total de Despesas</th>
            <th>Média por Operadora</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="uf in store.estatisticas.distribuicao_uf" :key="uf.uf">
            <td>{{ uf.uf }}</td>
            <td>R$ {{ uf.total_uf.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</td>
            <td>R$ {{ uf.media_por_operadora.toLocaleString('pt-BR', { minimumFractionDigits: 2 }) }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="!store.estatisticas?.distribuicao_uf?.length" class="empty">Nenhuma distribuição encontrada.</div>
    </div>
  </section>
</template>

<script setup>
import { onMounted } from 'vue'
import { useEstatisticasStore } from '../stores/estatisticas'

const store = useEstatisticasStore()

onMounted(() => {
  store.fetchEstatisticas()
})
</script>

<style scoped>
.title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #22223b;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.stats-cards {
  display: flex;
  gap: 2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  justify-content: center;
}
.stat-card {
  background: #f1f1fb;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(83,91,242,0.07);
  padding: 1.5rem 2rem;
  flex: 1 1 250px;
  min-width: 220px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 1rem;
}
.stat-label {
  color: #3a3a6a;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  font-weight: 500;
}
.stat-value {
  font-size: 1.7rem;
  font-weight: bold;
  color: #535bf2;
}
.subtitle {
  font-size: 1.3rem;
  margin: 2rem 0 1rem 0;
  color: #22223b;
  font-weight: 600;
}
.top-table {
  width: 100%;
  border-collapse: collapse;
  background: #f1f1fb;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(83,91,242,0.07);
  margin-bottom: 2rem;
}
.top-table th, .top-table td {
  padding: 0.75rem 1rem;
  text-align: left;
  color: #22223b;
}
.top-table th {
  background: #d1d8ff;
  color: #3a3a6a;
  font-weight: 700;
}
.top-table tr:nth-child(even) {
  background: #e9eafc;
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

@media (max-width: 900px) {
  .stats-cards {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  .main-content {
    padding: 1rem 0.2rem;
  }
  .top-table th, .top-table td {
    font-size: 0.95rem;
    padding: 0.5rem 0.5rem;
  }
}

@media (max-width: 600px) {
  .title {
    font-size: 1.3rem;
    margin-bottom: 1rem;
  }
  .stats-cards {
    gap: 0.8rem;
    margin-bottom: 1rem;
  }
  .stat-card {
    padding: 1rem 0.5rem;
    min-width: 140px;
    border-radius: 8px;
  }
  .stat-label {
    font-size: 0.95rem;
  }
  .stat-value {
    font-size: 1.4rem;
  }
  .subtitle {
    font-size: 1.1rem;
    margin: 1.5rem 0 0.8rem 0;
  }
  .top-table {
    margin-bottom: 1rem;
  }
  .top-table th, .top-table td {
    font-size: 0.85rem;
    padding: 0.4rem 0.2rem;
  }
}

@media (max-width: 480px) {
  .title {
    font-size: 1.1rem;
    margin-bottom: 0.8rem;
  }
  .stats-cards {
    gap: 0.5rem;
  }
  .stat-card {
    padding: 0.8rem 0.3rem;
    min-width: 120px;
  }
  .stat-label {
    font-size: 0.85rem;
    margin-bottom: 0.3rem;
  }
  .stat-value {
    font-size: 1.2rem;
  }
  .subtitle {
    font-size: 1rem;
    margin: 1rem 0 0.5rem 0;
  }
  .top-table th, .top-table td {
    font-size: 0.75rem;
    padding: 0.3rem 0.15rem;
  }
}

@media (max-width: 360px) {
  .title {
    font-size: 0.95rem;
  }
  .stat-card {
    padding: 0.6rem 0.2rem;
    min-width: 100px;
  }
  .stat-label {
    font-size: 0.8rem;
  }
  .stat-value {
    font-size: 1rem;
  }
  .subtitle {
    font-size: 0.9rem;
  }
  .top-table th, .top-table td {
    font-size: 0.7rem;
    padding: 0.2rem 0.1rem;
  }
}
</style>
