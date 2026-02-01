import { defineStore } from 'pinia'
import axios from 'axios'

export const useOperadorasStore = defineStore('operadoras', {
  state: () => ({
    operadoras: [],
    total: 0,
    loading: false,
    error: '',
    page: 1,
    limit: 10,
    search: ''
  }),
  actions: {
    async fetchOperadoras(page = 1, search = '') {
      this.loading = true
      this.error = ''
      try {
        const params = { page, limit: this.limit }
        if (search) params.search = search
        const { data } = await axios.get('/api/operadoras', { params })
        this.operadoras = data.data
        this.total = data.total
        this.page = page
        this.search = search
      } catch (e) {
        this.error = 'Erro ao buscar operadoras.'
      } finally {
        this.loading = false
      }
    }
  }
})
