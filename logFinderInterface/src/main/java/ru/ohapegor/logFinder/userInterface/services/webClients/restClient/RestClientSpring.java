package ru.ohapegor.logFinder.userInterface.services.webClients.restClient;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogClient;


import java.util.Arrays;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@Service("restClientSpring")
@Scope("prototype")
public class RestClientSpring implements SearchLogClient {


    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering RestClient.logSearch()");
        String ENDPOINT = Config.getString("LOG_SEARCH_REST_ENDPOINT");
        SearchInfoResult searchInfoResult = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON));
            httpHeaders.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<SearchInfo> entity = new HttpEntity<>(searchInfo, httpHeaders);
            ResponseEntity<SearchInfoResult> responseEntity =
                    restTemplate.postForEntity(ENDPOINT, entity, SearchInfoResult.class);
            switch (responseEntity.getStatusCode()) {
                case OK:
                    searchInfoResult = responseEntity.getBody();
                    break;
                case NO_CONTENT:
                    String errorCode = String.valueOf(responseEntity.getHeaders().get("errorCode")).replaceAll("\\[|]", "");
                    String errorMessage = String.valueOf(responseEntity.getHeaders().get("errorMessage")).replaceAll("\\[|]", "");
                    searchInfoResult = SearchInfoResult.ofError(Long.parseLong(errorCode), errorMessage);
                    break;
                default:
                    searchInfoResult = SearchInfoResult.ofUnknownResponse();
            }

        } catch (Exception e) {
            logger.info("Exception in RestClient.logSearch(): " + getStackTrace(e));
        }
        logger.info("Exiting RestClient.logSearch()");
        return searchInfoResult;
    }

}
