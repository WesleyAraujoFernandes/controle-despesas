package com.financas.controledespesas.modules.pagador.controller;

import java.util.List;

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

import com.financas.controledespesas.modules.categoria.service.FormaPagamentoService;
import com.financas.controledespesas.modules.pagador.dto.FormaPagamentoRequestDTO;
import com.financas.controledespesas.modules.pagador.dto.FormaPagamentoResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/formas-pagamento")
@RequiredArgsConstructor
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;

    @GetMapping
    public ResponseEntity<List<FormaPagamentoResponseDTO>> listar(
            @RequestParam(name = "apenasAtivas", required = false, defaultValue = "false") Boolean apenasAtivas) {
        List<FormaPagamentoResponseDTO> formas = formaPagamentoService.listarTodas(apenasAtivas);
        return ResponseEntity.ok(formas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        FormaPagamentoResponseDTO forma = formaPagamentoService.buscarPorId(id);
        return ResponseEntity.ok(forma);
    }

    @PostMapping
    public ResponseEntity<FormaPagamentoResponseDTO> criar(@Valid @RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO criada = formaPagamentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO atualizada = formaPagamentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FormaPagamentoResponseDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo) {
        FormaPagamentoResponseDTO alterada = formaPagamentoService.alterarStatus(id, ativo);
        return ResponseEntity.ok(alterada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        formaPagamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}