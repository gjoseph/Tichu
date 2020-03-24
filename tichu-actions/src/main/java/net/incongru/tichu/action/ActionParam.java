package net.incongru.tichu.action;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.incongru.tichu.action.param.ImmutableCheatDealParam;
import net.incongru.tichu.action.param.ImmutableInitialiseGameParam;
import net.incongru.tichu.action.param.ImmutableJoinTableParam;
import net.incongru.tichu.action.param.ImmutableNewTrickParam;
import net.incongru.tichu.action.param.ImmutablePlayerIsReadyParam;
import net.incongru.tichu.action.param.ImmutablePlayerPlaysParam;

// TODO Can we move this to a mixin ?
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        // our json shapes will have a "type" with values as specified below
        property = "type"
)
@JsonSubTypes({
        // We specify the type here using the Immutable* impls so serialising uses the correct name
        // but we still need to also specify @JsonDeserialize(as) on the abstract types for deserialisation
        @JsonSubTypes.Type(value = ImmutableCheatDealParam.class, name = "cheatDeal"), // TODO should not need this from json
        @JsonSubTypes.Type(value = ImmutableInitialiseGameParam.class, name = "init"),
        @JsonSubTypes.Type(value = ImmutableJoinTableParam.class, name = "join"),
        @JsonSubTypes.Type(value = ImmutableNewTrickParam.class, name = "newTrick"), // TODO should not need this one?
        @JsonSubTypes.Type(value = ImmutablePlayerIsReadyParam.class, name = "isReady"),
        @JsonSubTypes.Type(value = ImmutablePlayerPlaysParam.class, name = "play")
})
public interface ActionParam {
}
