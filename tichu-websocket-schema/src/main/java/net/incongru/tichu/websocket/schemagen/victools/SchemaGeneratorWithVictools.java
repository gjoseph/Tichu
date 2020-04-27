package net.incongru.tichu.websocket.schemagen.victools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.generator.TypeScope;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JsonSubTypesResolver;
import net.incongru.tichu.websocket.IncomingMessage;
import net.incongru.tichu.websocket.OutgoingMessage;
import net.incongru.tichu.websocket.codec.JacksonSetup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Jackson's own schema generator is unmaintained and doesn't seem to deal well with subtypes.
 * This alternative seems more active than mbknor-jackson-jsonSchema but also a lot less simple and doesn't generate
 * full type defs out of the box for our subtypes.
 * ... it's sort of getting there with some fiddling below, but still not happy:
 * - it's not reading @JsonProperty (see txId vs clientTxId)
 * - it's now generating a title "String" for simple string props, which is silly
 * But, it has a maven plugin, which could be handy.
 *
 * See shttps://victools.github.io/jsonschema-generator/
 */
class SchemaGeneratorWithVictools {
    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = JacksonSetup.setupJacksonMapper();
        final SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(mapper,
                SchemaVersion.DRAFT_6, // DRAFT_2020_12,
                OptionPreset.PLAIN_JSON)
                .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
                .with(new JacksonModule());

        configBuilder.forTypesInGeneral()
                .withTitleResolver(TypeScope::getSimpleTypeDescription)
                .withSubtypeResolver(new JsonSubTypesResolver());
        final SchemaGeneratorConfig config = configBuilder.build();

        final SchemaGenerator gen = new SchemaGenerator(config);
        writeSchema(mapper, gen, IncomingMessage.class, "Tichu - Incoming Messages", Path.of("victools-tichu-in-schema.json"));
        writeSchema(mapper, gen, OutgoingMessage.class, "Tichu - Outgoing Messages", Path.of("victools-tichu-out-schema.json"));
    }

    private static void writeSchema(ObjectMapper mapper, SchemaGenerator jsonSchemaGenerator, Class<?> clazz, String title, Path filePath) throws IOException {
        final JsonNode schema = jsonSchemaGenerator.generateSchema(clazz);
        // title, "JSON Schema for " + title);
        final String schemaStr = mapper.writer(SerializationFeature.INDENT_OUTPUT).writeValueAsString(schema);
        Files.writeString(filePath, schemaStr, StandardCharsets.UTF_8);
    }
}
