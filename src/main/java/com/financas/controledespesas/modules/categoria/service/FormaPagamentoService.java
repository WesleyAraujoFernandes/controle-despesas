package com.financas.controledespesas.modules.categoria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.controledespesas.exception.BusinessException;
import com.financas.controledespesas.exception.ResourceNotFoundException;
import com.financas.controledespesas.modules.despesa.repository.DespesaRepository;
import com.financas.controledespesas.modules.pagador.dto.FormaPagamentoRequestDTO;
import com.financas.controledespesas.modules.pagador.dto.FormaPagamentoResponseDTO;
import com.financas.controledespesas.modules.pagador.model.FormaPagamento;
import com.financas.controledespesas.modules.pagador.repository.FormaPagamentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final DespesaRepository despesaRepository;

    @Transactional(readOnly = true)
    public List<FormaPagamentoResponseDTO> listarTodas(Boolean apenasAtivas) {
        List<FormaPagamento> formas = Boolean.TRUE.equals(apenasAtivas)
        ? formaPagamentoRepository.findByAtivoTrueOrderByNomeAsc()
        : formaPagamentoRepository.findAll();

        return formas.stream()
            .map(FormaPagamentoResponseDTO::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public FormaPagamentoResponseDTO buscarPorId(Long id) {
        FormaPagamento forma = buscarEntidadePorId(id);
        return FormaPagamentoResponseDTO.fromEntity(forma);
    }

    @Transactional
    public FormaPagamentoResponseDTO criar(FormaPagamentoRequestDTO dto) {
        if (formaPagamentoRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe uma forma de pagamento cadastrada com o nome '" + dto.nome() + "'.");
        }
        FormaPagamento forma = FormaPagamento.builder()
            .nome(dto.nome())
            .ativo(true)
            .build();
        FormaPagamento salva = formaPagamentoRepository.save(forma);
        return FormaPagamentoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public FormaPagamentoResponseDTO atualizar(Long id, FormaPagamentoRequestDTO dto) {
        FormaPagamento forma = buscarEntidadePorId(id);
        if (formaPagamentoRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)) {
            throw new BusinessException("Já existe outra forma de pagamento com o nome '" + dto.nome() + "'.");
        }
        forma.setNome(dto.nome());
        FormaPagamento atualizada = formaPagamentoRepository.save(forma);
        return FormaPagamentoResponseDTO.fromEntity(atualizada);
    }

    @Transactional
    public FormaPagamentoResponseDTO alterarStatus(Long id, boolean ativo) {
        FormaPagamento forma = buscarEntidadePorId(id);
        forma.setAtivo(ativo);
        FormaPagamento salva = formaPagamentoRepository.save(forma);
        return FormaPagamentoResponseDTO.fromEntity(salva);
    }

    @Transactional
    public void excluir(Long id) {
        FormaPagamento forma = buscarEntidadePorId(id);
        boolean possuiDespesas = despesaRepository.existsByFormaPagamentoId(id);
        if (possuiDespesas) {
            throw new BusinessException("A forma de pagamento '" + forma.getNome() + "' não pode ser excluída pois possui despesas associadas. Considere inativá-la.");
        }
        formaPagamentoRepository.delete(forma);
    }

    public FormaPagamento buscarEntidadePorId(Long id) {
        return formaPagamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Forma de pagamento não encontrada com o ID:"+id));
    }
}
