package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    @NotEmpty
    private String username;
    @NotEmpty
    public String password;
    @NotEmpty
    private String email;


    public CharSequence password() {
        return password();
    }
}
