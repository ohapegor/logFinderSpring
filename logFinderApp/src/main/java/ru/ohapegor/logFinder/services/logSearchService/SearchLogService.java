package ru.ohapegor.logFinder.services.logSearchService;

import ru.ohapegor.logFinder.entities.CorrectionCheckResult;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;

import java.util.concurrent.atomic.AtomicInteger;

public interface SearchLogService {

    AtomicInteger count = new AtomicInteger();

    SearchInfoResult logSearch(SearchInfo searchInfo);

    CorrectionCheckResult correctionCheck(SearchInfo searchInfo);

}
