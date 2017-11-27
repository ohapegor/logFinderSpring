package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;

import javax.jws.WebService;

@WebService(targetNamespace = "http://soap.webServices.services.logFinder.ohapegor.ru/")
public interface SearchLogSOAP {

    SearchInfoResult logSearch(SearchInfo searchInfo);
}
