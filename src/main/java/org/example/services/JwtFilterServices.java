package org.example.services;

import jakarta.persistence.Column;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer")){
                String token = authorization.substring(7);
                if (!jwtUtils.isValid(token)){
                    throw new CustomException("Invalid token" , HttpStatus.UNAUTHORIZED);
                }

                TokenInfo tokenInfo = jwtUtils.extractInfo(token);

                if (!userDetailsServices.isValid(tokenInfo)){
                    throw new CustomException("Invalid token" , HttpStatus.UNAUTHORIZED);
                }

                List<GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(tokenInfo.getRoles()));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(tokenInfo.getEmail(), null,authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request,response);

        }
        catch (CustomException ex) {
            System.err.println("Custom Exception Caught:" +ex.getMessage());

            response.setStatus(ex.getStatus().value());
            response.setContentType("application/json");
            response.setHeader("ERROR_CODE",ex.getMessage());
            response.getWriter().write(ex.getMessage());
        }
        catch (RuntimeException ex){
            System.err.println("runTime exception caught:" +ex.getMessage());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setHeader("ERROR_CODE",ex.getMessage());
            response.getWriter().write(ex.getMessage());
        }
        catch (Exception ex){
            System.err.println("Unexpected exception caught:" + ex.getMessage());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setHeader("ERROR_CODE",ex.getMessage());
            response.getWriter().write("{\"error\":\"Internal server error\",\"message\":\""+ex.getMessage()+"\"}");
        }

    }
}
