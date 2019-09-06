package ru.cbr.neva.modules.metadatacollector.processor;

import java.nio.file.Path;
import java.util.Map;

public interface MetadataProcessor {
  String getMimeType();
  void process(Path path, Map<String, Object> metadata);
}
