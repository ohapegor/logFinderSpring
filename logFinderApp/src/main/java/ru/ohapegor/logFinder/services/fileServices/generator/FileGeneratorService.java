package ru.ohapegor.logFinder.services.fileServices.generator;

import ru.ohapegor.logFinder.entities.SearchInfo;

import java.util.concurrent.atomic.AtomicInteger;


public interface FileGeneratorService {

    AtomicInteger count = new AtomicInteger();

    void fileGenerate(SearchInfo searchInfo);

    String fileSearch(SearchInfo searchInfo);

    void generateUniqueFilePath(SearchInfo searchInfo);

    String getUniqueFilePath();

}
