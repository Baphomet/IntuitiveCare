from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from sqlalchemy import text
from app.database import SessionLocal

router = APIRouter(prefix="/api/estatisticas", tags=["Estatísticas"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.get("")
def estatisticas(db: Session = Depends(get_db)):
    total_query = text("""
        SELECT SUM(valor_despesas) AS total_despesas,
               AVG(valor_despesas) AS media_despesas
        FROM despesas_consolidadas
    """)
    top5_query = text("""
        SELECT 
            d.cnpj,
            o.razao_social,
            SUM(d.valor_despesas) AS total_despesas
        FROM despesas_consolidadas d
        JOIN operadoras o ON o.cnpj = d.cnpj
        GROUP BY d.cnpj, o.razao_social
        ORDER BY SUM(d.valor_despesas) DESC
        LIMIT 5
    """)
    top5_crescimento_query = text("""
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
    """)
    distribuicao_uf_query = text("""
        SELECT
            o.uf,
            SUM(d.valor_despesas) AS total_uf,
            AVG(d.valor_despesas) AS media_por_operadora
        FROM despesas_consolidadas d
        JOIN operadoras o ON o.registro_operadora = d.cnpj
        GROUP BY o.uf
        ORDER BY total_uf DESC
        LIMIT 5;
    """)

    total_result = db.execute(total_query).mappings().one_or_none() or {}
    top5_result = db.execute(top5_query).mappings().all()
    top5_crescimento_result = db.execute(top5_crescimento_query).mappings().all()
    distribuicao_uf_result = db.execute(distribuicao_uf_query).mappings().all()

    return {
        "total_despesas": float(total_result["total_despesas"]) if total_result["total_despesas"] is not None else None,
        "media_despesas": float(total_result["media_despesas"]) if total_result["media_despesas"] is not None else None,
        "top_5_operadoras": [
            {
                **row,
                "total_despesas": float(row["total_despesas"]) if row["total_despesas"] is not None else None
            } for row in top5_result
        ],
        "top_5_crescimento": [
            {
                "nome_fantasia": row["nome_fantasia"],
                "cnpj_real": row["cnpj_real"],
                "registro_operadora": row["registro_operadora"],
                "crescimento_percentual": float(row["crescimento_percentual"])
            } for row in top5_crescimento_result
        ],
        "distribuicao_uf": [
            {
                "uf": row["uf"],
                "total_uf": float(row["total_uf"]),
                "media_por_operadora": float(row["media_por_operadora"])
            } for row in distribuicao_uf_result
        ]
    }
