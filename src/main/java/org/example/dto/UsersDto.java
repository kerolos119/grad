package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Gender;
import org.example.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class UsersDto  {
    @NotBlank(message = "Name required")
    private String username;
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^[0-9+]+$",message = "Invalid phone number")
    private String phoneNumber;
    @NotNull(message = "Gender required")
    private Gender gender;
    @NotBlank
    @Size(min = 8)
    private String password;
    private Role role;




}
