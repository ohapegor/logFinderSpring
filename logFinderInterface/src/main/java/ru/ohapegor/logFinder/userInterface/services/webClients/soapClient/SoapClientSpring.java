package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.LogSearchRequest;
import ru.ohapegor.logFinder.entities.LogSearchResponse;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogClient;


import javax.xml.soap.MessageFactory;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

@Service("soapClientSpring")
public class SoapClientSpring extends WebServiceGatewaySupport implements SearchLogClient {
    
    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SoapClientSpring.logSearch()");
        SearchInfoResult searchInfoResult = null;
        try {
            SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(
                    MessageFactory.newInstance());
            messageFactory.afterPropertiesSet();
            WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);

            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(LogSearchRequest.class,LogSearchResponse.class);
            marshaller.afterPropertiesSet();

            webServiceTemplate.setMarshaller(marshaller);
            webServiceTemplate.afterPropertiesSet();

            webServiceTemplate.setUnmarshaller(marshaller);

            LogSearchRequest request = new LogSearchRequest(searchInfo);
            LogSearchResponse response = (LogSearchResponse) webServiceTemplate.marshalSendAndReceive(
                    Config.getString("LOG_SEARCH_SOAP_SPRING_ENDPOINT"), request);
            searchInfoResult = response.getSearchInfoResult();
        }catch (Exception e){
            logger.error("Exception in SoapClient.logSearch() : " + getStackTrace(e));
        }
        logger.info("Exiting SoapClientSpring.logSearch()");
        return searchInfoResult;
    }

}
