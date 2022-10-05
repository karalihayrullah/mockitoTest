package com.huseyinari.mockitotestexample.dto;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO implements Serializable {

    private Long id;
    @NotEmpty(message = "Ad boş olamaz.")
    private String name;
    @NotEmpty(message = "Soyad boş olamaz.")
    private String surname;
    private String email;
    private Integer age;

}
