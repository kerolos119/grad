package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.dto.Credentials;
import org.example.dto.UsersDto;
import org.example.exception.CustomException;
import org.example.mapper.UserMapper;
import org.example.model.ApiResponse;
import org.example.model.AuthResponse;
import org.example.model.TokenInfo;
import org.example.services.CustomUserDetailsServices;
import org.example.services.UserService;
import org.example.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final CustomUserDetailsServices userDetailsServices;

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody Credentials credentials) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword()
                    )
            );
            
            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Find user and generate tokens
            Users user = userDetailsServices.findByEmail(credentials.getEmail());
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);
            
            // Create response with tokens and user data
            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    refreshToken,
                    UserMapper.INSTANCE.toDto(user) // Convert user to DTO
            );
            
            return ResponseEntity.ok(
                    ApiResponse.success(authResponse, "Login successful")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.unauthorized("Invalid credentials")
            );
        }
    }

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody UsersDto userDto) {
        try {
            // Create the new user
            UsersDto createdUser = userService.create(userDto);
            
            // Get full user object for token generation
            Users user = userDetailsServices.findByEmail(createdUser.getEmail());
            
            // Generate tokens
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);
            
            // Create response with tokens and user data
            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    refreshToken,
                    createdUser
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.created(authResponse, "Registration successful")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.badRequest(e.getMessage())
            );
        }
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader) {
        try {
            // Extract refresh token from header
            if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
                throw new CustomException("Invalid token format", HttpStatus.BAD_REQUEST);
            }
            
            String refreshToken = refreshTokenHeader.substring(7);
            
            // Validate token is a refresh token
            if (!jwtUtils.isValid(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
                throw new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
            }
            
            // Extract user info from token
            TokenInfo tokenInfo = jwtUtils.extractInfo(refreshToken);
            
            // Validate user exists
            if (!userDetailsServices.isValid(tokenInfo)) {
                throw new CustomException("User not found", HttpStatus.UNAUTHORIZED);
            }
            
            // Get user and generate new access token
            Users user = userDetailsServices.findById(tokenInfo.getUserId());
            String newAccessToken = jwtUtils.generateAccessToken(user);
            
            // Return new access token
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            
            return ResponseEntity.ok(
                    ApiResponse.success(tokens, "Token refreshed successfully")
            );
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(
                    ApiResponse.error(e.getStatus(), e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.serverError("Failed to refresh token")
            );
        }
    }
} 