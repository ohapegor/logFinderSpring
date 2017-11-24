package ru.ohapegor.logFinder.userInterface.services.webClients;

import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;

public interface SearchLogWebClient {

    SearchInfoResult logSearch(SearchInfo searchInfo);

}
