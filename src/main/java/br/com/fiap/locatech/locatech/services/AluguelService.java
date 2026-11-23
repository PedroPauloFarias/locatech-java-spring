package br.com.fiap.locatech.locatech.services;

import br.com.fiap.locatech.locatech.dtos.AluguelRequestDTO;
import br.com.fiap.locatech.locatech.entities.Aluguel;
import br.com.fiap.locatech.locatech.repositores.AluguelRepository;
import br.com.fiap.locatech.locatech.repositores.PessoaRepository;
import br.com.fiap.locatech.locatech.repositores.VeiculoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import br.com.fiap.locatech.locatech.services.exceptions.ResourceNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final VeiculoRepository veiculoRepository;
    private final PessoaRepository pessoaRepository;

    public AluguelService(
            AluguelRepository aluguelService,
            VeiculoRepository veiculoRepository,
            PessoaRepository pessoaRepository
    ) {
        this.aluguelRepository = aluguelService;
        this.veiculoRepository = veiculoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Aluguel> findAllAlugueis(int page, int size) {
        validarPaginacao(page, size);
        int offset = (page - 1) * size;
        return this.aluguelRepository.findAll(size, offset);
    }

    public Aluguel findAluguelById(Long id) {

        return this.aluguelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Aluguel não encontrado"));
    }

    public void saveAluguel(AluguelRequestDTO aluguel) {
        var aluguelEntity = calculaAluguel(aluguel);
        boolean existeConflito = this.aluguelRepository.existeConflitoDePeriodo(
                aluguelEntity.getVeiculoId(),
                aluguelEntity.getDataInicio(),
                aluguelEntity.getDataFim(),
                0L
        );
        if (existeConflito) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Veículo não disponível para o período informado"
            );
        }
        var save = this.aluguelRepository.save(aluguelEntity);
        Assert.state(save == 1, "Erro ao salvar aluguel" + aluguel.pessoaId());
    }

    public void updateAluguel(Aluguel aluguel, Long id) {
        this.aluguelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado"));
        this.pessoaRepository.findById(aluguel.getPessoaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
        if (aluguel.getVeiculoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O id do veículo é obrigatório");
        }
        this.veiculoRepository.findById(aluguel.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
        boolean existeConflito = this.aluguelRepository.existeConflitoDePeriodo(
                aluguel.getVeiculoId(),
                aluguel.getDataInicio(),
                aluguel.getDataFim(),
                id
        );
        if (existeConflito) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Veículo não disponível para o período informado"
            );
        }
        var update = this.aluguelRepository.update(aluguel, id);
        if (update == 0) {
            throw new ResourceNotFoundException("Aluguel não encontrado");
        }
    }

    public void delete(Long id) {
        var delete = this.aluguelRepository.delete(id);
        if (delete == 0) {
            throw new ResourceNotFoundException("Aluguel não encontrado");
        }
    }

    private Aluguel calculaAluguel(AluguelRequestDTO aluguelRequestDTO) {
        this.pessoaRepository.findById(aluguelRequestDTO.pessoaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada"));
        var veiculo = this.veiculoRepository.findById(aluguelRequestDTO.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
        var quantidadeDias = BigDecimal.valueOf(
                ChronoUnit.DAYS.between(aluguelRequestDTO.dataInicio(), aluguelRequestDTO.dataFim())
        );
        var valor = veiculo.getValorDiaria().multiply(quantidadeDias);
        return new Aluguel(aluguelRequestDTO, valor);
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
