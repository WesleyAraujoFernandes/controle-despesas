package com.financas.controledespesas.modules.categoria.dto;

import java.time.OffsetDateTime;

import com.financas.controledespesas.modules.categoria.model.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        String corHex,
        String icone,
        Boolean ativo,
        OffsetDateTime dataCriacao) {
    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getCorHex(),
                categoria.getIcone(),
                categoria.getAtivo(),
                categoria.getDataCriacao());
    }
}
