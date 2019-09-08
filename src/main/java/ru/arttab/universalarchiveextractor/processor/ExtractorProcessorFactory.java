package ru.arttab.universalarchiveextractor.processor;

public interface ExtractorProcessorFactory {
    ExtractorProcessor createProcessor(String mimeType);
}
