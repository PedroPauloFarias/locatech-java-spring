package br.com.fiap.locatech.locatech.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record AluguelRequestDTO(
        @NotNull(message = "O id da pessoa não pode ser nulo")
        @Positive(message = "O id da pessoa deve ser maior que zero")
        Long pessoaId,
        @NotNull(message = "O id do veículo não pode ser nulo")
        @Positive(message = "O id do veículo deve ser maior que zero")
        Long veiculoId,
        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,
        @NotNull(message = "A data de fim é obrigatória")
        LocalDate dataFim) {

    @AssertTrue(message = "A data de fim deve ser posterior à data de início")
    public boolean isPeriodoValido() {
        return dataInicio == null
                || dataFim == null
                || dataFim.isAfter(dataInicio);
    }
}
