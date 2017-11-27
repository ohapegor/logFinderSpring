package ru.ohapegor.logFinder.services.file.generator;

import ru.ohapegor.logFinder.entities.SearchInfo;

import java.util.concurrent.atomic.AtomicInteger;

public interface FileGeneratorService {

    void fileGenerate(SearchInfo searchInfo);

    String fileSearch(SearchInfo searchInfo);

    void generateUniqueFilePath(SearchInfo searchInfo);

    String getUniqueFilePath();

}
