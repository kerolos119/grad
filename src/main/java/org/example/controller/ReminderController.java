package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PageResult;
import org.example.dto.ReminderDto;
import org.example.services.ReminderServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderServices services;

    @PostMapping("/reminder")
    public ResponseEntity<ReminderDto> create (@RequestBody ReminderDto dto){
        ReminderDto result= services.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<Void> delete(@PathVariable Integer reminderId){
        services.delete(reminderId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{reminderId}")
    public ResponseEntity<ReminderDto> update(@PathVariable Integer reminderId, @RequestBody ReminderDto dto){
        ReminderDto result= services.update(reminderId,dto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/plant/{plant}")
    public ResponseEntity<ReminderDto> findByPlant(@PathVariable String plant){
        ReminderDto reminderDto=services.findByPlant(plant);
        return ResponseEntity.ok(reminderDto);
    }
    @GetMapping("/search")
    public PageResult<ReminderDto> search(
            @RequestParam(required = false) String plant,
            @RequestParam(required = false) String reminderType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC")Sort.Direction direction){
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,sortBy));
        PageResult<ReminderDto> result = services.search(plant,reminderType,pageable);
        return result;
    }
}
