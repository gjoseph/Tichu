package net.incongru.tichu.websocket.schemagen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.incongru.tichu.websocket.IncomingMessage;
import net.incongru.tichu.websocket.OutgoingMessage;
import net.incongru.tichu.websocket.codec.JacksonSetup;

/**
 * Jackson's own schema generator is unmaintained and doesn't seem to deal well with subtypes, so we're using this third party.
 * See https://github.com/FasterXML/jackson-module-jsonSchema
 * See https://github.com/mbknor/mbknor-jackson-jsonSchema
 */
class SchemaGenerator {

    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = JacksonSetup.setupJacksonMapper();

        final JsonSchemaConfig config =
            JsonSchemaConfig.vanillaJsonSchemaDraft4();
        final JsonSchemaGenerator gen = new JsonSchemaGenerator(
            mapper,
            false,
            config
        );
        writeSchema(
            mapper,
            gen,
            IncomingMessage.class,
            "Tichu - Incoming Messages",
            Path.of("tichu-in-schema.json")
        );
        writeSchema(
            mapper,
            gen,
            OutgoingMessage.class,
            "Tichu - Outgoing Messages",
            Path.of("tichu-out-schema.json")
        );
    }

    private static void writeSchema(
        ObjectMapper mapper,
        JsonSchemaGenerator jsonSchemaGenerator,
        Class<?> clazz,
        String title,
        Path filePath
    ) throws IOException {
        final JsonNode schema = jsonSchemaGenerator.generateJsonSchema(
            clazz,
            title,
            "JSON Schema for " + title
        );
        final String schemaStr = mapper
            .writer(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsString(schema);
        Files.writeString(filePath, schemaStr, StandardCharsets.UTF_8);
    }
}
