package ru.ohapegor.logFinder.services.webServices.soap;

import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;

import javax.jws.WebService;

@WebService
public interface SearchLogSOAP {

    SearchInfoResult logSearch(SearchInfo searchInfo);
}
