package ru.arttab.universalarchiveextractor.processor;

import java.nio.file.Path;
import java.util.Map;

public interface ExtractorProcessor {
    String getMimeType();
    void process(Path path, Map<String, Object> metadata);
}
