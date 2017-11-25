package ru.ohapegor.logFinder.userInterface.services.webClients.restClient;


import org.apache.logging.log4j.LogManager;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogWebClient;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Stateless(name = "restClient")
public class RestClient implements SearchLogWebClient {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();


    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering RestClient.logSearch(SearchInfo searchInfo)");
        String ENDPOINT = Config.getString("LOG_SEARCH_REST_ENDPOINT");
        SearchInfoResult searchInfoResult = null;
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(ENDPOINT).request(MediaType.APPLICATION_XML).post(Entity.entity(searchInfo, MediaType.APPLICATION_XML_TYPE));
            if (response.getHeaderString("errorCode") != null) {
                searchInfoResult = new SearchInfoResult();
                searchInfoResult.setErrorCode(Integer.parseInt(response.getHeaderString("errorCode")));
                searchInfoResult.setErrorMessage(response.getHeaderString("errorMessage"));
            } else {
                searchInfoResult = response.readEntity(SearchInfoResult.class);
            }
        } catch (Exception e) {
            logger.info("Exception in RestClient.logSearch(SearchInfo searchInfo): " + e);
            e.printStackTrace();
        }

        logger.info("Exiting RestClient.logSearch(SearchInfo searchInfo)");
        return searchInfoResult;
    }

}
