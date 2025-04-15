package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.document.Users;
import org.example.dto.Credentials;
import org.example.dto.PageResult;
import org.example.dto.UsersDto;
import org.example.dto.UsersDto;
import org.example.services.UsersServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersServices usersServices;
//    @Autowired
//    UsersServices services;

    @PostMapping("/user")
    public ResponseEntity<UsersDto>  create(@RequestBody UsersDto dto){
        UsersDto result=usersServices.create(dto);
        return new  ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void>  delete(@PathVariable Long userId ){
        usersServices.delete(userId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UsersDto>  update (@PathVariable Long userId , @RequestBody UsersDto dto){
        UsersDto update= usersServices.update(userId,dto);
        return ResponseEntity.ok(update);
    }
    @GetMapping("/users/{userName}")
    public ResponseEntity<UsersDto> findByName(@PathVariable String userName ){
        UsersDto userDto =usersServices.findByName(userName);
        return  ResponseEntity.ok(userDto);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UsersDto> findByEmail(@PathVariable String email){
        UsersDto userDto = usersServices.findByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/search")
    public PageResult<UsersDto> search(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC")Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResult<UsersDto> result = usersServices.search(userName, email, phoneNumber, pageable);
        return result;
    }
//    @PostMapping("/login")
//    public String login(@RequestBody Credentials credentials){
//        return usersServices.login(credentials);
//    }

}
