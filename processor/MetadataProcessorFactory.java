package ru.cbr.neva.modules.metadatacollector.processor;

public interface MetadataProcessorFactory {
  MetadataProcessor createProcessor(String mimeType);
}
