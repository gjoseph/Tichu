package net.incongru.tichu.simu;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulationFileLoaderTest {
    @Test
    public void loadFileFiltersOutCommentsAndBlankLines() throws IOException {
        List<String> strings = SimulationFileLoader.fromResource("/sample-file.txt");
        assertThat(strings).containsExactly("line1", "line2", "line3 has multiple words and trailing space");
    }
}
