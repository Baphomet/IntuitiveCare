from pydantic import BaseModel
from typing import Optional
from datetime import date

class OperadoraBase(BaseModel):
    cnpj: str
    registro_operadora: Optional[str]
    razao_social: str
    nome_fantasia: Optional[str]
    modalidade: Optional[str]
    uf: Optional[str]
    data_registro_ans: Optional[date]

class OperadoraResponse(OperadoraBase):
    class Config:
        from_attributes = True

class DespesaConsolidadaResponse(BaseModel):
    id: int
    cnpj: str
    razao_social: Optional[str]
    ano: int
    trimestre: int
    valor_despesas: float

class DespesaAgregadaResponse(BaseModel):
    id: int
    razao_social: Optional[str]
    uf: Optional[str]
    total_despesas: Optional[float]
    media_trimestral: Optional[float]
    desvio_padrao: Optional[float]
