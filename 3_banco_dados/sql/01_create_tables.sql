CREATE DATABASE IF NOT EXISTS intuitivecare
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE intuitivecare;

CREATE TABLE operadoras (
    registro_operadora VARCHAR(10),
    cnpj VARCHAR(14) PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100),
    uf CHAR(2),
    data_registro_ans DATE,

    INDEX idx_operadoras_uf (uf),
    INDEX idx_operadoras_razao (razao_social)
);

CREATE TABLE despesas_consolidadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL,
    razao_social VARCHAR(255),
    ano INT NOT NULL,
    trimestre INT NOT NULL CHECK (trimestre BETWEEN 1 AND 4),
    valor_despesas DECIMAL(15,2) NOT NULL,

    INDEX idx_despesas_cnpj (cnpj),
    INDEX idx_despesas_periodo (ano, trimestre)
);

CREATE TABLE despesas_agregadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    razao_social VARCHAR(255),
    uf CHAR(2),
    total_despesas DECIMAL(18,2),
    media_trimestral DECIMAL(18,2),
    desvio_padrao DECIMAL(18,2),

    INDEX idx_total_despesas (total_despesas)
);
