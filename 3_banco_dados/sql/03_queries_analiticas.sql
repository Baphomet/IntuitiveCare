-- Top 5 operadoras com maior crescimento percentual
WITH periodos AS (
    SELECT
        d.cnpj AS registro_operadora,
        MIN(CONCAT(d.ano, d.trimestre)) AS inicio,
        MAX(CONCAT(d.ano, d.trimestre)) AS fim
    FROM despesas_consolidadas d
    GROUP BY d.cnpj
),
valores AS (
    SELECT
        p.registro_operadora,
        SUM(CASE WHEN CONCAT(d.ano, d.trimestre) = p.inicio THEN d.valor_despesas END) AS valor_inicio,
        SUM(CASE WHEN CONCAT(d.ano, d.trimestre) = p.fim THEN d.valor_despesas END) AS valor_fim
    FROM despesas_consolidadas d
    JOIN periodos p ON d.cnpj = p.registro_operadora
    GROUP BY p.registro_operadora
)
SELECT
    o.nome_fantasia,
    o.cnpj AS cnpj_real,
    v.registro_operadora,
    ((valor_fim - valor_inicio) / valor_inicio) * 100 AS crescimento_percentual
FROM valores v
JOIN operadoras o ON o.registro_operadora = v.registro_operadora
WHERE valor_inicio > 0
ORDER BY crescimento_percentual DESC
LIMIT 5;

-- Distribuição de despesas por UF (Top 5)
SELECT
    o.uf,
    SUM(d.valor_despesas) AS total_uf,
    AVG(d.valor_despesas) AS media_por_operadora
FROM despesas_consolidadas d
JOIN operadoras o ON o.registro_operadora = d.cnpj
GROUP BY o.uf
ORDER BY total_uf DESC
LIMIT 5;

-- Operadoras acima da média geral em ≥ 2 trimestres
WITH media_geral AS (
    SELECT AVG(valor_despesas) AS media
    FROM despesas_consolidadas
),
por_trimestre AS (
    SELECT
        d.cnpj AS registro_operadora,
        d.ano,
        d.trimestre,
        SUM(d.valor_despesas) AS total_trimestre
    FROM despesas_consolidadas d
    GROUP BY d.cnpj, d.ano, d.trimestre
)
SELECT
    o.nome_fantasia,
    o.cnpj AS cnpj_real,
    p.registro_operadora
FROM por_trimestre p, media_geral, operadoras o
WHERE p.total_trimestre > media_geral.media
  AND o.registro_operadora = p.registro_operadora
GROUP BY p.registro_operadora
HAVING COUNT(*) >= 2;
