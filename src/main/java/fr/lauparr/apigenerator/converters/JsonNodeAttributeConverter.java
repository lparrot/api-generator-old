package fr.lauparr.apigenerator.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Component
@Converter(autoApply = true)
public class JsonNodeAttributeConverter implements AttributeConverter<JsonNode, String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(JsonNode entityValue) {
        if (entityValue == null)
            return null;

        return entityValue.toString();
    }

    @SneakyThrows
    @Override
    public JsonNode convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null)
            return null;

        return objectMapper.readValue(databaseValue, JsonNode.class);

    }
}
