package com.huseyinari.mockitotestexample.dto.error;

import lombok.Data;
import org.apache.tomcat.util.bcel.Const;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorDTO {
    private List<ConstraintError> errors;
    private String error;
    private LocalDateTime dateTime;
}
