package org.example.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.CustomException;
import org.example.model.TokenInfo;
import org.example.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilterServices extends OncePerRequestFilter {
    @Autowired
    @Lazy
    JwtUtils jwtUtils;

    @Autowired
    CustomUserDetailsServices userDetailsServices;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Skip filter completely for login endpoint
        if (request.getServletPath().contains("/api/v1/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String authorization = request.getHeader("Authorization");

            // If no authorization header or not Bearer token, continue the chain
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Process token
            String token = authorization.substring(7);
            if (!jwtUtils.isValid(token)){
                throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            TokenInfo tokenInfo = jwtUtils.extractInfo(token);

            if (!userDetailsServices.isValid(tokenInfo)){
                throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
            }

            List<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(tokenInfo.getRoles()));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(tokenInfo.getEmail(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Continue the filter chain
            filterChain.doFilter(request, response);
        }
        catch (CustomException ex) {
            // Don't continue the filter chain in exception cases
            SecurityContextHolder.clearContext();
            response.setStatus(ex.getStatus().value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        }
        catch (RuntimeException ex){
            // Don't continue the filter chain in exception cases
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        }
        catch (Exception ex){
            // Don't continue the filter chain in exception cases
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Internal server error\",\"message\":\"" + ex.getMessage() + "\"}");
        }
    }
}