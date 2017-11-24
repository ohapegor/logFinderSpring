package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import org.apache.logging.log4j.LogManager;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogWebClient;

import javax.ejb.Stateless;
import java.net.MalformedURLException;
import java.net.URL;


@Stateless(name = "soapClient1")
public class SoapClient implements SearchLogWebClient {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SoapClient.logSearch(SearchInfo searchInfo)");
        String ENDPOINT = Config.getString("LOG_SEARCH_SOAP_ENDPOINT");
        SearchInfoResult result = null;
        try {
            SearchLogSOAPService soapService = new SearchLogSOAPService(new URL(ENDPOINT));
            SearchLogSOAPClient client = soapService.getSearchLogSOAPPort();
            result = client.logSearch(searchInfo);
        } catch (MalformedURLException e) {
            logger.error("Exception in SoapClient.logSearch(SearchInfo searchInfo)");
            e.printStackTrace();
        }
        logger.info("Exiting SoapClient.logSearch(SearchInfo searchInfo)");
        return result;
    }

}



