
# Etapa 2 – Transformação e Validação de Dados

Este módulo corresponde à **Etapa 2 do teste técnico**, responsável pela validação,
enriquecimento e agregação dos dados de despesas das operadoras de planos de saúde.

A implementação foi feita em **Java**, priorizando simplicidade, legibilidade,
tratamento de erros e decisões técnicas explícitas.

---

## Estrutura de Diretórios

```

2_transformacao_validacao/
├── src/
│   └── MainTransformacaoValidacao.java
├── data/
│   ├── raw/
│   │   ├── consolidado_despesas.csv
│   │   └── Relatorio_cadop.csv
│   └── output/
│       └── despesas_agregadas.csv

```

---

## Arquivos de Entrada (raw)

Esta etapa utiliza **dois arquivos CSV**, ambos presentes no diretório `data/raw`:

### 1. consolidado_despesas.csv
Arquivo gerado na **Etapa 1** e **copiado manualmente** para esta etapa.

Contém os dados consolidados de despesas com a seguinte estrutura:

```

CNPJ/RegistroANS;RazaoSocial;Trimestre;Ano;ValorDespesas

````

> Observação:  
> Apesar de o arquivo ser produzido na Etapa 1, nesta etapa ele é tratado como
> **entrada estática**, garantindo independência entre as etapas.

---

### 2. Relatorio_cadop.csv
Arquivo de dados cadastrais das operadoras ativas, obtido no portal de dados abertos
da ANS.

Contém informações como:
- Registro ANS
- Razão Social
- Modalidade
- UF
- Dados cadastrais adicionais

---

## Execução

A execução do processamento é feita diretamente pela classe principal:

src.MainTransformacaoValidacao

Ao final da execução, será gerado o arquivo:


2_transformacao_validacao/data/output/despesas_agregadas.csv


---

## 2.1 Validação de Dados

Durante a leitura do arquivo `consolidado_despesas.csv`, são aplicadas as seguintes
validações:

### ✔ Valores numéricos positivos

* Apenas valores **maiores que zero** são considerados
* Valores inválidos, vazios ou não numéricos são descartados

### ✔ Razão Social não vazia

* A Razão Social é obtida exclusivamente a partir do cadastro oficial da ANS
* Registros sem correspondência no cadastro são ignorados

### ✔ CNPJ / Registro ANS inválido

* O join é realizado utilizando o **Registro ANS**
* Registros sem correspondência no cadastro oficial são descartados

#### Trade-off técnico – Tratamento de CNPJs / Registros inválidos

**Estratégias consideradas:**

* Corrigir automaticamente registros inconsistentes
* Manter registros inválidos marcados como suspeitos
* Descartar registros inválidos

**Decisão adotada:**
➡️ **Descartar registros sem correspondência no cadastro da ANS**

**Justificativa:**

* Garante maior confiabilidade nos dados finais
* Evita associações incorretas entre despesas e operadoras
* Simplifica a lógica e reduz ruído analítico

**Contras:**

* Possível perda de registros que exigiriam análise manual

---

## 2.2 Enriquecimento de Dados com Tratamento de Falhas

O enriquecimento é realizado por meio de um **join entre**:

* `consolidado_despesas.csv`
* `Relatorio_cadop.csv`

### Chave de junção

* **Registro ANS**

### Campos enriquecidos

* Razão Social
* UF

---

### Tratamento de falhas no join

#### Registros sem match no cadastro

* São descartados do resultado final

#### Registros duplicados no cadastro

* Apenas o **primeiro registro encontrado** é considerado

#### Trade-off técnico – Estratégia de processamento do join

**Estratégias consideradas:**

* Processamento incremental linha a linha
* Uso de banco de dados intermediário
* Processamento em memória

**Decisão adotada:**
➡️ **Processamento em memória utilizando `HashMap`**

**Justificativa:**

* Volume de dados compatível com memória
* Lookup O(1) para o join
* Código simples, direto e legível

---

## 2.3 Agregação e Análise Estatística

Após validação e enriquecimento, os dados são agregados por:

* **Razão Social**
* **UF**

### Métricas calculadas:

* **Total de despesas**
* **Média trimestral das despesas**
* **Desvio padrão das despesas**

O desvio padrão permite identificar operadoras com alta variabilidade nos valores
de despesas ao longo do tempo.

---

## Ordenação dos Resultados

Os registros finais são ordenados por:

* **Total de despesas (ordem decrescente)**

#### Trade-off técnico – Estratégia de ordenação

**Decisão adotada:**

* Ordenação em memória usando `Collections.sort`

**Justificativa:**

* Volume reduzido após agregação
* Baixo custo computacional
* Implementação simples e clara

---

## Arquivo de Saída

O processamento gera o arquivo:

```
2_transformacao_validacao/data/output/despesas_agregadas.csv
```

### Estrutura do CSV final:

```
RazaoSocial;UF;TotalDespesas;MediaTrimestral;DesvioPadrao
```

---


