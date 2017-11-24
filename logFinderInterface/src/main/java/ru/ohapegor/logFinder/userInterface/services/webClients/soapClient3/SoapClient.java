package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient3;


import ru.ohapegor.logFinder.userInterface.services.webClients.soapClient2.SearchLogSOAP;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import java.net.MalformedURLException;
import java.net.URL;

@WebServiceClient(name = "SearchLogSOAPService",
        targetNamespace = "http://soap.web_services.services.logFinder.ohapegor.ru/",
        wsdlLocation = "http://localhost:7003/logFinderEjbApp/SearchLogSOAPService?wsdl")
public class SoapClient extends Service {

    private final static URL SEARCHLOG_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://localhost:7003/logFinderEjbApp/SearchLogSOAPService?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SEARCHLOG_WSDL_LOCATION = url;
    }

    public SoapClient(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    public SoapClient(){
        super(SEARCHLOG_WSDL_LOCATION,new QName("http://soap.web_services.services.logFinder.ohapegor.ru/",
                "SearchLogSOAPService"));
    }

    @WebEndpoint(name = "SearchLogSOAPPort")
    public SearchLogSOAP getHelloWorldImplPort() {
        return super.getPort(new QName("http://ws.mkyong.com/", "SearchLogSOAPPort"),SearchLogSOAP.class);
    }
}
