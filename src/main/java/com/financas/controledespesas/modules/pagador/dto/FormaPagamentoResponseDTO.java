package com.financas.controledespesas.modules.pagador.dto;

import java.time.OffsetDateTime;

import com.financas.controledespesas.modules.pagador.model.FormaPagamento;

public record FormaPagamentoResponseDTO(
        Long id,
        String nome,
        Boolean ativo,
        OffsetDateTime dataCriacao) {
    public static FormaPagamentoResponseDTO fromEntity(FormaPagamento formaPagamento) {
        return new FormaPagamentoResponseDTO(formaPagamento.getId(), formaPagamento.getNome(),
                formaPagamento.getAtivo(), formaPagamento.getDataCriacao());
    }
}
