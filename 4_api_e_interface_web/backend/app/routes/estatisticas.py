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
        ORDER BY total_despesas DESC
        LIMIT 5
    """)
    total_result = db.execute(total_query).mappings().one_or_none() or {}
    top5_result = db.execute(top5_query).mappings().all()
    return {
        "total_despesas": float(total_result["total_despesas"]) if total_result["total_despesas"] is not None else None,
        "media_despesas": float(total_result["media_despesas"]) if total_result["media_despesas"] is not None else None,
        "top_5_operadoras": [
            {
                **row,
                "total_despesas": float(row["total_despesas"]) if row["total_despesas"] is not None else None
            } for row in top5_result
        ]
    }
