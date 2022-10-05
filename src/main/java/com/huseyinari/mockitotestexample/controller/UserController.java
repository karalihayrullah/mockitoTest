package com.huseyinari.mockitotestexample.controller;

import com.huseyinari.mockitotestexample.dto.UserDTO;
import com.huseyinari.mockitotestexample.entity.User;
import com.huseyinari.mockitotestexample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserService service;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable Long id) {
    return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
    return new ResponseEntity<>(service.create(userDTO), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
//    return new ResponseEntity<>(service.update(userDTO), HttpStatus.OK);
    UserDTO result = service.update(userDTO);
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
