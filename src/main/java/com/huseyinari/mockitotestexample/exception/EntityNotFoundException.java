package com.huseyinari.mockitotestexample.exception;

public class EntityNotFoundException extends RuntimeException {
  private Object id;

  public EntityNotFoundException(Long id) {
    super(new StringBuilder()
            .append(id.toString())
            .append(" id'li kayıt bulunamadı !")
            .toString()
    );
    this.id = id;
  }
}
