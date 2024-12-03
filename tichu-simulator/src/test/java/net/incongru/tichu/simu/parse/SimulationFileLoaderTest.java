package net.incongru.tichu.simu.parse;

import static net.incongru.tichu.simu.PathUtil.resource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class SimulationFileLoaderTest {

    @Test
    void loadFileFiltersOutCommentsAndBlankLines() throws IOException {
        List<String> strings = SimulationFileLoader.from(
            resource("/sample-file.txt")
        );
        assertThat(strings).containsExactly(
            "line1",
            "line2",
            "line3 has multiple words and trailing space",
            "line4"
        );
    }
}
