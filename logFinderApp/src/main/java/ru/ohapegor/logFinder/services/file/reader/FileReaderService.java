package ru.ohapegor.logFinder.services.file.reader;

import ru.ohapegor.logFinder.entities.SearchInfoResult;

import java.io.File;

public interface FileReaderService {

    SearchInfoResult readInfoFromFile(File file);

}
