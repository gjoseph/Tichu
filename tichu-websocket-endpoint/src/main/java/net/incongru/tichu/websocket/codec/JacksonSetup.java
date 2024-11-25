package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.incongru.tichu.action.Action;
import net.incongru.tichu.action.ActionResponse;
import net.incongru.tichu.action.param.ImmutableJoinTableParam;
import net.incongru.tichu.action.param.ImmutableNewTrickParam;
import net.incongru.tichu.action.param.ImmutablePlayerIsReadyParam;
import net.incongru.tichu.action.param.ImmutablePlayerPlaysParam;
import net.incongru.tichu.action.param.InitialiseGameParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.Play;
import net.incongru.tichu.model.Player;
import net.incongru.tichu.model.UserId;
import net.incongru.tichu.model.util.DeckConstants;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Set;

class JacksonSetup {

    static ObjectMapper setupJacksonMapper() {
        final SimpleModule m = new SimpleModule();

        // Mixins to support custom serialisation of our non-javabean classes
        m.setMixInAnnotation(Card.class, CardJacksonSupport.class);
        m.setMixInAnnotation(Player.Hand.class, HandJacksonSupport.class);
        m.setMixInAnnotation(UserId.class, UserIdJacksonSupport.class);
        m.setMixInAnnotation(ActionResponse.class, ActionResultJacksonSupport.class);
        m.setMixInAnnotation(ActionResponse.Message.class, MessageJacksonSupport.class);

        // ActionParam support -- the mixin here is useless since we have @JsonTypeInfo on ActionParam
        // The fact that the annotation was needed to drive immutable to generate the right plumbing
        // may explain why we didn't figure out how to configure type info just via code
        // m.setMixInAnnotation(ActionParam.class, ActionParamJacksonMixin.class);

        // @JsonSubTypes equivalent - we could also do that on ActionParamJacksonMixin
        m.registerSubtypes(new NamedType(InitialiseGameParam.class, kebab(Action.ActionType.INIT)));
        m.registerSubtypes(new NamedType(ImmutableJoinTableParam.class, kebab(Action.ActionType.JOIN)));
        m.registerSubtypes(new NamedType(ImmutableNewTrickParam.class, kebab(Action.ActionType.NEW_TRICK))); // TODO should not need this one?
        m.registerSubtypes(new NamedType(ImmutablePlayerIsReadyParam.class, kebab(Action.ActionType.READY)));
        m.registerSubtypes(new NamedType(ImmutablePlayerPlaysParam.class, kebab(Action.ActionType.PLAY)));

        // All enums should be serialised in kebab-case
        // https://github.com/FasterXML/jackson-databind/issues/2667 would probably offer a more performant version of this with caching?
        m.addSerializer(Enum.class, new EnumKebabSerializer());

        return JsonMapper.builder()
                .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)
                //TODO should also fail seralisation on unknown subtypes: https://github.com/FasterXML/jackson-databind/issues/436
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // ?? maybe ignore unknown?
                .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .enable(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES)
                .serializationInclusion(JsonInclude.Include.NON_ABSENT)
                .addModule(new Jdk8Module())
                .addModule(m)
                .build();
    }

    @JsonDeserialize(using = CardDeserializer.class)
    abstract static class CardJacksonSupport {

//    @JsonCreator -- does not work on mixins (https://github.com/FasterXML/jackson-databind/issues/1820)
//    so we use a Serializer instead
//    static Card byName(String cardName) {
//        return DeckConstants.byName(cardName);
//    }

        @JsonValue
        abstract String shortName();
    }

    abstract static class HandJacksonSupport {
        @JsonProperty
        Set<Card> cards;
    }

    abstract static class UserIdJacksonSupport {
        @JsonValue
        String id;
    }

    static interface ActionResultJacksonSupport<R extends ActionResponse.Result> {

        @JsonGetter
        UserId actor();

        @JsonGetter
        Action.ActionType forAction();

        @JsonGetter
        R result();

        @JsonGetter
        net.incongru.tichu.action.ActionResponse.Message message();

        // PlayerPlaysResponse properties:
        @JsonGetter
        Play play();

        @JsonGetter
        UserId nextPlayer();
    }

    abstract static class MessageJacksonSupport {
        @JsonValue
        String message;
    }

    static class CardDeserializer extends FromStringDeserializer<Card> {

        public CardDeserializer() {
            super(Card.class);
        }

        @Override
        protected Card _deserialize(String value, DeserializationContext ctxt) {
            return DeckConstants.byName(value);
        }
    }

    private static class EnumKebabSerializer extends StdSerializer<Enum> {
        public EnumKebabSerializer() {
            super(Enum.class);
        }

        @Override
        public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            final String kebab = kebab(value);
            jgen.writeString(kebab);
        }
    }

    protected static String kebab(@Nonnull Enum<?> value) {
        return value.name().toLowerCase().replace('_', '-');
    }
}
