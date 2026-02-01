-- Query 1
-- Top 5 operadoras com maior crescimento percentual
WITH periodos AS (
    SELECT
        cnpj,
        MIN(CONCAT(ano, trimestre)) AS inicio,
        MAX(CONCAT(ano, trimestre)) AS fim
    FROM despesas_consolidadas
    GROUP BY cnpj
),
     valores AS (
         SELECT
             p.cnpj,
             SUM(CASE WHEN CONCAT(d.ano, d.trimestre) = p.inicio THEN d.valor_despesas END) AS valor_inicio,
             SUM(CASE WHEN CONCAT(d.ano, d.trimestre) = p.fim THEN d.valor_despesas END) AS valor_fim
         FROM despesas_consolidadas d
                  JOIN periodos p ON d.cnpj = p.cnpj
         GROUP BY p.cnpj
     )
SELECT
    cnpj,
    ((valor_fim - valor_inicio) / valor_inicio) * 100 AS crescimento_percentual
FROM valores
WHERE valor_inicio > 0
ORDER BY crescimento_percentual DESC
    LIMIT 5;

-- Query 2
-- Distribuição de despesas por UF (Top 5)
SELECT
    uf,
    SUM(total_despesas) AS total_uf,
    AVG(total_despesas) AS media_por_operadora
FROM despesas_agregadas
GROUP BY uf
ORDER BY total_uf DESC
    LIMIT 5;

-- Query 3
-- Operadoras acima da média geral em ≥ 2 trimestres
WITH media_geral AS (
    SELECT AVG(valor_despesas) AS media
    FROM despesas_consolidadas
),
por_trimestre AS (
    SELECT
        cnpj,
        ano,
        trimestre,
        SUM(valor_despesas) AS total_trimestre
    FROM despesas_consolidadas
    GROUP BY cnpj, ano, trimestre
)
SELECT
    cnpj
FROM por_trimestre, media_geral
WHERE total_trimestre > media_geral.media
GROUP BY cnpj
HAVING COUNT(*) >= 2;
