package tech.stark.webapp.controller.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleNoHandlerFoundException(
            BadRequestException ex, HttpServletRequest httpServletRequest) {
        String message;
        if(!"".equals(ex.getMessage()) || ex.getMessage() == null){

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonNode = objectMapper.createObjectNode();
            jsonNode.put("error", ex.getMessage());
            message = jsonNode.toPrettyString();
        } else {
            message = "";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
    }
}
