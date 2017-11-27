package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogWebClient;

import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;


@Stateless(name = "soapClient")
public class SoapClient implements SearchLogWebClient {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SoapClient.logSearch(SearchInfo searchInfo)");
        String wsdlUrl = Config.getString("LOG_SEARCH_SOAP_WSDL");
        String namespace = Config.getString("LOG_SEARCH_SOAP_NAMESPACE");
        String localPart = Config.getString("LOG_SEARCH_SOAP_LOCAL_PART");
        SearchInfoResult result = null;
        try {
            URL wsdl = new URL(wsdlUrl);
            QName serviceQName = new QName(namespace, localPart);
            Service service = Service.create(wsdl, serviceQName);
            SearchLogSOAP searchLogSOAP = service.getPort(SearchLogSOAP.class);
            result = searchLogSOAP.logSearch(searchInfo);
        } catch (Exception e) {
            logger.error("Exception in SoapClient.logSearch() : " + e);
            e.printStackTrace();
        }
        logger.info("Exiting SoapClient.logSearch()");
        return result;
    }
}



