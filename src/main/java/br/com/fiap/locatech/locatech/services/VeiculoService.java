package br.com.fiap.locatech.locatech.services;

import br.com.fiap.locatech.locatech.entities.Veiculo;
import br.com.fiap.locatech.locatech.repositores.VeiculoRepository;
import br.com.fiap.locatech.locatech.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<Veiculo> findAllVeiculos(int page, int size) {
        validarPaginacao(page, size);
        int offset = (page - 1) * size;
        return this.veiculoRepository.findAll(size, offset);
    }

    public Veiculo findVeiculoById(Long id) {
        return this.veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veiculo não encontrado"));
    }

    public void saveVeiculo(Veiculo veiculo) {
        var save = this.veiculoRepository.save(veiculo);
        Assert.state(save == 1, "Erro ao salvar veículo" + veiculo.getModelo());
    }

    public void updateVeiculo(Veiculo veiculo, Long id){
        var update = this.veiculoRepository.update(veiculo, id);
        if(update == 0){
            throw new ResourceNotFoundException("Veículo não encontrado");
        }
    }

    public void delete(Long id){
        var delete = this.veiculoRepository.delete(id);
        if (delete == 0){
            throw new ResourceNotFoundException("Veículo não encontrado");
        }
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
