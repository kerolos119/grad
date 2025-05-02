package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.model.Role;
import org.example.model.TokenInfo;
import org.example.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CustomUserDetailsServices implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServices.class);
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final UserRepository userRepository;

    /**
     * Validates a token by checking if the user still exists and has proper credentials
     */
    public Boolean isValid(TokenInfo tokenInfo) {
        try {
            if (tokenInfo == null) {
                logger.warn("Token info is null");
                return false;
            }
            
            Long userId = tokenInfo.getUserId();
            String email = tokenInfo.getEmail();
            
            if (userId == null || email == null) {
                logger.warn("Token missing essential claims: userId={}, email={}", userId, email);
                return false;
            }
            
            if (!isValidEmail(email)) {
                logger.warn("Invalid email format in token: {}", email);
                return false;
            }
            
            // Check if user exists with these credentials
            boolean userExists = userRepository.existsUserWithCredentials(email, userId);
            
            if (!userExists) {
                logger.warn("User not found or credentials don't match: userId={}, email={}", userId, email);
            }
            
            return userExists;
        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Simple email format validation
     */
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Loads user details by email for Spring Security authentication
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);
        
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        // Convert role to Spring Security authority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);
        
        logger.debug("User found with role: {}", user.getRole());
        
        // Build and return user details
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    
    /**
     * Find a user by their ID
     */
    @Transactional
    public Users findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", userId);
                    return new UsernameNotFoundException("User not found with ID: " + userId);
                });
    }
    
    /**
     * Find a user by their email
     */
    @Transactional
    public Users findByEmail(String email) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }
}
