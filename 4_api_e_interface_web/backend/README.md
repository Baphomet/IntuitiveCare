# IntuitiveCare API Backend

## Descrição
API FastAPI para consulta de operadoras de saúde, despesas e estatísticas, conectada ao banco MySQL `intuitivecare`.


## Rotas
- `GET /api/operadoras`: Lista operadoras com paginação e busca.
- `GET /api/operadoras/{cnpj}`: Detalhes de uma operadora.
- `GET /api/operadoras/{cnpj}/despesas`: Histórico de despesas da operadora.
- `GET /api/estatisticas`: Estatísticas agregadas (total, média, top 5).

## Exemplos de Resposta

### `GET /api/operadoras`
```json
{
	"data": [
		{
			"cnpj": "00006037000127",
			"registro_operadora": "361941",
			"razao_social": "UNIMED DE TATUI - COOPERATIVA DE TRABALHO MÉDICO",
			"nome_fantasia": "UNIMED TATUÍ",
			"modalidade": "Cooperativa Médica",
			"uf": "SP",
			"data_registro_ans": "1998-12-18"
		},
		// ...mais operadoras...
	],
	"page": 1,
	"limit": 10,
	"total": 1110
}
```

### `GET /api/operadoras/{cnpj}`
```json
{
	"cnpj": "00006037000127",
	"registro_operadora": "361941",
	"razao_social": "UNIMED DE TATUI - COOPERATIVA DE TRABALHO MÉDICO",
	"nome_fantasia": "UNIMED TATUÍ",
	"modalidade": "Cooperativa Médica",
	"uf": "SP",
	"data_registro_ans": "1998-12-18"
}
```

### `GET /api/operadoras/{cnpj}/despesas`
```json
{
	"data": [
		{
			"ano": 2025,
			"trimestre": "1T",
			"valor": 123456.78
		},
		// ...mais trimestres...
	],
	"total": 987654.32
}
```

### `GET /api/estatisticas`
```json
{
	"total_despesas": 123456789.00,
	"media_despesas": 111234.56,
	"top_5_operadoras": [
		{
			"cnpj": "00006037000127",
			"nome_fantasia": "UNIMED TATUÍ",
			"total_despesas": 9876543.21
		}
		// ...mais operadoras...
	]
}
```

## Configuração
- Banco: intuitivecare
- Usuário: root
- Senha: admin
- Host: localhost

Crie um arquivo `.env` com as variáveis de conexão (já incluso).

## Trade-offs Técnicos
### 1. Framework Backend
**Escolha:** FastAPI
- Justificativa: Simplicidade, performance, tipagem, documentação automática, ideal para APIs REST modernas.

### 2. Paginação
**Escolha:** Offset-based
- Justificativa: Volume de dados moderado, fácil implementação, suficiente para consultas administrativas.

### 3. Cache vs Queries Diretas
**Escolha:** Calcular sempre na hora
- Justificativa: Dados mudam pouco, consistência é prioridade, queries são rápidas para o volume esperado.

### 4. Estrutura de Resposta
**Escolha:** Dados + metadados (data, total, page, limit)
- Justificativa: Facilita paginação e exibição no frontend, padrão em APIs modernas.

## Execução
```bash
pip install -r requirements.txt
uvicorn app.main:app --reload
```

## Observações
- Estrutura do banco conforme scripts SQL da pasta 3_banco_dados.
- Coleção Postman e exemplos de uso estão na pasta `/postman`.
