SET GLOBAL local_infile = 1;

LOAD DATA LOCAL INFILE '3_banco_dados/data/Relatorio_cadop.csv'
INTO TABLE operadoras
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(
    registro_operadora,
    cnpj,
    razao_social,
    nome_fantasia,
    modalidade,
    @logradouro,
    @numero,
    @complemento,
    @bairro,
    @cidade,
    uf,
    @cep,
    @ddd,
    @telefone,
    @fax,
    @email,
    @representante,
    @cargo,
    @regiao,
    data_registro_ans
);

LOAD DATA LOCAL INFILE '3_banco_dados/data/consolidado_despesas.csv'
INTO TABLE despesas_consolidadas
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(
    cnpj,
    razao_social,
    trimestre,
    ano,
    valor_despesas
);

LOAD DATA LOCAL INFILE '3_banco_dados/data/despesas_agregadas.csv'
INTO TABLE despesas_agregadas
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(
    razao_social,
    uf,
    total_despesas,
    media_trimestral,
    desvio_padrao
);
