package org.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * طلب إعادة تعيين كلمة المرور.
 *
 * @param email        البريد الإلكتروني للمستخدم (مطلوب وصيغة صالحة)
 * @param newPassword  كلمة المرور الجديدة (مطلوب)
 * @param token        رمز التحقق لإعادة التعيين (مطلوب)
 */
public record ResetPasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "New password is required")
        String newPassword,

        @NotBlank(message = "Token is required")
        String token
) { }
