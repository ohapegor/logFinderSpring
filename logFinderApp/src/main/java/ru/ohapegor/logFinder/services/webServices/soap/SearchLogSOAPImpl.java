package ru.ohapegor.logFinder.services.webServices.soap;

import ru.ohapegor.logFinder.entities.InvalidSearchInfoException;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.webServices.AbstractWebService;

import javax.enterprise.context.RequestScoped;
import javax.jws.WebMethod;
import javax.jws.WebService;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@WebService(endpointInterface = "ru.ohapegor.logFinder.services.webServices.soap.SearchLogSOAP",
serviceName = "SearchLogSOAPService",
portName = "SearchLogSOAPPort")
public class SearchLogSOAPImpl extends AbstractWebService implements SearchLogSOAP{

    @WebMethod
    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogSOAPImpl.logSearch()" + searchInfo);
        SearchInfoResult searchInfoResult = null;
        try {
            searchInfoResult = logSearchBL(searchInfo);
        } catch (Exception e) {
           if (e.getCause() instanceof InvalidSearchInfoException){
               InvalidSearchInfoException ex = (InvalidSearchInfoException)e.getCause();
               searchInfoResult = new SearchInfoResult(ex.getCorrectionCheckResult());
           } else {
              logger.error("Exception in SearchLogSOAPImpl.logSearch(): "+getStackTrace(e));
           }
        }
        logger.info("Exiting SearchLogSOAPImpl.logSearch(SearchInfo searchInfo)");
        return searchInfoResult;
    }
}