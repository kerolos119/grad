package org.example.services;


import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.dto.Credentials;
import org.example.dto.UsersDto;
import org.example.exception.UserException;
import org.example.mapper.UserMapper;
import org.example.model.AuthResponse;
import org.example.repo.UserRepository;
import org.example.utils.JwtUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public UsersDto create(UsersDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new UserException("Email is already registered");
        }
        if (userRepository.existsByUsername(dto.getUsername())){
            throw new UserException("UserName is already taken");
        }

        var user = UserMapper.INSTANCE.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public void delete(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new UserException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public UsersDto update(Long userId, @Valid UsersDto dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(()->new UserException("User not found"));
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())){
            if (userRepository.existsByEmail(dto.getEmail())){
                throw new UserException("Email already registered");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public Page<UsersDto> search(String username, String email, String phone, Pageable pageable) {
        Specification<Users> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (email != null) {
                predicates.add(cb.equal(root.get("email"), email));
            }
            if (phone != null){
                predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(spec, pageable).map(UserMapper.INSTANCE::toDto);
    }

    public AuthResponse authenticate(@Valid Credentials request) {
        var user = userRepository.findByEmail(request.getEmail());
        if (!userRepository.existsByEmail(request.getEmail())){
            throw new UserException("Invalid creditials");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UserException("Invalid creditials");
        }
        return new AuthResponse(
                jwtUtils.generate(user), // يجب أن تُرجع String
                userMapper.toDto(user) // استبدال INSTANCE بـ instance عادي إن لزم
        );
    }

    public UsersDto findByName(String username) {
        if ((username == null || username.isEmpty())){
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        return Optional.ofNullable(userRepository.findByUsername(username.trim()))
                .map(userMapper::toDto)
                .orElseThrow(()->new UserException("user not found with name:" + username));
    }

    public UsersDto findByEmail(String email) {
        if ((email == null || email.isEmpty())){
        throw new IllegalArgumentException("email cannot be null or empty");
    }
        return Optional.ofNullable(userRepository.findByEmail(email.trim()))
                .map(userMapper::toDto)
                .orElseThrow(()->new UserException("user not found with email:" + email));
    }


//    public void resetPassword(ResetPasswordRequest request) {
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UserException("User not found"));
//
//        String tempPassword = "temp_" + System.currentTimeMillis();
//        user.setPassword(passwordEncoder.encode(tempPassword));
//        userRepository.save(user);
//    }
//
//
//    public void verifyEmail(String token) {
//        String email = jwtUtils.validateToken(token);
//        if (!userRepository.existsByEmail(email)){
//            throw new UserException("Invalid verfication token");
//        }
//    }
}