package com.financas.controledespesas.modules.pagador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FormaPagamentoRequestDTO(
        @NotBlank(message = "O nome da forma de pagamento é obrigatório.") @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres") String nome) {
}
