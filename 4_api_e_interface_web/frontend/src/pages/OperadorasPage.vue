<template>
  <section>
    <h2 class="title">Operadoras de Saúde</h2>
    <div class="search-bar">
      <input v-model="search" placeholder="Buscar por razão social ou CNPJ..." @keyup.enter="fetchOperadoras" />
      <button @click="fetchOperadoras">Buscar</button>
    </div>
    <div v-if="store.loading" class="loading">Carregando...</div>
    <div v-else>
      <template v-if="store.error">
        <div class="error">{{ store.error }}</div>
      </template>
      <template v-else>
        <table v-if="store.operadoras.length" class="operadoras-table">
          <thead>
            <tr>
              <th>CNPJ</th>
              <th>Registro</th>
              <th>Razão Social</th>
              <th>Nome Fantasia</th>
              <th>Modalidade</th>
              <th>UF</th>
              <th>Detalhes</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="op in store.operadoras" :key="op.cnpj">
              <td>{{ op.cnpj }}</td>
              <td>{{ op.registro_operadora }}</td>
              <td>{{ op.razao_social }}</td>
              <td>{{ op.nome_fantasia }}</td>
              <td>{{ op.modalidade }}</td>
              <td>{{ op.uf }}</td>
              <td><router-link :to="`/operadora/${op.cnpj}`">Ver</router-link></td>
            </tr>
          </tbody>
        </table>
        <div v-if="!store.operadoras.length" class="empty">Nenhuma operadora encontrada.</div>
        <div class="pagination">
          <button :disabled="store.page === 1" @click="changePage(store.page-1)">Anterior</button>
          <span>Página {{ store.page }} de {{ totalPages }}</span>
          <button :disabled="store.page === totalPages" @click="changePage(store.page+1)">Próxima</button>
        </div>
      </template>
    </div>
  </section>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useOperadorasStore } from '../stores/operadoras'

const store = useOperadorasStore()
const search = ref(store.search)

const totalPages = computed(() => Math.ceil(store.total / store.limit))

function fetchOperadoras() {
  store.fetchOperadoras(store.page, search.value)
}

function changePage(p) {
  store.fetchOperadoras(p, search.value)
}

onMounted(() => {
  store.fetchOperadoras(store.page, store.search)
})
</script>

<style scoped>
.title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #2c3e50;
}
.search-bar {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}
.search-bar input {
  flex: 1;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: 1px solid #cbd5e1;
  font-size: 1rem;
}
.search-bar button {
  background: #646cff;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1.5rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.search-bar button:hover {
  background: #535bf2;
}
.operadoras-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1.5rem;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.operadoras-table th, .operadoras-table td {
  padding: 0.75rem 1rem;
  text-align: left;
}
.operadoras-table th {
  background: #e0e7ff;
  color: #2c3e50;
  font-weight: 600;
}
.operadoras-table tr:nth-child(even) {
  background: #f8fafc;
}
.pagination {
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: flex-end;
  margin-bottom: 1rem;
}
.pagination button {
  background: #646cff;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.4rem 1.2rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}
.pagination button:disabled {
  background: #cbd5e1;
  cursor: not-allowed;
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
  .search-bar {
    flex-direction: column;
    gap: 0.8rem;
    margin-bottom: 1rem;
  }
  .search-bar input,
  .search-bar button {
    padding: 0.6rem 0.8rem;
    font-size: 0.95rem;
  }
  .operadoras-table th, .operadoras-table td {
    padding: 0.5rem 0.5rem;
    font-size: 0.9rem;
  }
  .pagination {
    justify-content: center;
    gap: 0.8rem;
  }
}

@media (max-width: 600px) {
  .title {
    font-size: 1.2rem;
    margin-bottom: 0.8rem;
  }
  .search-bar {
    gap: 0.5rem;
  }
  .search-bar input {
    padding: 0.5rem 0.6rem;
    font-size: 0.9rem;
  }
  .search-bar button {
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
  }
  .operadoras-table {
    font-size: 0.85rem;
  }
  .operadoras-table th, .operadoras-table td {
    padding: 0.4rem 0.3rem;
    font-size: 0.8rem;
  }
  .pagination {
    flex-wrap: wrap;
    margin-bottom: 0.8rem;
  }
  .pagination span {
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .title {
    font-size: 1rem;
    margin-bottom: 0.6rem;
  }
  .search-bar {
    gap: 0.3rem;
  }
  .search-bar input {
    padding: 0.4rem 0.4rem;
    font-size: 0.8rem;
  }
  .search-bar button {
    padding: 0.4rem 0.8rem;
    font-size: 0.8rem;
  }
  .operadoras-table {
    display: block;
    overflow-x: auto;
  }
  .operadoras-table th, .operadoras-table td {
    padding: 0.3rem 0.2rem;
    font-size: 0.7rem;
  }
  .pagination {
    gap: 0.5rem;
  }
  .pagination button {
    padding: 0.3rem 0.6rem;
    font-size: 0.75rem;
  }
}

@media (max-width: 360px) {
  .title {
    font-size: 0.9rem;
  }
  .search-bar input {
    font-size: 0.75rem;
  }
  .search-bar button {
    font-size: 0.75rem;
    padding: 0.3rem 0.6rem;
  }
  .operadoras-table th, .operadoras-table td {
    padding: 0.2rem 0.1rem;
    font-size: 0.65rem;
  }
}
</style>
