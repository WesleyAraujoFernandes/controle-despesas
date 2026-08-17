package com.financas.controledespesas.modules.pagador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.controledespesas.modules.pagador.model.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
    List<FormaPagamento> findByAtivoTrueOrderByNomeAsc();
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
