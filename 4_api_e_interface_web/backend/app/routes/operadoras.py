from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from app.database import SessionLocal
from app.models import Operadora, DespesaConsolidada
from app.schemas import OperadoraResponse, DespesaConsolidadaResponse

router = APIRouter(prefix="/api/operadoras", tags=["Operadoras"])

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.get("/")
def listar_operadoras(
    page: int = Query(1, ge=1),
    limit: int = Query(10, ge=1, le=100),
    search: str | None = None,
    db: Session = Depends(get_db)
):
    offset = (page - 1) * limit
    query = db.query(Operadora)
    if search:
        query = query.filter(
            (Operadora.razao_social.ilike(f"%{search}%")) |
            (Operadora.cnpj.ilike(f"%{search}%"))
        )
    total = query.count()
    operadoras = query.offset(offset).limit(limit).all()
    return {
        "data": [OperadoraResponse.model_validate(op, from_attributes=True).model_dump() for op in operadoras],
        "page": page,
        "limit": limit,
        "total": total
    }


@router.get("/{cnpj}")
def detalhe_operadora(
    cnpj: str,
    db: Session = Depends(get_db)
):
    operadora = db.query(Operadora).filter(Operadora.cnpj == cnpj).first()
    if not operadora:
        return {"erro": "Operadora não encontrada"}
    return OperadoraResponse.model_validate(operadora, from_attributes=True).model_dump()

@router.get("/{cnpj}/despesas")
def despesas_operadora(
    cnpj: str,
    page: int = Query(1, ge=1),
    limit: int = Query(20, le=100),
    db: Session = Depends(get_db)
):
    offset = (page - 1) * limit
    query = db.query(DespesaConsolidada).filter(DespesaConsolidada.cnpj == cnpj).order_by(DespesaConsolidada.ano, DespesaConsolidada.trimestre)
    total = query.count()
    despesas = query.offset(offset).limit(limit).all()
    return {
        "data": [
            {
                "id": d.id,
                "cnpj": d.cnpj,
                "razao_social": d.razao_social,
                "ano": d.ano,
                "trimestre": d.trimestre,
                "valor_despesas": float(d.valor_despesas) if d.valor_despesas is not None else None
            } for d in despesas
        ],
        "page": page,
        "limit": limit,
        "total": total
    }
