package rba.it.CardApp.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import rba.it.CardApp.dto.ErrorResponseDTO;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptions.BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(CustomExceptions.BadRequestException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("400", UUID.randomUUID().toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(CustomExceptions.UnauthorizedException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("401", UUID.randomUUID().toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomExceptions.NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(CustomExceptions.NotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("404", UUID.randomUUID().toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomExceptions.ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(CustomExceptions.ConflictException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("409", UUID.randomUUID().toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomExceptions.InternalServerErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleInternalServerErrorException(CustomExceptions.InternalServerErrorException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("500", UUID.randomUUID().toString(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Dodani novi handleri
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("403", UUID.randomUUID().toString(), "Access denied.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("401", UUID.randomUUID().toString(), "Authentication failed.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("500", UUID.randomUUID().toString(), "An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
