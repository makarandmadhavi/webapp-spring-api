package tech.stark.webapp.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceUnavailableExceptionHandler {

    @ExceptionHandler({ServiceUnavailableException.class})
    public ResponseEntity<?> handleNoHandlerFoundException(
            ServiceUnavailableException ex, HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON).body("");
    }
}
