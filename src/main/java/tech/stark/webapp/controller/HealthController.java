package tech.stark.webapp.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.OnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthz")
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> greeting(HttpServletRequest request, @RequestBody @Nullable String body) {
        if(request.getQueryString() != null || body != null){
            return ResponseEntity.badRequest().cacheControl(CacheControl.noCache()).build();
        }
        try{
            jdbcTemplate.execute("SELECT 1");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).build();
    }
}