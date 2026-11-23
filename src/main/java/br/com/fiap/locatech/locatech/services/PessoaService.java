package br.com.fiap.locatech.locatech.services;

import br.com.fiap.locatech.locatech.dtos.PessoaRequestDTO;
import br.com.fiap.locatech.locatech.entities.Pessoa;
import br.com.fiap.locatech.locatech.repositores.PessoaRepository;
import br.com.fiap.locatech.locatech.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> findAllPessoas(int page, int size) {
        validarPaginacao(page, size);
        int offset = (page - 1) * size;
        return this.pessoaRepository.findAll(size, offset);
    }

    public Pessoa findPessoaById(Long id) {
        return this.pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Pessoa não encontrada"));
    }

    public void savePessoa(PessoaRequestDTO pessoaDTO) {
        var pessoas = criarPessoa(pessoaDTO);
        var save = this.pessoaRepository.save(pessoas);
        Assert.state(save == 1, "Erro ao salvar pessoa"
                + pessoas.getNome());
    }

    public void updatePessoa(Pessoa pessoas, Long id) {
        var update = this.pessoaRepository.update(pessoas, id);
        if (update == 0) {
            throw new ResourceNotFoundException("Pessoa não encontrado");
        }
    }

    public void delete(Long id) {
        var delete = this.pessoaRepository.delete(id);
        if (delete == 0) {
            throw new ResourceNotFoundException("Pessoa não encontrado");
        }
    }

    private Pessoa criarPessoa(PessoaRequestDTO pessoaDTO) {
        return new Pessoa(
                null,
                pessoaDTO.nome(),
                pessoaDTO.cpf(),
                pessoaDTO.telefone(),
                pessoaDTO.email()
                );
    }

    private void validarPaginacao(int page, int size) {
        if (page < 1 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A página deve ser maior que zero e o tamanho deve estar entre 1 e 100"
            );
        }
    }
}
