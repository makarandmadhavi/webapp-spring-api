package tech.stark.webapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.stark.webapp.WebappApplication;
import tech.stark.webapp.controller.error.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
@Slf4j
public class ValidationService {
    public JsonNode validateJson(String json, String schemaPath) {
        JsonNode jsonNode;
        try (InputStream schemaAsStream = WebappApplication.class.getClassLoader().getResourceAsStream(schemaPath)) {
            JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);

            ObjectMapper om = new ObjectMapper();
            jsonNode = om.readTree(json);

            Set<ValidationMessage> errors = schema.validate(jsonNode);
            StringBuilder errorsCombined = new StringBuilder();
            for (ValidationMessage error : errors) {
                log.error("Validation Error: {}", error);
                errorsCombined.append(error.toString()).append("\n");
            }
            if (errors.size() > 0)
                throw new BadRequestException("Improper json format: \n" + errorsCombined);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Failed to parse the body into json");
        } catch (IOException e) {
            throw new JsonSchemaException("Error occurred while fetching the schema from resource");
        }
        return jsonNode;
    }
}

