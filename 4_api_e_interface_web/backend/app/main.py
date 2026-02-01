from fastapi import FastAPI
from app.routes import operadoras, estatisticas

app = FastAPI(title="API Operadoras IntuitiveCare")

app.include_router(operadoras.router)
app.include_router(estatisticas.router)
