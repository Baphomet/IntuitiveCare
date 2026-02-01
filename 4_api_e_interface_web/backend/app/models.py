from sqlalchemy import Column, String, Integer, DECIMAL, Date
from .database import Base

class Operadora(Base):
    __tablename__ = "operadoras"
    cnpj = Column(String(14), primary_key=True)
    registro_operadora = Column(String(10))
    razao_social = Column(String(255), nullable=False)
    nome_fantasia = Column(String(255))
    modalidade = Column(String(100))
    uf = Column(String(2))
    data_registro_ans = Column(Date)

class DespesaConsolidada(Base):
    __tablename__ = "despesas_consolidadas"
    id = Column(Integer, primary_key=True, autoincrement=True)
    cnpj = Column(String(14))
    razao_social = Column(String(255))
    ano = Column(Integer)
    trimestre = Column(Integer)
    valor_despesas = Column(DECIMAL(15, 2))

class DespesaAgregada(Base):
    __tablename__ = "despesas_agregadas"
    id = Column(Integer, primary_key=True, autoincrement=True)
    razao_social = Column(String(255))
    uf = Column(String(2))
    total_despesas = Column(DECIMAL(18, 2))
    media_trimestral = Column(DECIMAL(18, 2))
    desvio_padrao = Column(DECIMAL(18, 2))
