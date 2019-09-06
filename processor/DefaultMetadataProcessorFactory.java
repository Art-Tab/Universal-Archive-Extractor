package ru.cbr.neva.modules.metadatacollector.processor;

import java.util.List;

public class DefaultMetadataProcessorFactory implements MetadataProcessorFactory {
  private List<MetadataProcessor> metadataProcessors;
  private DefaultMetadataProcessor defaultMetadataProcessor;

  public DefaultMetadataProcessorFactory(List<MetadataProcessor> metadataProcessors,
                                         DefaultMetadataProcessor defaultMetadataProcessor) {
    this.metadataProcessors = metadataProcessors;
    this.defaultMetadataProcessor = defaultMetadataProcessor;
  }

  @Override
  public MetadataProcessor createProcessor(String mimeType) {
    for (MetadataProcessor processor : metadataProcessors) {
      if (processor.getMimeType().equals(mimeType)) {
        return processor;
      }
    }
    return defaultMetadataProcessor;
  }
}
