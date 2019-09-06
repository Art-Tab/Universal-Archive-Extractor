package ru.cbr.neva.routes.cadergatekhd;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import ru.cbr.neva.routes.cedargatekhd.utils.FileExploder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileExploderTest {
    List<Path> result;

    @Before
    public void setup() throws IOException {
        Path configFilePath = Paths.get("src/test/resources/package/");
        Path copied = Paths.get("src\\test\\resources\\temp");
        if (Files.exists(copied))
            Files.walk(copied)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        File source = new File(String.valueOf(configFilePath));
        File dest = new File(String.valueOf(copied));
        FileUtils.copyDirectory(source, dest);

        try (Stream<Path> walk = Files.walk(copied)) {
            result = walk.filter(Files::isRegularFile)
                    .map(path -> {
                        return path.toAbsolutePath();
                    }).collect(Collectors.toList());
            result.forEach(System.out::println);
        }
    }

    @Test
    public void shouldExplode() {
        for (Path path : result) {
            FileExploder.explodeFile(path);
        }
    }
}
