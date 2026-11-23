package br.com.fiap.locatech.locatech.repositores;

import br.com.fiap.locatech.locatech.entities.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {
    Optional<Pessoa> findById(Long id);

    List<Pessoa> findAll(int size, int offset);

    Integer save(Pessoa pessoas);

    Integer update(Pessoa pessoas, Long id);

    Integer delete(Long id);
}
