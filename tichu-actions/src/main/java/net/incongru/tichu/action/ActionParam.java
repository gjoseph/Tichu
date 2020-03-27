package net.incongru.tichu.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// We need this here as well, so that immutables generates @JsonTypeInfo(use=JsonTypeInfo.Id.NONE)
// on implementations' $Json inner classes, without with the polymorphic deser just falls apart
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        // our json shapes will have a "type" with values as specified below
        property = "type"
)
// Subtypes declaration is done at usage point, to minimise distance between allowed params (e.g we don't want to expose CheatDealParam to the web client)
public interface ActionParam {
}
