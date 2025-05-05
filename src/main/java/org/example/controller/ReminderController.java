package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PageResult;
import org.example.dto.ReminderDto;
import org.example.model.ApiResponse;
import org.example.services.ReminderNotificationService;
import org.example.services.ReminderServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
@Tag(name = "Reminders", description = "Reminder management APIs")
public class ReminderController {
    private final ReminderServices reminderService;
    private final ReminderNotificationService reminderNotificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new reminder",
        description = "Creates a new reminder for a plant",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Reminder created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<ReminderDto>> createReminder(@Valid @RequestBody ReminderDto dto) {
        ReminderDto result = reminderService.create(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(result, "Reminder created successfully"));
    }

    @GetMapping("/{reminderId}")
    @Operation(
        summary = "Get reminder by ID",
        description = "Returns the reminder with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ReminderDto>> getReminderById(
            @Parameter(description = "Reminder ID") 
            @PathVariable Integer reminderId) {
        ReminderDto reminder = reminderService.findById(reminderId);
        return ResponseEntity.ok(ApiResponse.success(reminder, "Reminder retrieved successfully"));
    }

    @PutMapping("/{reminderId}")
    @Operation(
        summary = "Update a reminder",
        description = "Updates the reminder with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ReminderDto>> updateReminder(
            @Parameter(description = "Reminder ID") 
            @PathVariable Integer reminderId,
            @Valid @RequestBody ReminderDto dto) {
        ReminderDto updated = reminderService.update(reminderId, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Reminder updated successfully"));
    }

    @DeleteMapping("/{reminderId}")
    @Operation(
        summary = "Delete a reminder",
        description = "Deletes the reminder with the specified ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteReminder(
            @Parameter(description = "Reminder ID") 
            @PathVariable Integer reminderId) {
        reminderService.delete(reminderId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.noContent("Reminder deleted successfully"));
    }

    @GetMapping("/plant/{plantId}")
    @Operation(
        summary = "Find reminder by plant ID",
        description = "Returns the reminder for the specified plant",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ReminderDto>> findReminderByPlant(
            @Parameter(description = "Plant ID") 
            @PathVariable String plantId) {
        ReminderDto reminder = reminderService.findByPlant(plantId);
        return ResponseEntity.ok(ApiResponse.success(reminder, "Reminder found"));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search reminders",
        description = "Search for reminders with optional filters and pagination",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<PageResult<ReminderDto>>> searchReminders(
            @Parameter(description = "Filter by plant") 
            @RequestParam(required = false) String plant,
            
            @Parameter(description = "Filter by reminder type") 
            @RequestParam(required = false) String reminderType,
            
            @Parameter(description = "Page number (0-based)") 
            @RequestParam(required = false,defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(required = false,defaultValue = "10") int size,
            
            @Parameter(description = "Sort by field") 
            @RequestParam(required = false,defaultValue = "id") String sortBy,
            
            @Parameter(description = "Sort direction") 
            @RequestParam(required = false,defaultValue = "ASC") Sort.Direction direction) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PageResult<ReminderDto> result = reminderService.search(plant, reminderType, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Search results"));
    }

    @PostMapping("/{reminderId}/send-notification")
    @Operation(
        summary = "Send reminder notification",
        description = "Sends a notification for a specific reminder",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<String>> sendReminderNotification(
            @Parameter(description = "Reminder ID") 
            @PathVariable Integer reminderId) {
        // Find the reminder
        ReminderDto reminder = reminderService.findById(reminderId);
        
        // Send notification using the reminder details
        reminderNotificationService.sendTestReminderNotification(
                reminder.getUserId().intValue(),
                reminder.getPlantId(),
                reminder.getReminderType()
        );
        
        return ResponseEntity.ok(ApiResponse.success(
                "Notification sent for reminder ID: " + reminderId,
                "Reminder notification sent successfully"));
    }

    @PostMapping("/send-due-reminders")
    @Operation(
        summary = "Send all due reminders",
        description = "Checks and sends notifications for all reminders due today",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> sendDueReminders() {
        reminderNotificationService.sendDueReminders();
        return ResponseEntity.ok(ApiResponse.success(
                "Due reminders processed",
                "All due reminders have been processed and notifications sent"));
    }
}
