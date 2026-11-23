package br.com.fiap.locatech.locatech.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import br.com.fiap.locatech.locatech.validations.ValidCpf;


public record PessoaRequestDTO(
        @NotBlank(message = "O nome não pode ficar vazio")
        String nome,
        @NotBlank(message = "O CPF não pode ficar vazio")
        @ValidCpf
        String cpf,
        String telefone,
        @Email(message = "O e-mail deve ter um formato válido")
        String email
) {
}
