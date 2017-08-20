package net.incongru.tichu.model.immu;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Style(
        // Generate construction method using all attributes as parameters
        allParameters = true,
        // Changing generated name just for fun
        // typeImmutable = "*Tuple",
        // Disable builder
        defaults = @Immutable(builder = false),
        // Rename factory method
        of = "of"
)
public @interface Tuple {
}
