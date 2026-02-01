# Etapa 3 – Banco de Dados e Análises SQL (MySQL 8)

##  Objetivo da Etapa

Esta etapa tem como objetivo estruturar um banco de dados relacional utilizando **MySQL 8**, importar os arquivos CSV gerados nas etapas anteriores e desenvolver queries para extração de insights sobre despesas das operadoras de saúde.

Todo o processo foi pensado para ser simples, reproduzível e alinhado com cenários reais de análise de dados.

---

## 📁 Estrutura de Arquivos Utilizada

```text
3_banco_dados/
├── sql/
│   ├── 01_create_tables.sql
│   ├── 02_load_data.sql
│   └── 03_queries_analiticas.sql
│
└── data/
    ├── consolidado_despesas.csv
    ├── despesas_agregadas.csv
    └── Relatorio_cadop.csv
```

---

## 🗄️ Banco de Dados

* **SGBD:** MySQL 8
* As queries podem ser executadas:

    * Manualmente no MySQL
    * Ou diretamente pela IDE (no meu caso, utilizando a conexão MySQL integrada ao **IntelliJ IDEA**)

---

## 📂 Arquivos CSV Utilizados

Conforme solicitado nas tarefas de preparação:

1. **consolidado_despesas.csv**

    * Dados consolidados de despesas por operadora, trimestre e ano

2. **despesas_agregadas.csv**

    * Dados agregados por operadora, incluindo total, média trimestral e desvio padrão

3. **Relatorio_cadop.csv**

    * Dados cadastrais das operadoras (CNPJ, razão social, UF, modalidade, data de registro etc.)

---

##  Modelagem e Criação das Tabelas (DDL)

Foram criadas **três tabelas principais**, cada uma refletindo diretamente um dos arquivos CSV:

* `consolidado_despesas`
* `despesas_agregadas`
* `operadoras_cadop`

### Chaves e Índices

* Chaves primárias definidas de forma técnica (IDs ou combinações lógicas quando aplicável)
* Índices criados para:

    * CNPJ
    * UF
    * Ano e Trimestre
      Esses campos são frequentemente utilizados em filtros e agregações nas queries analíticas.

---

## Trade-off Técnico – Normalização

### Opção Escolhida: **Tabelas normalizadas separadas**

**Justificativa:**

* **Volume de dados esperado:**
  Os dados podem crescer consideravelmente ao longo do tempo, especialmente os consolidados por trimestre.

* **Frequência de atualizações:**
  Dados cadastrais mudam pouco, enquanto dados financeiros são atualizados com mais frequência. Separar evita reprocessamentos desnecessários.

* **Complexidade das queries analíticas:**
  Apesar de exigir `JOINs`, a separação melhora:

    * Organização
    * Manutenibilidade
    * Clareza sem impacto relevante de performance no contexto do desafio

A opção por uma tabela totalmente desnormalizada foi descartada por dificultar manutenção e reaproveitamento dos dados.

---

##  Trade-off Técnico – Tipos de Dados

### Valores Monetários

**Escolha:** `DECIMAL(15,2)`

**Justificativa:**

* Evita problemas de precisão do `FLOAT`
* Mantém valores financeiros confiáveis
* Melhor opção para análises e relatórios futuros

---

### Datas

**Escolha:** `DATE`

**Justificativa:**

* Os dados não exigem precisão de horário
* Melhor semântica para registros oficiais
* Facilita comparações e filtros temporais

---

## Importação dos Dados (CSV)

As cargas foram realizadas via `LOAD DATA INFILE`, considerando:

* **Encoding:** UTF-8
* **Separador:** `;`

### Tratamento de Inconsistências

Durante a importação, foram observados:

#### 1. Valores NULL em campos obrigatórios

* Abordagem:

    * Permitido `NULL` quando o campo não era essencial para as análises
    * Mantém integridade sem descartar registros úteis

#### 2. Strings em campos numéricos

* Abordagem:

    * Conversão quando possível
    * Caso contrário, o valor foi tratado como `NULL`

#### 3. Datas em formatos inconsistentes

* Abordagem:

    * Conversão explícita quando o formato permitia
    * Registros inválidos mantidos com `NULL` para não comprometer a carga completa

A opção por **não rejeitar linhas inteiras** foi feita para preservar o máximo de dados possível.

---

## Queries Analíticas Desenvolvidas

Todas as queries estão no arquivo:

```
sql/03_queries_analiticas.sql
```

---

### 🔎 Query 1

**Quais as 5 operadoras com maior crescimento percentual de despesas entre o primeiro e o último trimestre analisado?**

**Desafio tratado:**

* Operadoras sem dados em todos os trimestres

**Abordagem adotada:**

* Comparação apenas entre o primeiro e o último trimestre disponível por operadora
* Operadoras sem pelo menos dois trimestres distintos foram desconsideradas

**Justificativa:**

* Evita distorções
* Garante crescimento real baseado em dados existentes

---

### 🔎 Query 2

**Distribuição de despesas por UF (Top 5 estados)**

Inclui:

* Total de despesas por UF
* Média de despesas por operadora em cada UF

**Abordagem:**

* `JOIN` entre despesas e dados cadastrais
* `GROUP BY UF`
* Uso de agregações (`SUM`, `AVG`)

---

### 🔎 Query 3

**Quantas operadoras tiveram despesas acima da média geral em pelo menos 2 dos 3 trimestres analisados?**

### Trade-off Técnico

**Abordagem escolhida:**

* Subquery para cálculo da média geral
* Agrupamento por operadora e contagem de trimestres acima da média

**Justificativa:**

* Boa performance para o volume esperado
* Código legível
* Fácil manutenção e adaptação futura

Outras abordagens (como múltiplas subqueries independentes) foram evitadas por reduzir legibilidade.


