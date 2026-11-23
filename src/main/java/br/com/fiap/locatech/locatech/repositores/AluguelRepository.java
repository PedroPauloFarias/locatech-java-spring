package br.com.fiap.locatech.locatech.repositores;

import br.com.fiap.locatech.locatech.entities.Aluguel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AluguelRepository {
    Optional<Aluguel> findById(Long id);

    List<Aluguel> findAll(int size, int offset);

    Integer save(Aluguel aluguel);

    Integer update(Aluguel aluguel, Long id);


    Integer delete(Long id);

    boolean existeConflitoDePeriodo(Long veiculoId, LocalDate dataInicio, LocalDate dataFim, Long aluguelIdIgnorado);
}
