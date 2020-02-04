package net.incongru.tichu.simu.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class SimulationFileLoader {

    static List<String> from(Path p) throws IOException {
        return Files.lines(p)
                .map(String::strip)
                .filter(SimulationFileLoader::filter)
                .collect(Collectors.toList());
    }

    private static boolean filter(String s) {
        return !s.isEmpty() && !s.startsWith("#");
    }
}
