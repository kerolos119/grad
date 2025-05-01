package org.example.document;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.example.model.Gender;
import org.example.model.Role;


@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public  class Users   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", length = 50, nullable = false,unique = true)
    @NotBlank(message = "Name required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String username;

    @Column(name = "email", unique = true, length = 50, nullable = false)
    @Size(max = 50)
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "password", length = 50, nullable = false)
    @NotBlank(message = "Password required")
    @Size(min = 6 , message = "Password must contain at least 6 characters")
    private String password;

    @Column(name = "phone_number", length = 15, nullable = false)
    @Pattern(regexp = "^[0-9+]+$",message = "Invalid phone number")
    private String phonenumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE', 'FEMALE')") // تحديد نوع الحقل يدويًا
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    @Builder.Default
    private Role role = Role.USER;






//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Plants> plants = new ArrayList<>();
}
