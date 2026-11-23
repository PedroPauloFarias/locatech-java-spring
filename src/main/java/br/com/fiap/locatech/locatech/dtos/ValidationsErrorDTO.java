package br.com.fiap.locatech.locatech.dtos;

import java.util.List;

public record ValidationsErrorDTO(List<String> errors, int status) {
}
