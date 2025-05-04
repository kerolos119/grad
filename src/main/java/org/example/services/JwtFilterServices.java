package org.example.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.CustomException;
import org.example.model.TokenInfo;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilterServices extends OncePerRequestFilter {
    
    @Autowired
    @Lazy
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsServices userDetailsServices;
    
    @Value("${jwt.header:Authorization}")
    private String headerName;
    
    @Value("${jwt.prefix:Bearer }")
    private String headerPrefix;
    
    // List of paths to skip authentication
    private final List<String> publicPaths = Arrays.asList(
            "/api/v1/users/login",
            "/api/v1/users/register",
            "/api/v1/auth",
            "/swagger-ui",
            "/api-docs",
            "/api/products",
            "/api/plants",
            "/api/categories",
            "/api/payments/webhook",
            "/api/orders/track"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String requestPath = request.getServletPath();
        
        // Skip authentication for public paths
        if (isPublicPath(requestPath) || request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String authHeader = request.getHeader(headerName);

            // If no authorization header or not Bearer token, proceed to next filter
            if (authHeader == null || !authHeader.startsWith(headerPrefix)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract and validate token
            String token = authHeader.substring(headerPrefix.length());
            
            // Check if token is valid
            if (!jwtUtils.isValid(token)) {
                throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
            }
            
            // Check if token is a refresh token (not allowed for regular API access)
            if (jwtUtils.isRefreshToken(token) && !requestPath.contains("/api/v1/auth/refresh")) {
                throw new CustomException("Invalid token type", HttpStatus.UNAUTHORIZED);
            }

            // Extract token info and validate user
            TokenInfo tokenInfo = jwtUtils.extractInfo(token);
            
            if (!userDetailsServices.isValid(tokenInfo)) {
                throw new CustomException("Invalid user credentials", HttpStatus.UNAUTHORIZED);
            }

            // Set up Spring Security context with user authorities
            List<SimpleGrantedAuthority> authorities = 
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + tokenInfo.getRoles()));
            
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            tokenInfo.getEmail(), 
                            null, 
                            authorities
                    );
            
            // Add user details to authentication token
            authenticationToken.setDetails(tokenInfo);
            
            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Continue the filter chain
            filterChain.doFilter(request, response);
        }
        catch (CustomException ex) {
            handleSecurityException(response, ex.getStatus().value(), ex.getMessage());
        }
        catch (RuntimeException ex) {
            handleSecurityException(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
        catch (Exception ex) {
            handleSecurityException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Internal server error: " + ex.getMessage());
        }
    }
    
    /**
     * Check if the path is public (no authentication required)
     */
    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(path::startsWith);
    }
    
    /**
     * Handle security exceptions by setting appropriate response status and message
     */
    private void handleSecurityException(HttpServletResponse response, int status, String message) throws IOException {
        // Clear security context
        SecurityContextHolder.clearContext();
        
        // Set response status and content
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}