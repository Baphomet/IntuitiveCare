# IntuitiveCare Frontend

## Descrição
Interface web moderna desenvolvida em Vue.js + Vite para consulta de operadoras de saúde, despesas e estatísticas, consumindo a API FastAPI do backend.

## Funcionalidades
- Tabela paginada de operadoras com busca/filtro por razão social ou CNPJ
- Página de detalhes da operadora com histórico de despesas
- Gráfico de distribuição de despesas por UF (Chart.js)
- Estatísticas agregadas e top 5 operadoras
- Feedback visual para loading, erros e dados vazios

## Execução
```bash
cd 4_api_e_interface_web/frontend
npm install
npm run dev
```
Acesse: http://localhost:5173

> Certifique-se de que o backend está rodando em http://localhost:8000 ou ajuste o proxy se necessário.

## Trade-offs Técnicos
### 1. Estratégia de Busca/Filtro
**Escolha:** Busca no servidor
- Justificativa: Volume de dados potencialmente grande, melhor performance e UX, evita transferir dados desnecessários para o cliente.

### 2. Gerenciamento de Estado
**Escolha:** Pinia
- Justificativa: Simplicidade, reatividade, integração nativa com Vue 3, fácil manutenção e escalabilidade.

### 3. Performance da Tabela
**Escolha:** Paginação no backend
- Justificativa: Evita renderização de grandes volumes no frontend, mantém a aplicação leve e responsiva.

### 4. Tratamento de Erros e Loading
- Mensagens específicas para cada tipo de erro (ex: falha de rede, dados não encontrados)
- Indicadores visuais de carregamento em todas as requisições
- Mensagem clara para dados vazios
- Justificativa: Melhora a experiência do usuário, facilita diagnóstico e evita frustração.

### 4.3.4. Tratamento de Erros e Loading

#### Empresas sem Nome
- **Cenário:** Algumas empresas podem não ter um nome fantasia registrado no banco de dados, resultando em uma célula vazia na tabela de "Top 5 Operadoras".
- **Abordagem:**
  - Exibir "Nome não disponível" na célula correspondente para evitar espaços vazios e melhorar a clareza para o usuário.
  - Justificativa: Isso garante uma experiência de usuário consistente e evita confusão ao interpretar os dados.

#### Outros Tratamentos
- **Erros de Rede/API:**
  - Mensagens específicas para falhas de conexão ou respostas inválidas.
  - Indicador visual de erro exibido no componente correspondente.
- **Estados de Loading:**
  - Indicadores visuais (spinners) exibidos durante requisições.
- **Dados Vazios:**
  - Mensagem clara informando que não há dados disponíveis para exibição.

## Estrutura de Pastas
- `src/pages`: Páginas principais (Operadoras, Detalhes, Estatísticas)
- `src/components`: Componentes reutilizáveis (Navbar, etc)
- `src/stores`: Stores Pinia para estado global

## Observações
- O frontend espera que o backend esteja disponível em `/api`. Se necessário, configure um proxy em `vite.config.js`.
- O projeto utiliza Chart.js via vue-chartjs para gráficos responsivos.

## Exemplo de Proxy (vite.config.js)
```js
export default {
	server: {
		proxy: {
			'/api': 'http://localhost:8000'
		}
	}
}
```

## Como customizar
- Para alterar estilos, edite `src/style.css` ou os estilos scoped dos componentes.
- Para adicionar novas rotas/páginas, edite `src/router.js` e crie novos arquivos em `src/pages`.

---

Este projeto segue o princípio KISS (Keep It Simple), priorizando clareza, responsividade e experiência do usuário.
