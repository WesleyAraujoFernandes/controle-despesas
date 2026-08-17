package com.financas.controledespesas.modules.categoria.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financas.controledespesas.modules.despesa.model.Despesa;
import com.financas.controledespesas.modules.despesa.model.StatusDespesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    @Query(
        "SELECT d FROM Despesa d "+
        "JOIN FETCH d.categoria " +
        "LEFT JOIN FETCH d.formaPagamento " +
        "WHERE d.dataVencimento BETWEEN :dataInicio AND :dataFim " +
        "ORDER BY d.dataVencimento ASC"
    )
    List<Despesa> findByPeriodoWithFetch(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    List<Despesa> findByStatusOrderByDataVencimentoAsc(StatusDespesa status);
    @Query(
        "SELECT COALESCE(SUM(d.valor),0) FROM Despesa d " +
        "WHERE d.status = :status " +
        "AND d.dataVencimento BETWEEN :dataInicio AND :dataFim"
    )
    BigDecimal calcularTotalPorStatusEPeriodo(
        @Param("status") StatusDespesa status,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
    boolean existsByCategoriaId(Long categoriaId);
    boolean existsByFormaPagamentoId(Long formaPagamentoId);
}
