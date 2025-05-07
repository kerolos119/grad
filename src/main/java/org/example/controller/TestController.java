// package org.example.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.MessageSource;
// import org.springframework.context.i18n.LocaleContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.HashMap;
// import java.util.Locale;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/test")
// public class TestController {

//     @Autowired
//     private MessageSource messageSource;

//     @GetMapping("/messages")
//     public Map<String, String> getMessages() {
//         Locale currentLocale = LocaleContextHolder.getLocale();
//         Map<String, String> messages = new HashMap<>();

//         // General messages
//         messages.put("welcome", messageSource.getMessage("welcome.message", null, currentLocale));
//         messages.put("login", messageSource.getMessage("login.title", null, currentLocale));

//         // Validation messages
//         messages.put("username.required", messageSource.getMessage("validation.username.required", null, currentLocale));
//         messages.put("email.invalid", messageSource.getMessage("validation.email.invalid", null, currentLocale));

//         // Error messages
//         messages.put("user.notfound", messageSource.getMessage("error.user.notfound", null, currentLocale));
//         messages.put("user.duplicate.email", messageSource.getMessage("error.user.duplicate.email", null, currentLocale));

//         // Success messages
//         messages.put("user.created", messageSource.getMessage("success.user.created", null, currentLocale));

//         return messages;
//     }

//     @GetMapping("/messages/with-params")
//     public Map<String, String> getMessagesWithParams() {
//         Locale currentLocale = LocaleContextHolder.getLocale();
//         Map<String, String> messages = new HashMap<>();

//         // Test messages with parameters
//         messages.put("user.notfound.username", 
//             messageSource.getMessage("error.user.notfound.username", new Object[]{"testuser"}, currentLocale));
//         messages.put("user.notfound.email", 
//             messageSource.getMessage("error.user.notfound.email", new Object[]{"test@example.com"}, currentLocale));

//         return messages;
//     }
// } 