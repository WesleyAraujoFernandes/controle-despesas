-- =============================================================================
-- V1__create_initial_schema.sql
-- Dominio: Controle de Despesas Domesticas
-- =============================================================================

-- 1. Tabela de Categorias (ex: Alimentacao, Moradia, Transporte, Lazer)
CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    descricao VARCHAR(255),
    cor_hex VARCHAR(7), -- Para exibicao visual no frontend (ex: #FF5733)
    icone VARCHAR(50),  -- Nome do icone para o Angular (ex: 'shopping_cart')
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
);

-- 2. Tabela de Formas de Pagamento (ex: PIX, Cartao de Credito, Debito, Dinheiro)
CREATE TABLE tb_forma_pagamento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_forma_pagamento_nome UNIQUE (nome)
);

-- 3. Tabela Principal de Despesas
CREATE TABLE tb_despesa (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(150) NOT NULL,
    valor NUMERIC(12, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, PAGO, CANCELADO
    
    -- Relacionamentos
    categoria_id BIGINT NOT NULL,
    forma_pagamento_id BIGINT,
    
    -- Auditoria Basica
    observacao TEXT,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP WITH TIME ZONE,

    -- Chaves Estrangeiras
    CONSTRAINT fk_despesa_categoria 
        FOREIGN KEY (categoria_id) REFERENCES tb_categoria (id),
    CONSTRAINT fk_despesa_forma_pagamento 
        FOREIGN KEY (forma_pagamento_id) REFERENCES tb_forma_pagamento (id)
);

-- Indices para otimizacao de buscas/filtros frequentes
CREATE INDEX idx_despesa_data_vencimento ON tb_despesa (data_vencimento);
CREATE INDEX idx_despesa_status ON tb_despesa (status);
CREATE INDEX idx_despesa_categoria ON tb_despesa (categoria_id);

-- =============================================================================
-- Cargas Iniciais Basicas (Carga Semente / Seed Data)
-- =============================================================================

INSERT INTO tb_categoria (nome, descricao, cor_hex, icone) VALUES
('Moradia', 'Aluguel, condominio, luz, agua, internet', '#4E73DF', 'home'),
('Alimentação', 'Supermercado, feira, restaurantes, delivery', '#1CC88A', 'restaurant'),
('Transporte', 'Combustivel, transporte publico, aplicativo', '#36B9CC', 'directions_car'),
('Saúde', 'Farmacia, consultas, plano de saude', '#E74A3B', 'favorite'),
('Lazer', 'Viagens, cinema, passeios, streaming', '#F6C23E', 'movie');

INSERT INTO tb_forma_pagamento (nome) VALUES
('PIX'),
('Cartão de Crédito'),
('Cartão de Débito'),
('Dinheiro'),
('Boleto Bancário');