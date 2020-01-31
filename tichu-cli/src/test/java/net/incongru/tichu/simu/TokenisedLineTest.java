package net.incongru.tichu.simu;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenisedLineTest {
    @Test
    public void peekPopAndRemainder() {
        TokenisedLine t = new TokenisedLine("foo bar qux zoom");
        assertEquals("bar", t.peek(1));
        assertEquals("qux", t.peek(2));
        assertEquals("bar", t.pop(1));
        assertEquals("qux", t.pop(1));
        assertEquals("foo", t.peek(0));
        assertEquals("zoom", t.peek(1));
        assertEquals("foo zoom", t.remainder());
    }

    @Test
    public void popAndReset() {
        TokenisedLine t = new TokenisedLine("foo bar qux zoom");
        assertEquals("bar", t.pop(1));
        assertEquals("foo bar qux zoom", t.whole());
        assertEquals("foo qux zoom", t.remainder());
        assertEquals("foo bar qux zoom", t.reset().remainder());
    }

    @Test
    public void testMatchesStringToken() {
        TokenisedLine t = new TokenisedLine("foo bar qux zoom");
        assertThat(t.test(1, "not-bar")).isFalse();
        assertEquals("foo bar qux zoom", t.remainder());
        assertThat(t.test(1, "bar")).isTrue();
        assertEquals("foo qux zoom", t.remainder());
    }
}
