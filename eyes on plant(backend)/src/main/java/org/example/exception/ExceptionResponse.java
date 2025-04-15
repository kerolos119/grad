package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private String messsage;
    private HttpStatus status;
    private int code;
    private LocalDateTime time;


}
