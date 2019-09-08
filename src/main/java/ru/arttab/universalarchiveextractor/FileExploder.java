package ru.arttab.universalarchiveextractor;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cbr.neva.commons.base.exception.ProcessingException;
import ru.cbr.neva.routes.cedargatekhd.constants.Extentions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class FileExploder {
    private static final Logger logger = LoggerFactory.getLogger(FileExploder.class);

    public static void explodeFile(Path path) {
        try {
            Path tempDir = Files.createTempDirectory(path.getParent(), "tmp");
            if (path.toFile().getName().toLowerCase().endsWith(Extentions.ZIP_EXTENTION)) {
                logger.info("Архив определен как ZIP");
                try (InputStream is = new FileInputStream(path.toFile());
                     ArchiveInputStream ais = new ArchiveStreamFactory()
                             .createArchiveInputStream("zip", is)
                ) {
                    ZipArchiveEntry entry = (ZipArchiveEntry) ais.getNextEntry();
                    logger.info("Начало извлечения данных");
                    while (entry != null) {
                        try (FileOutputStream out = new FileOutputStream(tempDir + File.separator + entry.getName())) {
                            byte[] content = new byte[(int) entry.getSize()];
                            ais.read(content, 0, content.length);
                            out.write(content);
                        } catch (Exception ex) {
                            throw new ProcessingException("Ошибка при переносе данных из zip архива в директорию", ex);
                        }
                        entry = (ZipArchiveEntry) ais.getNextEntry();
                    }
                    logger.info("Извлечение данных завершено");
                } catch (ArchiveException ex) {
                    throw new ProcessingException("Ошибка извлечения данных из архива zip", ex);
                }

            } else if (path.toFile().getName().toLowerCase().endsWith(Extentions.Z7_EXTENTION)) {
                logger.info("Архив определен как Z7");
                SevenZFile sevenZFile = new SevenZFile(path.toFile());
                SevenZArchiveEntry entry = sevenZFile.getNextEntry();
                logger.info("Начало извлечения данных");
                while (entry != null) {
                    try (FileOutputStream out = new FileOutputStream(tempDir + File.separator + entry.getName())) {
                        byte[] content = new byte[(int) entry.getSize()];
                        sevenZFile.read(content, 0, content.length);
                        out.write(content);
                    } catch (Exception ex) {
                        throw new ProcessingException("Ошибка при переносе данных из Z7 архива в директорию", ex);
                    }
                    entry = sevenZFile.getNextEntry();
                }
                sevenZFile.close();
                logger.info("Извлечение данных завершено");
            } else if (path.toFile().getName().toLowerCase().endsWith(Extentions.BZIP2_EXTENTION)) {
                logger.info("Архив определен как BZ2");
                logger.info("Начало извлечения данных");
                try (FileInputStream in = new FileInputStream(path.toFile());
                     OutputStream out =
                             Files.newOutputStream(Paths.get(String.valueOf(new StringBuilder(tempDir.toString())
                                     .append(File.separator)
                                     .append(path.getFileName().toString().toLowerCase().replaceAll(Extentions.BZIP2_EXTENTION, "")))));
                     BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in)) {
                    final byte[] buffer = new byte[1024];
                    int n = 0;
                    while (-1 != (n = bzIn.read(buffer))) {
                        out.write(buffer, 0, n);
                    }
                } catch (Exception ex) {
                    throw new ProcessingException("Ошибка извлечения данных из архива bz2", ex);
                }
            }
            try {
                FileUtils.forceDelete(path.toFile());
            } catch (IOException ex) {
                throw new ProcessingException("Ошибка удаления исходного файла архива", ex);
            }
            logger.info("Выполено удаление архива {}", path.toString());
            Files.move(tempDir, path, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException ex) {
            throw new ProcessingException("Ошибка извлечения данных из архива", ex);
        }
    }
}
