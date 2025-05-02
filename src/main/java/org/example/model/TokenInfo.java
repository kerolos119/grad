package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenInfo {
    private String email;
    private Long userId;
    private String roles;
    private String firstName;
    private String lastName;
    private String tokenType;
    private Date expiredAt;
    private Date issuedAt;
}
