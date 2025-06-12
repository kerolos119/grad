package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DeviceTokenDto;
import org.example.model.ApiResponse;
import org.example.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/devices")
@RequiredArgsConstructor
@Tag(name = "User Devices", description = "User device management APIs")
public class UserDeviceController {

    private final UserRepository userRepository;
    
    @PostMapping("/token")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(
        summary = "Register device token",
        description = "Registers or updates a user's device token for push notifications",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> registerDeviceToken(
            @Parameter(description = "Device token information") 
            @Valid @RequestBody DeviceTokenDto deviceTokenDto) {
        
        userRepository.findById(deviceTokenDto.getUserId())
                .ifPresent(user -> {
                    user.setDeviceToken(deviceTokenDto.getToken());
                    userRepository.save(user);
                });
        
        return ResponseEntity.ok(ApiResponse.success(
                "Device token registered",
                "Device token successfully registered for user"));
    }
    
    @DeleteMapping("/token/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(
        summary = "Delete device token",
        description = "Removes a user's device token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> deleteDeviceToken(
            @Parameter(description = "User ID") 
            @PathVariable Long userId) {
        
        userRepository.findById(userId)
                .ifPresent(user -> {
                    user.setDeviceToken(null);
                    userRepository.save(user);
                });
        
        return ResponseEntity.ok(ApiResponse.success(
                "Device token removed",
                "Device token successfully removed for user"));
    }
} 