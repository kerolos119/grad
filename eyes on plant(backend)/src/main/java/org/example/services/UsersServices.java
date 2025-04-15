package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.dto.Credentials;
import org.example.dto.PageResult;
import org.example.dto.UsersDto;
import org.example.exception.DuplicateEmailException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersServices {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;


    public UsersDto create(UsersDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email is already registered!"); // Custom exception
        }
        Users users = mapper.toEntity(dto);
        users.setPassword(encoder.encode(dto.getPassword()));
        Users savedUser = repository.save(users);
        return mapper.toDto(savedUser);}

    public void delete(Long userId) {
        Users users = repository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        repository.delete(users);
    }

    public UsersDto update(Long userId, UsersDto dto) {
        Users users=repository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        mapper.updateToEntity(dto,users);
        users.setPassword(encoder.encode(dto.getPassword()));
        Users updateUser=repository.save(users);
        return mapper.toDto(updateUser);
    }



    public UsersDto findByName(String userName) {
        if (userName==null || userName.trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return Optional.ofNullable(repository.findByUserName(userName.trim()))
                .map(mapper::toDto)
                .orElseThrow(()->new UserNotFoundException("User not found with name:"+userName));
    }

    public UsersDto findByEmail(String email) {
        if (email == null ||  email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return Optional.of(repository.findByEmail(email.trim()))
                .map(mapper::toDto)
                .orElseThrow(()->new UserNotFoundException("User not found with email:"+email));
    }


    public PageResult<UsersDto> search(String userName, String email, String phone, Pageable pageable) {
        return null;
    }

//    public String login(Credentials credentials) {
//    }
}
