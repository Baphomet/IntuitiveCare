import { defineStore } from 'pinia'
import axios from 'axios'

export const useOperadoraDetailStore = defineStore('operadoraDetail', {
  state: () => ({
    operadora: null,
    despesas: [],
    loading: false,
    error: '',
    loadingDespesas: false,
    errorDespesas: ''
  }),
  actions: {
    async fetchOperadora(cnpj) {
      this.loading = true
      this.error = ''
      try {
        const { data } = await axios.get(`/api/operadoras/${cnpj}`)
        this.operadora = data
      } catch (e) {
        this.error = 'Erro ao buscar operadora.'
      } finally {
        this.loading = false
      }
    },
    async fetchDespesas(cnpj) {
      this.loadingDespesas = true
      this.errorDespesas = ''
      try {
        const { data } = await axios.get(`/api/operadoras/${cnpj}/despesas`)
        this.despesas = data.data
      } catch (e) {
        this.errorDespesas = 'Erro ao buscar despesas.'
      } finally {
        this.loadingDespesas = false
      }
    }
  }
})
