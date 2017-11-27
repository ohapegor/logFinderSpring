package ru.ohapegor.logFinder.services.logSearch;

import ru.ohapegor.logFinder.entities.CorrectionCheckResult;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;

import java.util.concurrent.atomic.AtomicInteger;

public interface SearchLogService {

    SearchInfoResult logSearch(SearchInfo searchInfo);

    CorrectionCheckResult correctionCheck(SearchInfo searchInfo);

}
