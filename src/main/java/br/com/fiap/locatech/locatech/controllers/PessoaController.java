package br.com.fiap.locatech.locatech.controllers;

import br.com.fiap.locatech.locatech.dtos.PessoaRequestDTO;
import br.com.fiap.locatech.locatech.entities.Pessoa;
import br.com.fiap.locatech.locatech.services.PessoaService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/pessoas", "/v1/pessoas", "/v2/pessoas"})
@Tag(name = "Pessoas", description = "Cadastro e consulta de pessoas. Rotas legadas, v1 e v2 coexistem.")
public class PessoaController {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    @Operation(summary = "Lista pessoas", description = "Retorna uma página de pessoas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Pessoa>> findAllPessoas(
            @Parameter(description = "Número da página, começando em 1", example = "1")
            @RequestParam("page") int page,
            @Parameter(description = "Quantidade por página (máximo 100)", example = "10")
            @RequestParam("size") int size
    ) {
        logger.info("/pessoas");
        var pessoas = this.pessoaService.findAllPessoas(page, size);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca pessoa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Pessoa> findPessoa(
            @Parameter(description = "Identificador da pessoa", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("/pessoas/" + id);
        var pessoas = this.pessoaService.findPessoaById(id);
        return ResponseEntity.ok(pessoas);
    }

    @PostMapping
    @Operation(summary = "Cadastra uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pessoa cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Regra de integridade violada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> savePessoa(
           @Valid @RequestBody PessoaRequestDTO pessoas
    ) {
        logger.info("POST -> /pessoa");
        this.pessoaService.savePessoa(pessoas);
        return ResponseEntity.status(201).build();

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Operação conflitante"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> updatePessoa(
            @Parameter(description = "Identificador da pessoa", example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody Pessoa pessoas
    ) {
        logger.info("PUT -> /pessoa" + id);
        this.pessoaService.updatePessoa(pessoas, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma pessoa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa excluída"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Pessoa possui dados relacionados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletePessoa(
            @Parameter(description = "Identificador da pessoa", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("DELETE ->/pessoas");
        this.pessoaService.delete(id);
        return ResponseEntity.ok().build();
    }

}
