package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.model.TokenInfo;
import org.example.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;  // Spring’s UserDetails implementation
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServices implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * التحقق من صحة التوكن عن طريق معرّف المستخدم والإيميل والأدوار.
     */
    public Boolean isValid(TokenInfo tokenInfo) {
        try {
            Long userId = Long.parseLong(tokenInfo.getUserId());
            if (!isValidEmail(tokenInfo.getEmail())) {
                return false;
            }
            return userRepository.existsUserWithCredentials(
                    tokenInfo.getEmail(),
                    userId
            );
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * تحقق بسيط من صيغة البريد الإلكتروني.
     */
    private boolean isValidEmail(String email) {
        return email != null
                && email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * تحميل تفاصيل المستخدم باستخدام البريد الإلكتروني وتحويل الأدوار إلى GrantedAuthority.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);
        if (users == null) {
            throw new UsernameNotFoundException(
                    "User not found with email: " + email
            );
        }

        // تحويل الأدوار إلى GrantedAuthority


        // بناء وإرجاع كائن UserDetails
        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .build();
    }

}
