package com.huseyinari.mockitotestexample.handler;

import com.huseyinari.mockitotestexample.dto.error.ConstraintError;
import com.huseyinari.mockitotestexample.dto.error.ErrorDTO;
import com.huseyinari.mockitotestexample.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
    ErrorDTO errorDTO = new ErrorDTO();
    List<ConstraintError> errors = new ArrayList<>();

    e.getConstraintViolations().stream().forEach(item -> {
      ConstraintError constraintError = new ConstraintError();
      constraintError.setColumn(item.getPropertyPath().toString());
      constraintError.setMessage(item.getMessage());

      errors.add(constraintError);
    });

    errorDTO.setErrors(errors);
    errorDTO.setDateTime(LocalDateTime.now());

    return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleRuntimeError(RuntimeException e) {
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDateTime(LocalDateTime.now());
    errorDTO.setError(e.getMessage());
    return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
  }

}
