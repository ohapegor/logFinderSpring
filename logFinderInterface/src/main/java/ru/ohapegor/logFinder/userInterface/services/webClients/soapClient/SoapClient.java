package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.webServices.soap.SearchLogSOAP;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogClient;
import javax.xml.namespace.QName;


import java.net.URL;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@Service("soapClient")
@Scope("prototype")
public class SoapClient implements SearchLogClient {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Developer's log: Entering SoapClient.logSearch()");
        logger.info("Entering SoapClient.logSearch()");
        String wsdlUrl = Config.getString("LOG_SEARCH_SOAP_WSDL");
        String namespace = Config.getString("LOG_SEARCH_SOAP_NAMESPACE");
        String localPart = Config.getString("LOG_SEARCH_SOAP_LOCAL_PART");
        SearchInfoResult result = null;
        try {
            URL wsdl = new URL(wsdlUrl);
            QName serviceQName = new QName(namespace, localPart);
            javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdl, serviceQName);
            SearchLogSOAP searchLogSOAP = service.getPort(SearchLogSOAP.class);
            result = searchLogSOAP.logSearch(searchInfo);
        } catch (Exception e) {
            logger.error("Exception in SoapClient.logSearch() : " + getStackTrace(e));
        }
        logger.info("Exiting SoapClient.logSearch()");
        return result;
    }


}



