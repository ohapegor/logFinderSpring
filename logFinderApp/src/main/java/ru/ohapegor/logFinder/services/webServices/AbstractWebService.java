package ru.ohapegor.logFinder.services.webServices;


import commonj.work.Work;
import commonj.work.WorkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.file.generator.FileGeneratorService;
import ru.ohapegor.logFinder.services.logSearch.SearchLogService;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


public abstract class AbstractWebService {

    protected static final Logger logger = LogManager.getLogger();

    @PostConstruct
    private void init(){
        try {
            InitialContext context = new InitialContext();
            searchLogService = (SearchLogService) context.lookup("java:module/SearchLogBean");
            fileGeneratorService = (FileGeneratorService) context.lookup("java:module/FileGeneratorBean");
        }catch (Exception e){
            logger.error("Exception in "+this.getClass().getSimpleName()+".init() : "+e);
            e.printStackTrace();
        }
    }


    //@EJB
    private SearchLogService searchLogService;

    //@EJB
    private FileGeneratorService fileGeneratorService;

    private WorkManager myWorkManager;

    private void initWorkManager() {
        try {
            InitialContext context = new InitialContext();
            myWorkManager = (commonj.work.WorkManager) context.lookup("java:comp/env/wm/myWorkManager");
        } catch (NamingException e) {
            logger.error("Exception in " + this.getClass().getSimpleName() + ".initWorkManager() : " + getStackTrace(e));
        }

    }

    private boolean fileSearch(SearchInfo searchInfo) {
        logger.info("Entering " + this.getClass().getSimpleName() + ".fileSearch()");
        boolean isFound = fileGeneratorService.fileSearch(searchInfo) != null;
        logger.info("Exiting  " + this.getClass().getSimpleName() + ".fileSearch()");
        return isFound;
    }


    private void fileGenerate(SearchInfo searchInfo) {
        logger.info("Entering  " + this.getClass().getSimpleName() + ".fileGenerate()");
        fileGeneratorService.fileGenerate(searchInfo);
        logger.info("Exiting  " + this.getClass().getSimpleName() + ".fileGenerate()");
    }

    protected SearchInfoResult logSearchBL(SearchInfo searchInfo) {
        logger.info("Entering  " + this.getClass().getSimpleName() + ".logSearchBL()");
        searchLogService.correctionCheck(searchInfo);
        SearchInfoResult searchInfoResult = null;
        //synchronous
        if (!searchInfo.getRealization()) {
            searchInfoResult = searchLogService.logSearch(searchInfo);
        }

        //asynchronous
        else{
            if (!fileSearch(searchInfo)) {
                //if file not found
                fileGeneratorService.generateUniqueFilePath(searchInfo);
                //run asynchronous file generation
                initWorkManager();
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
                            logger.info("Starting asynch work in " + this.getClass().getSimpleName() + ".logSearchBL()");
                            fileGenerate(searchInfo);
                        }
                    });
                } catch (Exception e) {
                    logger.info("Exception in asych work " + this.getClass().getSimpleName() + ".logSearchBL() : " + e);
                    e.printStackTrace();
                }
            }
            //set file path in result
            searchInfoResult = new SearchInfoResult();
            searchInfoResult.setFilePath(fileGeneratorService.getUniqueFilePath());
        }
        logger.info("Exiting " + this.getClass().getSimpleName() + ".logSearchBL()");
        return searchInfoResult;
    }

}
