package com.huseyinari.mockitotestexample.service;

import com.huseyinari.mockitotestexample.dto.UserDTO;
import com.huseyinari.mockitotestexample.entity.User;
import com.huseyinari.mockitotestexample.exception.EntityNotFoundException;
import com.huseyinari.mockitotestexample.mapper.UserMapper;
import com.huseyinari.mockitotestexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final MailService mailService;

  public List<UserDTO> getAll() {
    List<User> userList = repository.findAll();
    return mapper.toDtoList(userList);
  }

  public UserDTO getOne(Long id) throws EntityNotFoundException{
    User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    return mapper.toDto(user);
  }

  public UserDTO create(@Valid UserDTO userDTO) {
    if (userDTO.getId() != null)
      throw new RuntimeException("Yeni kaydedilecek kullanıcı id içeremez.");

    User user = repository.save(mapper.toEntity(userDTO));
    mailService.sendEMail(user.getName(), user.getSurname());

    return mapper.toDto(user);
  }

  public UserDTO update(UserDTO userDTO) {
    if (userDTO.getId() == null)
      throw new RuntimeException("Güncellenecek kullanıcının id'si bulunmalıdır.");

    UserDTO current = getOne(userDTO.getId());

    if (StringUtils.hasText(userDTO.getName()))
      current.setName(userDTO.getName());
    if (StringUtils.hasText(userDTO.getSurname()))
      current.setSurname(userDTO.getSurname());

    current.setEmail(userDTO.getEmail());
    current.setAge(userDTO.getAge());

    repository.save(mapper.toEntity(current));
    return current;
  }

  public void delete(Long id) {
    UserDTO userDTO = getOne(id);
    repository.deleteById(userDTO.getId());
  }

}
