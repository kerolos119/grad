package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.model.Gender;
import org.example.model.Role;
import org.example.model.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class UsersDto extends Auditable {
    @NotBlank(message = "Name required")
    private String username;
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^[0-9+]+$",message = "Invalid phone number")
    private String phoneNumber;
    @NotNull(message = "Gender required")
    private Gender gender;
    @NotBlank(message = "First name required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;
    @NotBlank(message = "Last name required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;
    @NotBlank
    @Size(min = 8)
    private String password;
    private Role role;




}
