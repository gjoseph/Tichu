package net.incongru.tichu.simu.util;

import static net.incongru.tichu.simu.util.NameableEnum.allNamesOf;
import static net.incongru.tichu.simu.util.NameableEnum.byName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class NameableEnumTest {

    @Test
    void byNameSupportsIsCaseInsensitive() {
        assertEquals(SampleEnum.a, byName(SampleEnum.class, "a"));
        assertEquals(SampleEnum.a, byName(SampleEnum.class, "A"));
        assertEquals(SampleEnum.BAR, byName(SampleEnum.class, "bar"));
        assertEquals(SampleEnum.BAR, byName(SampleEnum.class, "BaR"));
        assertEquals(SampleEnum.BAR, byName(SampleEnum.class, "BAR"));
    }

    @Test
    void byNameSupportsAlternativeNamesCaseInsensitively() {
        assertEquals(SampleEnum.foo, byName(SampleEnum.class, "fOo"));
        assertEquals(SampleEnum.foo, byName(SampleEnum.class, "fU"));
        assertEquals(SampleEnum.BAR, byName(SampleEnum.class, "Tavern"));
        assertEquals(SampleEnum.BAR, byName(SampleEnum.class, "PUB"));
    }

    @Test
    void allNamesLowercasesAllNames() {
        assertThat(
            allNamesOf(SampleEnum.class).split("\\s*,\\s*")
        ).containsExactlyInAnyOrder(
            "a",
            "b",
            "c",
            "foo",
            "fu",
            "bar",
            "tavern",
            "pub"
        );
    }

    enum SampleEnum implements NameableEnum {
        a,
        b,
        c,
        foo("Fu"),
        BAR("tavern", "Pub");

        private final List<String> altNames;

        SampleEnum(String... altNames) {
            this.altNames = Arrays.asList(altNames);
        }

        @Override
        public List<String> altNames() {
            return altNames;
        }
    }
}
