package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.UsersDto;

import java.time.Instant;

@NoArgsConstructor
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Instant expiredAt;
    private UsersDto users;

//    public AuthResponse(  String accessToken,
//     String refreshToken,
//     Instant expiredAt,
//     Users users) {
//        this.accessToken=accessToken;
//        this.refreshToken=refreshToken;
//        this.expiredAt=expiredAt;
//        this.users=users;
//    }

    public AuthResponse(String token, UsersDto dto) {
        this.accessToken = token;
        this.users = dto;
    }
    
    public AuthResponse(String accessToken, String refreshToken, UsersDto dto) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.users = dto;
    }

//    public AuthResponse AuthResponse(Object o, UsersDto dto) {return new AuthResponse(o,dto);
//    }
}
