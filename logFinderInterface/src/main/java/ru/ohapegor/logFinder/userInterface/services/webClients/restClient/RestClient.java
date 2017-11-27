package ru.ohapegor.logFinder.userInterface.services.webClients.restClient;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogClient;


import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@Service("restClient")
@Scope("prototype")
public class RestClient implements SearchLogClient {


    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo){
        logger.info("Entering RestClient.logSearch()");
        String ENDPOINT = Config.getString("LOG_SEARCH_REST_ENDPOINT");
        SearchInfoResult searchInfoResult = null;
        try {
            javax.ws.rs.client.Client client = ClientBuilder.newClient();
            Response response = client.target(ENDPOINT).request(MediaType.APPLICATION_XML).post(Entity.entity(searchInfo, MediaType.APPLICATION_XML_TYPE));
            if (response.getHeaderString("errorCode") != null) {
                searchInfoResult = new SearchInfoResult();
                searchInfoResult.setErrorCode(Integer.parseInt(response.getHeaderString("errorCode")));
                searchInfoResult.setErrorMessage(response.getHeaderString("errorMessage"));
            } else {
                searchInfoResult = response.readEntity(SearchInfoResult.class);
            }
        } catch (Exception e) {
            logger.info("Exception in RestClient.logSearch(SearchInfo searchInfo): " + getStackTrace(e));
        }

        logger.info("Exiting RestClient.logSearch(SearchInfo searchInfo)");
        return searchInfoResult;
    }

}
