package org.web.analytics.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    ResponseEntity<ApiError> handleConnection(CannotGetJdbcConnectionException ex, HttpServletRequest request) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "Cannot connect to Spark SQL through Hive JDBC", request);
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ApiError> handleDataAccess(DataAccessException ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_GATEWAY, "Spark SQL query failed. Check that all OLAP scripts were executed.", request);
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        ));
    }
}
