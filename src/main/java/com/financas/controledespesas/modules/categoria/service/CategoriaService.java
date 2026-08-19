package com.financas.controledespesas.modules.categoria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financas.controledespesas.exception.BusinessException;
import com.financas.controledespesas.exception.ResourceNotFoundException;
import com.financas.controledespesas.modules.categoria.dto.CategoriaRequestDTO;
import com.financas.controledespesas.modules.categoria.dto.CategoriaResponseDTO;
import com.financas.controledespesas.modules.categoria.model.Categoria;
import com.financas.controledespesas.modules.categoria.repository.CategoriaRepository;
import com.financas.controledespesas.modules.despesa.repository.DespesaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final DespesaRepository despesaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas(Boolean apenasAtivas) {
        List<Categoria> categorias = Boolean.TRUE.equals(apenasAtivas)
        ? categoriaRepository.findByAtivoTrueOrderByNomeAsc()
        : categoriaRepository.findAll();
        return categorias.stream()
            .map(CategoriaResponseDTO::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe uma categoria cadastrada com o nome '"+dto.nome()+"'");
        }
        Categoria categoria = Categoria.builder()
            .nome(dto.nome())
            .descricao(dto.descricao())
            .corHex(dto.corHex())
            .icone(dto.icone())
            .ativo(true)
            .build();
        Categoria salva = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(salva);
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = buscarEntidadePorId(id);
        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)) {
            throw new BusinessException("Já existe outra categoria com o nome '"+dto.nome()+"'");
        }
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setCorHex(dto.corHex());
        categoria.setIcone(dto.icone());
        Categoria atualizada = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(atualizada);
    }

    @Transactional
    public CategoriaResponseDTO alterarStatus(Long id, boolean ativo) {
        Categoria categoria = buscarEntidadePorId(id);
        categoria.setAtivo(ativo);
        Categoria salva = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(salva);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        boolean possuiDespesas = despesaRepository.existsByCategoriaId(id);
        if (possuiDespesas) {
            throw new BusinessException("A categoria " + categoria.getNome() + "' não pode ser excluída, pois possui despesas associadas. Considere inativá-la.'" );
        }
        categoriaRepository.delete(categoria);
    }

    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
    }
}
