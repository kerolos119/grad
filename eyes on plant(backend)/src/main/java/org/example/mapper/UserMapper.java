package org.example.mapper;

import org.example.document.Users;

import org.example.dto.UsersDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<UsersDto,Users>{

    public UserMapper() {
        super(UsersDto.class, Users.class);
    }

    @Override
    public Users updateToEntity(UsersDto dto, Users entity) {
        entity.setUserName(dto.getUserName());
        entity.setEmail(dto.getEmail());
        entity.setPhonenumber(dto.getPhoneNumber());
        entity.setPassword(dto.getPassword());
        return entity;
    }

}
