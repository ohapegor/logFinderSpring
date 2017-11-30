package ru.ohapegor.logFinder.services.webServices;

import commonj.work.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.fileServices.generator.FileGeneratorService;
import ru.ohapegor.logFinder.services.logSearchService.SearchLogService;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import commonj.work.WorkManager;

import java.lang.annotation.Inherited;
import java.util.Date;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public abstract class AbstractWebService  {

    protected static final Logger logger = LogManager.getLogger();

    private SearchLogService searchLogService;

    private FileGeneratorService fileGeneratorService;

    @Autowired
    public void setSearchLogService(SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }

    @Autowired
    public void setFileGeneratorService(FileGeneratorService fileGeneratorService) {
        this.fileGeneratorService = fileGeneratorService;
    }

    private WorkManager myWorkManager;

    @Autowired
    public void setMyWorkManager(WorkManager myWorkManager) {
        this.myWorkManager = myWorkManager;
    }

    private boolean fileSearch(SearchInfo searchInfo) {
        logger.info("Entering " + this.getClass().getSimpleName() + ".fileSearch(SearchInfo searchInfo)");
        boolean isFound = fileGeneratorService.fileSearch(searchInfo) != null;
        logger.info("Exiting  " + this.getClass().getSimpleName() + ".fileSearch(SearchInfo searchInfo)");
        return isFound;
    }

    private void fileGenerate(SearchInfo searchInfo) {
        logger.info("Entering  " + this.getClass().getSimpleName() + ".fileGenerate(SearchInfo searchInfo)");
        fileGeneratorService.fileGenerate(searchInfo);
        logger.info("Exiting  " + this.getClass().getSimpleName() + ".fileGenerate(SearchInfo searchInfo)");
    }

    protected SearchInfoResult logSearchBL(SearchInfo searchInfo) {
        logger.info("Entering  " + this.getClass().getSimpleName() + ".logSearchBL(SearchInfo searchInfo)");
        //throws InvalidSearchInfoException
        searchLogService.correctionCheck(searchInfo);

        SearchInfoResult searchInfoResult = null;
        //synchronous
        if (!searchInfo.getRealization()) {
            long start = new Date().getTime();
            searchInfoResult = searchLogService.logSearch(searchInfo);
            long end = new Date().getTime();
            searchInfoResult.setErrorMessage(searchInfoResult.getErrorMessage()+". Execution time = "+(end-start)+" ms");
        }

        //asynchronous
        else {
            if (!fileSearch(searchInfo)) {
                //if file not found
                fileGeneratorService.generateUniqueFilePath(searchInfo);
                //run asynchronous file generation
                try {
                    myWorkManager.schedule(new Work() {
                        @Override
                        public void release() {
                        }

                        @Override
                        public boolean isDaemon() {
                            return false;
                        }

                        @Override
                        public void run() {
                            logger.info("Starting asynch work in " + this.getClass().getSimpleName() + ".logSearchBL(SearchInfo searchInfo)");
                            fileGenerate(searchInfo);
                        }
                    });
                } catch (Exception e) {
                    logger.info("Exception in asych work " + this.getClass().getSimpleName() +
                            ".logSearchBL() : " + getStackTrace(e));
                }
            }
            //set file path in result
            searchInfoResult = SearchInfoResult.ofGeneratedFile(fileGeneratorService.getUniqueFilePath());
            searchInfoResult.setFilePath(fileGeneratorService.getUniqueFilePath());
        }
        logger.info("Exiting  " + this.getClass().getSimpleName());
        return searchInfoResult;
    }

}
