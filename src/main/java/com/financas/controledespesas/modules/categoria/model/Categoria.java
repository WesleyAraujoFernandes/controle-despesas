package com.financas.controledespesas.modules.categoria.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    private String nome;
    @Column(length = 255)
    private String descricao;
    @Column(name = "cor_hex", length = 7)
    private String corHex;
    @Column(length = 50)
    private String icone;
    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;
    @PrePersist
    public void PrePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = OffsetDateTime.now();
        }
        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}
