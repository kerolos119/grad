package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "users",description = "Endpoints for managing farmers and agricultural users")
public class UsersController {

    private final UserService usersServices;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create new user",security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<UsersDto> create(@RequestBody UsersDto dto){
        UsersDto result=usersServices.create(dto);
        return  ApiResponse.created(result);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "delete user",security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN') or (#userId == principal.id)")
    public void delete(@PathVariable Long userId) {
        usersServices.delete(userId);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "update user",security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ApiResponse<UsersDto>  update (
            @PathVariable Long userId ,
            @Valid @RequestBody UsersDto dto){
        UsersDto update= usersServices.update(userId,dto);
        return ApiResponse.success(update,"User updated successfully");
    }
    @GetMapping("/username/{username}")
    @Operation(summary = "search by username")
    public ApiResponse<UsersDto> findByName(@PathVariable String username ){
        UsersDto userDto =usersServices.findByName(username);
        return  ApiResponse.success(userDto,"user found");
    }
    @GetMapping("/email/{email}")
    @Operation(summary = "Search by email")
    public ApiResponse<UsersDto> findByEmail(@PathVariable String email){
        UsersDto userDto = usersServices.findByEmail(email);
        return ApiResponse.success(userDto,"user found");
    }

    private Sort.Order[] parsseSortOrder (String [] sort){
        return Arrays.stream(sort)
                .map(s->s.split(","))
                .map(parts->new Sort.Order(
                        Sort.Direction.fromString(parts[1]),
                        parts[0]))
                .toArray(Sort.Order[]::new);
    }
    @GetMapping("/search")
    @Operation(summary = "Advanced User Search")
    public ApiResponse<Page<UsersDto>> search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ceatedAt,desc")String[] sort){
        Sort sorting = Sort.by(parsseSortOrder(sort));
        PageRequest pageable = PageRequest.of(page,size,sorting);

        Page<UsersDto> result = usersServices.search(username,email,phoneNumber,pageable);
        return ApiResponse.success(result,"Search results");
    }
    @PostMapping("/login")
    @Operation(summary = "login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody Credentials request){
        AuthResponse response = usersServices.authenticate(request);
        return ApiResponse.success(response,"Authentication Successful");
    }


//    @PostMapping("/reset-password")
//    @Operation(summary = "Initiate password reset")
//    public ApiResponse<ResetPasswordRequest> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
//        usersServices.resetPassword(request);
//        return ApiResponse.success(request,"Password reset");
//    }
//    @GetMapping("/verify-email")
//    @Operation(summary = "Verify email address")
//    public ApiResponse<String> verifyEmail(@RequestParam String token){
//        usersServices.verifyEmail(token);
//        return ApiResponse.success(token,"Password resetPassword reset");
//    }
}
