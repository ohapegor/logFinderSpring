package ru.ohapegor.logFinder.userInterface.services.webClients.restClient;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogClient;

@Service("restClientSpring1")
@Scope("prototype")
public class RestClientSpring1 implements SearchLogClient {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering "+ this.getClass().getSimpleName()+"logSearch()");
        String restEndpoint = Config.getString("LOG_SEARCH_REST_ENDPOINT")+"1/";
        logger.info("Invoking rest method at : "+restEndpoint);

        SearchInfoResult searchInfoResult =  new RestTemplate().postForObject(restEndpoint,
                searchInfo,SearchInfoResult.class);

        logger.info("Exiting "+ this.getClass().getSimpleName()+"logSearch()");
        return searchInfoResult;
    }
}
