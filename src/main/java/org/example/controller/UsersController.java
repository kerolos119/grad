package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.Credentials;
import org.example.dto.UsersDto;
import org.example.model.ApiResponse;
import org.example.model.AuthResponse;
import org.example.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UsersController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "User created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid input or email/username already exists")
    })
    public ResponseEntity<ApiResponse<UsersDto>> registerUser(@Valid @RequestBody UsersDto dto) {
        UsersDto result = userService.create(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(result, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates user credentials and returns a token"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Authentication successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody Credentials request) {
        AuthResponse response = userService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Authentication successful"));
    }

    @GetMapping
    @Operation(
        summary = "Search users",
        description = "Search for users with optional filters and pagination",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Page<UsersDto>>> searchUsers(
            @Parameter(description = "Filter by username") 
            @RequestParam(required = false) String username,
            
            @Parameter(description = "Filter by email") 
            @RequestParam(required = false) String email,
            
            @Parameter(description = "Filter by phone number") 
            @RequestParam(required = false) String phoneNumber,
            
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size,
            
            @Parameter(description = "Sort properties in format: property,direction") 
            @RequestParam(required = false,defaultValue = "userId,desc") String[] sort) {
        
        Sort sorting = Sort.by(parseSortOrder(sort));
        PageRequest pageable = PageRequest.of(page, size, sorting);
        Page<UsersDto> result = userService.search(username, email, phoneNumber, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Search results"));
    }

    @GetMapping("/username/{username}")
    @Operation(
        summary = "Get user by username",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UsersDto>> findByUsername(
            @Parameter(description = "Username to search for") 
            @PathVariable String username) {
        UsersDto userDto = userService.findByName(username);
        return ResponseEntity.ok(ApiResponse.success(userDto, "User found"));
    }

    @GetMapping("/email/{email}")
    @Operation(
        summary = "Get user by email",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UsersDto>> findByEmail(
            @Parameter(description = "Email to search for") 
            @PathVariable String email) {
        UsersDto userDto = userService.findByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(userDto, "User found"));
    }

    @GetMapping("/{userId}")
    @Operation(
        summary = "Get user by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UsersDto>> getUserById(
            @Parameter(description = "User ID") 
            @PathVariable Long userId) {
        UsersDto userDto = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(userDto, "User found"));
    }

    @PutMapping("/{userId}")
    @Operation(
        summary = "Update user details",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<UsersDto>> updateUser(
            @Parameter(description = "User ID to update") 
            @PathVariable Long userId,
            @Valid @RequestBody UsersDto dto) {
        UsersDto updated = userService.update(userId, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }

    @DeleteMapping("/{userId}")
    @Operation(
        summary = "Delete user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID to delete") 
            @PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.noContent("User deleted successfully"));
    }

    private Sort.Order[] parseSortOrder(String[] sort) {
        return Arrays.stream(sort)
                .map(s -> s.split(","))
                .map(parts -> {
                    String property = parts[0];
                    Sort.Direction direction = parts.length > 1 
                        ? Sort.Direction.fromString(parts[1]) 
                        : Sort.Direction.ASC;
                    return new Sort.Order(direction, property);
                })
                .toArray(Sort.Order[]::new);
    }
}
