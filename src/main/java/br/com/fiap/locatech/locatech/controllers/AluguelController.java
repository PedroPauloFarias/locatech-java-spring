package br.com.fiap.locatech.locatech.controllers;
import br.com.fiap.locatech.locatech.dtos.AluguelRequestDTO;
import br.com.fiap.locatech.locatech.entities.Aluguel;
import br.com.fiap.locatech.locatech.services.AluguelService;
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
@RequestMapping({"/alugueis", "/v1/alugueis", "/v2/alugueis"})
@Tag(name = "Aluguéis", description = "Operações de locação. Rotas legadas, v1 e v2 coexistem.")
public class AluguelController {
    private static final Logger logger = LoggerFactory.getLogger(AluguelController.class);

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;

    }

    @GetMapping
    @Operation(summary = "Lista aluguéis")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Aluguel>> findAllAlugueis(
            @Parameter(description = "Número da página, começando em 1", example = "1")
            @RequestParam("page") int page,
            @Parameter(description = "Quantidade por página (máximo 100)", example = "10")
            @RequestParam("size") int size
    ) {
        logger.info("/alugueis");
        var aluguel = this.aluguelService.findAllAlugueis(page, size);
        return ResponseEntity.ok(aluguel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca aluguel por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluguel encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Aluguel> findAluguel(
            @Parameter(description = "Identificador do aluguel", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("/alugueis/" + id);
        var aluguel = this.aluguelService.findAluguelById(id);
        return ResponseEntity.ok(aluguel);
    }

    @PostMapping
    @Operation(summary = "Cria um aluguel", description = "Valida o período e a disponibilidade do veículo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aluguel criado"),
            @ApiResponse(responseCode = "400", description = "Dados ou período inválidos"),
            @ApiResponse(responseCode = "404", description = "Pessoa ou veículo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Veículo indisponível no período"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> saveAluguel(
            @Valid @RequestBody AluguelRequestDTO aluguel
    ) {
        logger.info("POST -> /alugueis");
        this.aluguelService.saveAluguel(aluguel);
        return ResponseEntity.status(201).build();

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um aluguel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluguel atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados ou período inválidos"),
            @ApiResponse(responseCode = "404", description = "Aluguel ou veículo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Veículo indisponível no período"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> updateAluguel(
            @Parameter(description = "Identificador do aluguel", example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody Aluguel aluguel
    ) {
        logger.info("PUT -> /alugueis" + id);
        this.aluguelService.updateAluguel(aluguel, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um aluguel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluguel excluído"),
            @ApiResponse(responseCode = "404", description = "Aluguel não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteAluguel(
            @Parameter(description = "Identificador do aluguel", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("DELETE ->/alugueis");
        this.aluguelService.delete(id);
        return ResponseEntity.ok().build();
    }
}
