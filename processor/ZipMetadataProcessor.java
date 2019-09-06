package ru.cbr.neva.modules.metadatacollector.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zeroturnaround.zip.ZipUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ZipMetadataProcessor extends DefaultMetadataProcessor {
  private static final Logger logger = LoggerFactory.getLogger(ZipMetadataProcessor.class);

  @Override
  public String getMimeType() {
    return "application/zip";
  }

  @Override
  public void process(Path path, Map<String, Object> metadata) {
    super.process(path, metadata);
    List<String> files = new ArrayList<>();
    ZipUtil.iterate(path.toFile(), zipEntry -> {
      if (zipEntry.isDirectory()) {
        return;
      }
      files.add(zipEntry.getName());
    });
    logger.info("ZIP-архив содержит файлы: {}", files);
    metadata.put("files", files);
  }
}
