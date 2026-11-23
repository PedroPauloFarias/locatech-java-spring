package br.com.fiap.locatech.locatech.entities;


import br.com.fiap.locatech.locatech.dtos.PessoaRequestDTO;
import br.com.fiap.locatech.locatech.validations.ValidCpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Pessoa {
    private Long id;
    @NotBlank(message = "O nome não pode ficar vazio")
    private String nome;
    @NotBlank(message = "O CPF não pode ficar vazio")
    @ValidCpf
    private String cpf;
    private String telefone;
    @Email(message = "O e-mail deve ter um formato válido")
    private String email;

    public Pessoa(PessoaRequestDTO pessoaDTO, String valor) {
        this.nome = pessoaDTO.nome();
        this.cpf = pessoaDTO.cpf();
        this.telefone = pessoaDTO.telefone();
        this.email = pessoaDTO.email();
    }

}
