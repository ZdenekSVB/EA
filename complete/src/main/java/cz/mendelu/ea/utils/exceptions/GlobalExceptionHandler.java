package cz.mendelu.ea.utils.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Vrací 409 Conflict při duplicite nebo jiné porušení constraintu
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Conflict: " + (ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage()));
    }

    // Další handlery, pokud chceš (např. NotFoundException -> 404)
}
