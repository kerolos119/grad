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
    private String email;
    @NotEmpty
    public String password;

    public CharSequence password() {
        return password();
    }
}
