package com.shiproutepro.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class APIErrorResponse implements Serializable {

    private HttpStatus status;
    private Integer error;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private APIErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    APIErrorResponse(HttpStatus status, String message, Integer error) {
        this();
        this.status = status;
        this.message = message;
        this.error = error;
    }
}