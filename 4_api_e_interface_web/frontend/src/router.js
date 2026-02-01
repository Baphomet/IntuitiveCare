import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Operadoras',
    component: () => import('./pages/OperadorasPage.vue')
  },
  {
    path: '/operadora/:cnpj',
    name: 'DetalheOperadora',
    component: () => import('./pages/OperadoraDetailPage.vue')
  },
  {
    path: '/estatisticas',
    name: 'Estatisticas',
    component: () => import('./pages/EstatisticasPage.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
