package ru.ohapegor.logFinder.services.webServices.soap;



import org.springframework.context.annotation.Scope;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.ohapegor.logFinder.entities.InvalidSearchInfoException;
import ru.ohapegor.logFinder.entities.LogSearchRequest;
import ru.ohapegor.logFinder.entities.LogSearchResponse;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.webServices.AbstractWebService;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

@Endpoint
@Scope("request")
public class SearchLogSOAPImpl extends AbstractWebService {

    private static final String NAMESPACE_URI = "http://ohapegor.ru/logFinder/services/webServices/soap/schema";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "logSearchRequest")
    @ResponsePayload
    public LogSearchResponse logSearch(@RequestPayload LogSearchRequest logSearchRequest) {
        logger.info("Entering SearchLogSOAPImpl.logSearch()" + logSearchRequest);
        SearchInfoResult searchInfoResult = null;
        try {


            //invoke business method
            searchInfoResult = logSearchBL(logSearchRequest.getSearchInfo());


        }catch (InvalidSearchInfoException e) {
            logger.info("Incorrect searchInfo : "+ e.getCorrectionCheckResult()+" Exiting SearchLogSOAP.logSearch()");
            searchInfoResult = new SearchInfoResult(e.getCorrectionCheckResult());
        }catch (Exception e){
                logger.error("Exception in SearchLogSOAPImpl.logSearch(): "+ getStackTrace(e));
        }
        logger.info("Exiting SearchLogSOAPImpl.logSearch()");
        return new LogSearchResponse(searchInfoResult);
    }

}