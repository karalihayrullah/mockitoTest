package com.huseyinari.mockitotestexample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity(name = "User")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  @NotEmpty(message = "Ad boş olamaz.")
  private String name;

  @Column(name = "surname")
  @NotEmpty(message = "Soyad boş olamaz.")
  private String surname;

  @Column(name = "email")
  private String email;

  @Column(name = "age")
  private Integer age;


}
