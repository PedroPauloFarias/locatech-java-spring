package br.com.fiap.locatech.locatech.entities;

import br.com.fiap.locatech.locatech.dtos.AluguelRequestDTO;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class Aluguel {
    private Long id;
    @NotNull(message = "O id da pessoa é obrigatório")
    @Positive(message = "O id da pessoa deve ser maior que zero")
    private Long pessoaId; //pessoa_id
    @NotNull(message = "O id do veículo é obrigatório")
    @Positive(message = "O id do veículo deve ser maior que zero")
    private Long veiculoId; //veiculo_id
    private String veiculoModelo;
    private String pessoaCpf;
    private String pessoaNome;
    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;
    @NotNull(message = "A data de fim é obrigatória")
    private LocalDate dataFim;
    private BigDecimal valorTotal;

    @AssertTrue(message = "A data de fim deve ser posterior à data de início")
    public boolean isPeriodoValido() {
        return dataInicio == null || dataFim == null || dataFim.isAfter(dataInicio);
    }

    public Aluguel(AluguelRequestDTO aluguelDTO, BigDecimal valor) {
        this.pessoaId = aluguelDTO.pessoaId();
        this.veiculoId = aluguelDTO.veiculoId();
        this.dataInicio = aluguelDTO.dataInicio();
        this.dataFim = aluguelDTO.dataFim();
        this.valorTotal = valor;

    }
}
