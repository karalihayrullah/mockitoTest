package com.huseyinari.mockitotestexample.mapper;

import com.huseyinari.mockitotestexample.dto.UserDTO;
import com.huseyinari.mockitotestexample.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);
    UserDTO toDto(User entity);
    List<UserDTO> toDtoList(List<User> userList);
    List<User> toEntityList(List<UserDTO> userDTOList);
}
