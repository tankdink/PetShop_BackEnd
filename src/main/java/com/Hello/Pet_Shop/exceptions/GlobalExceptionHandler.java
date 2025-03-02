package com.Hello.Pet_Shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(value = CustomValidJwtException.class)
    ResponseEntity<String> handlingExpiredJwtException(CustomValidJwtException exception)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<String> handlingResourceNotFoundException(ResourceNotFoundException exception)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = DuplicateEntryException.class)
    ResponseEntity<String> handlingDuplicateEntryException(DuplicateEntryException exception)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(value = CustomBadCredentialsException.class)
    ResponseEntity<String> handlingCustomBadCredentialsException(CustomBadCredentialsException exception)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        // Return the first validation error message if available
        if (!errors.isEmpty()) 
        {
            String errorMessage = errors.get(0).getDefaultMessage(); // Get the first error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        // Default error message if no field errors
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error occurred");
    }

    @ExceptionHandler(value = PermissionException.class)
    ResponseEntity<String> handlingPermissionException(PermissionException exception)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have enough permission for this action!");
    }

    @ExceptionHandler(value = BadInputException.class)
    ResponseEntity<String> handlingBadInputException(BadInputException exception)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No access allowed!");
    }


}
