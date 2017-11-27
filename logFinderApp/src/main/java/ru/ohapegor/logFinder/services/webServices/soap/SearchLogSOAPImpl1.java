package ru.ohapegor.logFinder.services.webServices.soap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.ohapegor.logFinder.entities.InvalidSearchInfoException;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.fileServices.generator.FileGeneratorService;
import ru.ohapegor.logFinder.services.logSearchService.SearchLogService;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

/*@WebService(serviceName = "SearchLogSOAPService",
        portName = "SearchLogSOAPPort",
        endpointInterface = "ru.ohapegor.logFinder.services.webServices.soap.SearchLogSOAP")
*/
public class SearchLogSOAPImpl1 extends SpringBeanAutowiringSupport implements SearchLogSOAP {

    private static final Logger logger = LogManager.getLogger();

    //без этой хрени не работает, wtf???
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private SearchLogService searchLogService;

    private FileGeneratorService fileGeneratorService;

    @Autowired
    @Qualifier("searchLogService")
    public void setSearchLogService(SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }

    @Autowired
    @Qualifier("fileGeneratorService")
    public void setFileGeneratorService(FileGeneratorService fileGeneratorService) {
        this.fileGeneratorService = fileGeneratorService;
    }

    @Override
    @WebMethod
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogSOAP.logSearch(SearchInfo searchInfo)" + searchInfo);
        SearchInfoResult searchInfoResult = null;
        try {
            searchLogService.correctionCheck(searchInfo);
            //synchronous
            if (!searchInfo.getRealization()) {
                searchInfoResult = searchLogService.logSearch(searchInfo);
            }
            //asynchronous
            else {
                if (!fileSearch(searchInfo)) {
                    //if file not found
                    fileGeneratorService.generateUniqueFilePath(searchInfo);
                    //run asynchronous file generation
                    fileGenerate(searchInfo);
                }
                searchInfoResult = new SearchInfoResult();
                //set file path in result
                searchInfoResult.setFilePath(fileGeneratorService.getUniqueFilePath());
            }
        } catch (InvalidSearchInfoException e) {
            logger.info("Incorrect searchInfo : "+ e.getCorrectionCheckResult()+" Exiting SearchLogSOAP.logSearch()");
            searchInfoResult = new SearchInfoResult();
            searchInfoResult.setErrorCode(e.getErrorCode());
            searchInfoResult.setErrorMessage(e.getErrorMessage());
        }catch (Exception e){
                logger.error("Exception in SearchLogSOAPImpl.logSearch(): " + getStackTrace(e));
        }
        logger.info("Exiting SearchLogSOAP.logSearch(SearchInfo searchInfo)");
        return searchInfoResult;
    }

    private boolean fileSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogSOAP.fileSearch(SearchInfo searchInfo)");
        boolean isFound = fileGeneratorService.fileSearch(searchInfo) != null;
        logger.info("Exiting SearchLogSOAP.fileSearch(SearchInfo searchInfo)");
        return isFound;
    }

    @Async
    private void fileGenerate(SearchInfo searchInfo) {
        logger.info("Entering SearchLogSOAP.fileGenerate(SearchInfo searchInfo)");
        fileGeneratorService.fileGenerate(searchInfo);
        logger.info("Exiting SearchLogSOAP.fileGenerate(SearchInfo searchInfo)");
    }

}