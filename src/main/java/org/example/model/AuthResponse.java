package org.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.document.Users;
import org.example.dto.UsersDto;

import java.time.Instant;

@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Instant expiredAt;
    private Users users;

    public AuthResponse(  String accessToken,
     String refreshToken,
     Instant expiredAt,
     Users users) {
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.expiredAt=expiredAt;
        this.users=users;
    }

    public AuthResponse(Object o, UsersDto dto) {
    }

    public AuthResponse AuthResponse(Object o, UsersDto dto) {return new AuthResponse(o,dto);
    }
}
