CREATE DATABASE IF NOT EXISTS petshop;
USE petshop;

CREATE TABLE IF NOT EXISTS agenda (
    horario       INT PRIMARY KEY,        -- 0 a 9 (a chave primária impede dois agendamentos no mesmo horário)
    tipo          VARCHAR(10)  NOT NULL,  -- 'Cachorro' ou 'Gato'
    nome          VARCHAR(50)  NOT NULL,
    idade         INT          NOT NULL,
    nome_dono     VARCHAR(50)  NOT NULL,
    telefone_dono VARCHAR(20)  NOT NULL,
    detalhe       VARCHAR(30)  NOT NULL,  -- porte (Cachorro) ou temperamento (Gato)
    sintomas      VARCHAR(200) NOT NULL
);
