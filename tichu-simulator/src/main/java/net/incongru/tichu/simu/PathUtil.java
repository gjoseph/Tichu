package net.incongru.tichu.simu;

import java.net.URISyntaxException;
import java.nio.file.Path;

public class PathUtil {

    static Path file(String path) {
        return Path.of(path);
    }

    public static Path resource(String path) {
        try {
            return Path.of(PathUtil.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
