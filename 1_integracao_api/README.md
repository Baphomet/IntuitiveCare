# Etapa 1 – Integração com API Pública (ANS)

## Objetivo
Esta etapa tem como objetivo acessar e processar os dados de **Demonstrações Contábeis**
disponibilizados pela ANS, consolidando informações de **Despesas com Eventos/Sinistros**
dos últimos **3 trimestres disponíveis** em um único arquivo CSV normalizado.

O foco desta etapa é a integração, leitura, normalização e consolidação dos dados,
além da documentação das decisões técnicas tomadas durante o processo.

---

## Fonte dos Dados
Os dados utilizados foram obtidos a partir do portal oficial de Dados Abertos da ANS:

https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/

Foram considerados os três trimestres mais recentes disponíveis no momento do desenvolvimento:
- 1T2025
- 2T2025
- 3T2025

Os arquivos ZIP foram baixados, extraídos e os arquivos CSV resultantes foram armazenados em:

1_integracao_api/data/raw/

---

## Estrutura do Projeto
```
1_integracao_api/
├── data/
│ ├── raw/ # CSVs originais da ANS (1T2025, 2T2025, 3T2025)
│ └── output/ # Arquivo CSV consolidado gerado pelo programa
├── src/
│ └── main/java/ # Código Java da etapa 1
└── README.md
```
---

## Como Executar a Etapa 1

Esta etapa pode ser executada de forma independente.

### Passos:
1. Garantir que os arquivos CSV dos trimestres estejam em:
   1_integracao_api/data/raw/

2. Executar a classe principal (`Main`) do projeto Java
3. O arquivo consolidado será gerado automaticamente em:
   1_integracao_api/data/output/consolidado_despesas.csv

---

## Processamento dos Arquivos

### Identificação de Despesas com Eventos/Sinistros
Durante o processamento dos arquivos CSV, são consideradas apenas as linhas cujo
campo **DESCRICAO** contém as palavras:

- `EVENTO`
- `SINISTRO`

A verificação é feita de forma **case-insensitive**, garantindo maior cobertura
independente da padronização textual do arquivo.

---

### Normalização dos Dados
As seguintes normalizações foram aplicadas:

- Separador de colunas: `;`
- Remoção de aspas (`"`)
- Valores monetários:
- Remoção do separador de milhar (`.`)
- Substituição de vírgula por ponto para conversão numérica
- Datas:
- Ano extraído diretamente do campo `DATA`
- Trimestre calculado a partir do mês da data

---

## Consolidação dos Dados

Os dados dos três trimestres são consolidados em um único arquivo CSV:

1_integracao_api/data/output/consolidado_despesas.csv

### Estrutura do CSV Final
| Coluna        | Descrição |
|--------------|-----------|
| CNPJ         | Registro ANS da operadora |
| RazaoSocial  | Não disponível nesta etapa (preenchido como `N/A`) |
| Trimestre    | Trimestre de referência |
| Ano          | Ano de referência |
| ValorDespesas| Valor final da despesa |

---

## Tratamento de Inconsistências e Decisões Técnicas

Durante a consolidação dos dados, foram identificadas as seguintes situações:

### CNPJs duplicados com razões sociais diferentes
- Os arquivos de Demonstrações Contábeis não possuem a Razão Social da operadora.
- Dessa forma, o campo **RazaoSocial** foi preenchido como `N/A` nesta etapa.

**Decisão:** marcar como `N/A`  
**Justificativa:** evitar inferência ou suposição de dados inexistentes.  
A normalização e validação da Razão Social será realizada na Etapa 2,
com base nos dados cadastrais oficiais da ANS.

---

### Valores zerados ou negativos
- Foram identificados registros com valores zerados ou negativos.
- Esses valores podem representar ajustes ou lançamentos contábeis válidos.

**Decisão:** manter os registros  
**Justificativa:** a validação de valores positivos será tratada explicitamente
na Etapa 2, conforme solicitado no teste.

---

### Datas ou trimestres inconsistentes
- O trimestre foi calculado com base no mês da data.
- Registros com erro de parsing ou formato inválido foram ignorados.

**Decisão:** ignorar registros inválidos  
**Justificativa:** evitar contaminação do conjunto de dados consolidados.

---

## Trade-off Técnico – Estratégia de Processamento

### Alternativas consideradas:
- Carregar todos os arquivos em memória
- Processar os arquivos de forma incremental (linha a linha)

### Decisão adotada:
Processamento incremental (streaming)

### Justificativa:
- Arquivos potencialmente grandes
- Menor consumo de memória
- Melhor escalabilidade
- Simplicidade e clareza da implementação

---

## Resultado Final
Ao final da execução, o arquivo `consolidado_despesas.csv` contém as despesas
de eventos/sinistros consolidadas dos três trimestres processados.