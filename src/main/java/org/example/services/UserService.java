package org.example.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.dto.Credentials;
import org.example.dto.UsersDto;
import org.example.exception.NotFoundException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * Create a new user
     */
    public UsersDto create(UsersDto dto) {
        // Validate unique constraints
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserException("Email is already registered");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserException("Username is already taken");
        }

        // Validate required fields
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new UserException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new UserException("Last name is required");
        }
        if (dto.getGender() == null) {
            throw new UserException("Gender is required");
        }

        // Map DTO to entity and encode password
        Users user = UserMapper.INSTANCE.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // Save and return DTO
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    /**
     * Find user by ID
     */
    public UsersDto findById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        return userRepository.findById(userId)
                .map(UserMapper.INSTANCE::toDto)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    /**
     * Delete a user by ID
     */
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Update user information
     */
    public UsersDto update(Long userId, @Valid UsersDto dto) {
        // Find user
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        
        // Update email if provided and not already in use
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserException("Email already registered");
            }
            user.setEmail(dto.getEmail());
        }
        
        // Update username if provided and not already in use
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new UserException("Username already taken");
            }
            user.setUsername(dto.getUsername());
        }
        
        // Update password if provided
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // Update other fields if provided
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        
        // Save and return updated DTO
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    /**
     * Search users with filters
     */
    public Page<UsersDto> search(String username, String email, String phoneNumber, Pageable pageable) {
        Specification<Users> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }
            
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }
            
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return userRepository.findAll(spec, pageable).map(UserMapper.INSTANCE::toDto);
    }

    /**
     * Authenticate user and generate token
     */
    public AuthResponse authenticate(@Valid Credentials request) {
        // Validate credentials
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new UserException("Email is required");
        }
        
        // Find user by email
        Users user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new UserException("Invalid credentials");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException("Invalid credentials");
        }
        
        // Generate token and create response
        String token = jwtUtils.generateAccessToken(user);
        return new AuthResponse(token, UserMapper.INSTANCE.toDto(user));
    }

    /**
     * Find user by username
     */
    public UsersDto findByName(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        
        Users user = userRepository.findByUsername(username.trim());
        if (user == null) {
            throw new NotFoundException("User not found with username: " + username);
        }
        
        return UserMapper.INSTANCE.toDto(user);
    }

    /**
     * Find user by email
     */
    public UsersDto findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        Users user = userRepository.findByEmail(email.trim());
        if (user == null) {
            throw new NotFoundException("User not found with email: " + email);
        }
        
        return UserMapper.INSTANCE.toDto(user);
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