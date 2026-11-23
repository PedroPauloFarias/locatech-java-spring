package br.com.fiap.locatech.locatech.repositores;

import br.com.fiap.locatech.locatech.entities.Pessoa;
import br.com.fiap.locatech.locatech.entities.Pessoa;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PessoaRepositoryImp implements PessoaRepository {
    private final JdbcClient jdbcClient;

    public PessoaRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Pessoa> findById(Long id) {
        return this.jdbcClient.sql("SELECT * FROM pessoas WHERE id = :id")
                .param("id", id)
                .query(Pessoa.class)
                .optional();
    }

    @Override
    public List<Pessoa> findAll(int size, int offset) {
        return this.jdbcClient
                .sql("SELECT * FROM pessoas LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query(Pessoa.class)
                .list();
    }

    @Override
    public Integer save(Pessoa pessoas) {
        return this.jdbcClient
                .sql("INSERT INTO Pessoas(nome, cpf, telefone, email) VALUES(:nome, :cpf, :telefone, :email)")
                .param("nome", pessoas.getNome())
                .param("cpf", pessoas.getCpf())
                .param("telefone", pessoas.getTelefone())
                .param("email", pessoas.getEmail())
                .update();
    }

    @Override
    public Integer update(Pessoa pessoas, Long id) {
        return this.jdbcClient
                .sql("UPDATE Pessoas SET nome = :nome, cpf= :cpf, telefone = :telefone, email=  :email WHERE id = :id")
                .param("id", id)
                .param("nome", pessoas.getNome())
                .param("cpf", pessoas.getCpf())
                .param("telefone", pessoas.getTelefone())
                .param("email", pessoas.getEmail())
                .update();
    }

    @Override
    public Integer delete(Long id) {
        return this.jdbcClient
                .sql("DELETE FROM pessoas WHERE id = :id")
                .param("id", id)
                .update();
    }
}
