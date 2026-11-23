package br.com.fiap.locatech.locatech.controllers;

import br.com.fiap.locatech.locatech.entities.Veiculo;
import br.com.fiap.locatech.locatech.services.VeiculoService;
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
@RequestMapping({"/veiculos", "/v1/veiculos", "/v2/veiculos"})
@Tag(name = "Veículos", description = "Cadastro e consulta de veículos. Rotas legadas, v1 e v2 coexistem.")
public class VeiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VeiculoController.class);

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }


    // http://localhost::8080/veiculos?page=1&size=10
    @GetMapping
    @Operation(summary = "Lista veículos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<Veiculo>> findAllVeiculos(
            @Parameter(description = "Número da página, começando em 1", example = "1")
            @RequestParam("page") int page,
            @Parameter(description = "Quantidade por página (máximo 100)", example = "10")
            @RequestParam("size") int size
    ) {
        logger.info("/veiculos");
        var veiculos = this.veiculoService.findAllVeiculos(page, size);
        return ResponseEntity.ok(veiculos);
    }

    // http://localhost::8080/veiculos/1
    @GetMapping("/{id}")
    @Operation(summary = "Busca veículo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Veiculo> findVeiculo(
            @Parameter(description = "Identificador do veículo", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("/veiculos/" + id);
        var veiculo = this.veiculoService.findVeiculoById(id);
        return ResponseEntity.ok(veiculo);
    }

    @PostMapping
    @Operation(summary = "Cadastra um veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Operação conflitante"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> saveVeiculo(
          @Valid @RequestBody Veiculo veiculo
    ) {
        logger.info("POST -> /veiculos");
        this.veiculoService.saveVeiculo(veiculo);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Operação conflitante"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> updateVeiculo(
            @Parameter(description = "Identificador do veículo", example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody Veiculo veiculo
    ) {
        logger.info("PUT -> /veiculos/" + id);
        this.veiculoService.updateVeiculo(veiculo, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo excluído"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Veículo possui aluguéis relacionados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteVeiculo(
            @Parameter(description = "Identificador do veículo", example = "1")
            @PathVariable("id") Long id
    ) {
        logger.info("DELETE -> /veiculos/");
        this.veiculoService.delete(id);
        return ResponseEntity.ok().build();
    }
}