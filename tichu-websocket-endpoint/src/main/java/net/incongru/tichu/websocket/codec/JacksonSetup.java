package net.incongru.tichu.websocket.codec;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.incongru.tichu.action.ActionParam;
import net.incongru.tichu.action.param.CheatDealParam;
import net.incongru.tichu.action.param.ImmutableInitialiseGameParam;
import net.incongru.tichu.action.param.ImmutableJoinTableParam;
import net.incongru.tichu.action.param.ImmutableNewTrickParam;
import net.incongru.tichu.action.param.ImmutablePlayerIsReadyParam;
import net.incongru.tichu.action.param.ImmutablePlayerPlaysParam;
import net.incongru.tichu.model.Card;
import net.incongru.tichu.model.util.DeckConstants;

class JacksonSetup {

    static ObjectMapper setupJacksonMapper() {
        final SimpleModule m = new SimpleModule();

        // Card support
        m.setMixInAnnotation(Card.class, CardJacksonSupport.class);

        // ActionParam support -- the mixin here is useless since we have @JsonTypeInfo on ActionParam
        // The fact that the annotation was needed to drive immutable to generate the right plumbing
        // may explain why we didn't figure out how to configure type info just via code
        // m.setMixInAnnotation(ActionParam.class, ActionParamJacksonMixin.class);

        // @JsonSubTypes equivalent - we could also do that on ActionParamJacksonMixin
        m.registerSubtypes(new NamedType(ImmutableInitialiseGameParam.class, "init"));
        m.registerSubtypes(new NamedType(ImmutableJoinTableParam.class, "join"));
        m.registerSubtypes(new NamedType(ImmutableNewTrickParam.class, "newTrick")); // TODO should not need this one?
        m.registerSubtypes(new NamedType(ImmutablePlayerIsReadyParam.class, "isReady"));
        m.registerSubtypes(new NamedType(ImmutablePlayerPlaysParam.class, "play"));

        // Not sure how this is useful
        final PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(ActionParam.class)
                .denyForExactBaseType(CheatDealParam.class)
                .build();

        return JsonMapper.builder()
                .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // ?? maybe ignore unknown?
                .enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .addModule(new Jdk8Module())
                .addModule(m)
                // .polymorphicTypeValidator(ptv)
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

    static class CardDeserializer extends FromStringDeserializer<Card> {

        public CardDeserializer() {
            super(Card.class);
        }

        @Override
        protected Card _deserialize(String value, DeserializationContext ctxt) {
            return DeckConstants.byName(value);
        }
    }

}
