// package org.example.config;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;

// import java.util.Locale;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// @SpringBootTest
// public class MessageSourceTest {

//     @Autowired
//     private MessageSource messageSource;

//     @Test
//     public void testEnglishMessages() {
//         // Set locale to English
//         LocaleContextHolder.setLocale(Locale.ENGLISH);

//         // Test general messages
//         assertEquals("Welcome to our application", 
//             messageSource.getMessage("welcome.message", null, Locale.ENGLISH));
//         assertEquals("Login", 
//             messageSource.getMessage("login.title", null, Locale.ENGLISH));

//         // Test validation messages
//         assertEquals("Username is required", 
//             messageSource.getMessage("validation.username.required", null, Locale.ENGLISH));
//         assertEquals("Invalid email format", 
//             messageSource.getMessage("validation.email.invalid", null, Locale.ENGLISH));

//         // Test error messages
//         assertEquals("User not found", 
//             messageSource.getMessage("error.user.notfound", null, Locale.ENGLISH));
//         assertEquals("Email already exists", 
//             messageSource.getMessage("error.user.duplicate.email", null, Locale.ENGLISH));

//         // Test success messages
//         assertEquals("User created successfully", 
//             messageSource.getMessage("success.user.created", null, Locale.ENGLISH));
//     }

//     @Test
//     public void testArabicMessages() {
//         // Set locale to Arabic
//         LocaleContextHolder.setLocale(new Locale("ar"));

//         // Test general messages
//         assertEquals("مرحباً بك في تطبيقنا", 
//             messageSource.getMessage("welcome.message", null, new Locale("ar")));
//         assertEquals("تسجيل الدخول", 
//             messageSource.getMessage("login.title", null, new Locale("ar")));

//         // Test validation messages
//         assertEquals("اسم المستخدم مطلوب", 
//             messageSource.getMessage("validation.username.required", null, new Locale("ar")));
//         assertEquals("صيغة البريد الإلكتروني غير صحيحة", 
//             messageSource.getMessage("validation.email.invalid", null, new Locale("ar")));

//         // Test error messages
//         assertEquals("المستخدم غير موجود", 
//             messageSource.getMessage("error.user.notfound", null, new Locale("ar")));
//         assertEquals("البريد الإلكتروني موجود بالفعل", 
//             messageSource.getMessage("error.user.duplicate.email", null, new Locale("ar")));

//         // Test success messages
//         assertEquals("تم إنشاء المستخدم بنجاح", 
//             messageSource.getMessage("success.user.created", null, new Locale("ar")));
//     }

//     @Test
//     public void testMessageWithParameters() {
//         // Test message with parameters in English
//         assertEquals("User not found with username: testuser", 
//             messageSource.getMessage("error.user.notfound.username", new Object[]{"testuser"}, Locale.ENGLISH));

//         // Test message with parameters in Arabic
//         assertEquals("المستخدم غير موجود باسم المستخدم: testuser", 
//             messageSource.getMessage("error.user.notfound.username", new Object[]{"testuser"}, new Locale("ar")));
//     }

//     @Test
//     public void testMessageSourceConfiguration() {
//         assertNotNull(messageSource, "MessageSource should not be null");
//     }
// } 