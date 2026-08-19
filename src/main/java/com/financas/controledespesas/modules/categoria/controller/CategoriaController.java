package com.financas.controledespesas.modules.categoria.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financas.controledespesas.modules.categoria.dto.CategoriaRequestDTO;
import com.financas.controledespesas.modules.categoria.dto.CategoriaResponseDTO;
import com.financas.controledespesas.modules.categoria.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(
            @RequestParam(name = "apenasAtivas", required = false, defaultValue = "false") Boolean apenasAtivas) {
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas(apenasAtivas);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO criada = categoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO atualizada = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoriaResponseDTO> alterarStatus(
        @PathVariable Long id,
        @RequestParam boolean ativo
    ) {
        CategoriaResponseDTO alterada = categoriaService.alterarStatus(id, ativo);
        return ResponseEntity.ok(alterada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
