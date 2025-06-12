package org.example.mapper;

import jakarta.annotation.PostConstruct;
import org.example.document.Users;

import org.example.dto.UsersDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<UsersDto,Users>{

    public static AbstractMapper<UsersDto,Users> INSTANCE;

    public UserMapper() {
        super(UsersDto.class, Users.class);
    }
    
    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    @Override
    public Users updateToEntity(UsersDto dto, Users entity) {
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setGender(dto.getGender());
        return entity;
    }

}
