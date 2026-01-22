package com.securefile.exception;

import com.securefile.dto.response.ApiResponse;
import com.securefile.dto.response.FileDownloadResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<FileDownloadResponse>> hadnleFileNotFoundException(EntityNotFoundException e){
        ApiResponse<FileDownloadResponse> apiResponse = new ApiResponse<>(
                HttpStatus.NOT_FOUND.toString(),
                404,
                e.getMessage(),
                true,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(FileExpiredException.class)
    public ResponseEntity<ApiResponse<FileDownloadResponse>> handleFileExpiredException(FileExpiredException e){
        ApiResponse<FileDownloadResponse> apiResponse = new ApiResponse<>(
                HttpStatus.GONE.toString(),
                410,
                e.getMessage(),
                true,
                null
        );
        return ResponseEntity.status(HttpStatus.GONE).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<FileDownloadResponse>> handleIllegalArgumentException(IllegalArgumentException e){
        ApiResponse<FileDownloadResponse> apiResponse = new ApiResponse<>(
                HttpStatus.PAYLOAD_TOO_LARGE.toString(),
                413,
                e.getMessage(),
                true,
                null
        );
        return ResponseEntity.status(HttpStatus.GONE).body(apiResponse);
    }
}
