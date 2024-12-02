package net.incongru.tichu.simu.parse;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class SimulationFileLoader {

    static List<String> from(Path p) throws IOException {
        return Files.lines(p)
            .map(s -> s.replaceFirst("#.*", "")) // Strip comments
            .map(String::strip) // Trim whitespaces left and right
            .filter(not(String::isEmpty)) // Remove empty lines
            .collect(Collectors.toList());
    }
}
