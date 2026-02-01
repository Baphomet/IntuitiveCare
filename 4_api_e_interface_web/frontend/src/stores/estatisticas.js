import { defineStore } from 'pinia'
import axios from 'axios'

export const useEstatisticasStore = defineStore('estatisticas', {
  state: () => ({
    estatisticas: null,
    loading: false,
    error: ''
  }),
  actions: {
    async fetchEstatisticas() {
      this.loading = true
      this.error = ''
      try {
        const { data } = await axios.get('/api/estatisticas')
        this.estatisticas = data
      } catch (e) {
        this.error = 'Erro ao buscar estatísticas.'
      } finally {
        this.loading = false
      }
    }
  }
})
