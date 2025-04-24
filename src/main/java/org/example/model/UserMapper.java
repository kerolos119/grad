package org.example.model;

import org.apache.catalina.User;
import org.example.dto.UsersDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UsersDto toDto(User entity);
    User toEntity(UsersDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UsersDto dto, @MappingTarget User entity);
}