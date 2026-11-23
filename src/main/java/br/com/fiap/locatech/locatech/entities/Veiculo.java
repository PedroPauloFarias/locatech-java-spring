package br.com.fiap.locatech.locatech.entities;

import lombok.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Veiculo {
    private Long id;
    @NotBlank(message = "A marca não pode ficar vazia")
    private String marca;
    @NotBlank(message = "O modelo não pode ficar vazio")
    private String modelo;
    @NotBlank(message = "A placa não pode ficar vazia")
    private String placa;
    @Min(value = 1886, message = "O ano deve ser igual ou posterior a 1886")
    private int ano;
    @NotBlank(message = "A cor não pode ficar vazia")
    private String cor;
    @NotNull(message = "O valor da diária é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor da diária deve ser maior que zero")
    private BigDecimal valorDiaria;

}
