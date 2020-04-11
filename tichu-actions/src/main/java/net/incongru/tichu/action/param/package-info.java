@ImmutableActionParam
package net.incongru.tichu.action.param;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Meta annotation to generate Immutable implementations of {@link net.incongru.tichu.action.ActionParam}s.
 * We specify Jackson annotations here to tell Immutables to add Jackson annotations on generated classes.
 * Implementations should be whitelisted at usage point (e.g see net.incongru.tichu.websocket.codec.JacksonSetup)
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Value.Style(jdkOnly = true)
@JsonSerialize
@JsonDeserialize
@interface ImmutableActionParam {
}
