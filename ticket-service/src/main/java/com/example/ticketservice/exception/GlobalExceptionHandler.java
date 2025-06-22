//package com.example.ticketservice.exception;
//
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Handle all unhandled exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
//        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ex.getMessage());
//    }
//
//    // Handle entity not found (e.g. findById().orElseThrow)
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<Object> handleNotFound(NoSuchElementException ex) {
//        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
//    }
//
//    // Handle bad request: invalid input, path variable, etc.
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
//        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage());
//    }
//
//    // Handle validation errors from @Valid (if added later)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        return ResponseEntity.badRequest().body(errors);
//    }
//
//    @ExceptionHandler(TicketNotFoundException.class)
//    public ResponseEntity<Object> handleCustomException(TicketNotFoundException ex) {
//        return buildErrorResponse(HttpStatus.NOT_FOUND, "Event Not Found", ex.getMessage());
//    }
//    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, String message) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", status.value());
//        body.put("error", error);
//        body.put("message", message);
//        return new ResponseEntity<>(body, status);
//    }
//}
