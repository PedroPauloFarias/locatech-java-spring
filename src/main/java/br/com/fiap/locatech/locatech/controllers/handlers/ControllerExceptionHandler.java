package br.com.fiap.locatech.locatech.controllers.handlers;

import br.com.fiap.locatech.locatech.dtos.ApiErrorDTO;
import br.com.fiap.locatech.locatech.dtos.ResourceNotFoundDTO;
import br.com.fiap.locatech.locatech.dtos.ValidationsErrorDTO;
import br.com.fiap.locatech.locatech.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDTO> handlerResourceNotFoundException(ResourceNotFoundException e){
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new ResourceNotFoundDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationsErrorDTO> handlerMethoArgumentNotValidException(MethodArgumentNotValidException e){
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<String>();
        for (var error: e.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errors.add(error.getDefaultMessage());
            }
        }
        return ResponseEntity.status(status.value()).body(new ValidationsErrorDTO(errors, status.value()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiErrorDTO> handlerBadRequest(Exception e) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorDTO("Requisição inválida ou parâmetro obrigatório ausente", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDTO> handlerDataIntegrityViolation(DataIntegrityViolationException e) {
        var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
                .body(new ApiErrorDTO("A operação viola uma regra de integridade dos dados", status.value()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDTO> handlerResponseStatusException(ResponseStatusException e){
     var status = e.getStatusCode();
     return  ResponseEntity.status(status)
             .body(new ApiErrorDTO(e.getReason(), status.value()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handlerUnexpectedException(Exception e) {
        logger.error("Erro inesperado ao processar a requisição", e);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(new ApiErrorDTO("Erro interno do servidor", status.value()));
    }
}
