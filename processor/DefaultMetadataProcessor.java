package ru.cbr.neva.modules.metadatacollector.processor;

import org.apache.commons.codec.binary.Hex;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;
import ru.cbr.neva.commons.base.exception.ProcessingException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

@Component
public class DefaultMetadataProcessor implements MetadataProcessor {
  @Override
  public String getMimeType() {
    return MimeTypes.OCTET_STREAM;
  }

  @Override
  public void process(Path path, Map<String, Object> metadata) {
    long length = path.toFile().length();
    metadata.put("size", length);

    byte[] content = new byte[length > 1024 ? 1024 : (int) length];
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      int bytes = inputStream.read(content);
    } catch (IOException ex) {
      throw new ProcessingException("Ошибка чтения содержимого файла {}", ex, path.getFileName().toString());
    }
    metadata.put("content", Hex.encodeHexString(content));
  }
}
