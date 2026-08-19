package com.financas.controledespesas.modules.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(min = 3, max = 60, message = "O nome deve ter entre 3 e 60 caracteres.")
    String nome,
    @Size(max = 255, message = "A descrição não pode exceder 255 caracteres.")
    String descricao,
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "A cor deve ser um código Hexadecimal válido (ex: #FF5733).")
    String corHex,
    @Size(max = 50, message = "O nome do ícone não pode exceder 50 caracteres.")
    String icone
) {

}
