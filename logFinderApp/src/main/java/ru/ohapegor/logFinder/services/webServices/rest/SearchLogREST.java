package ru.ohapegor.logFinder.services.webServices.rest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ohapegor.logFinder.entities.InvalidSearchInfoException;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.webServices.AbstractWebService;

import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@RestController
@RequestMapping("/SearchLogService")
@Scope("request")
public class SearchLogREST extends AbstractWebService {


    @RequestMapping(value = "/logSearch", method = RequestMethod.POST,
            consumes = {"application/xml", "application/json"},
            produces = {"application/xml", "application/json"})
    public ResponseEntity<SearchInfoResult> logSearch(@RequestBody SearchInfo searchInfo) {
        logger.info("Entering SearchLogREST.logSearch(SearchInfo " + searchInfo + ")");
        ResponseEntity<SearchInfoResult> response;
        SearchInfoResult searchInfoResult = null;
        try {


            //invoke business method
            searchInfoResult = logSearchBL(searchInfo);


            response = new ResponseEntity<>(searchInfoResult, HttpStatus.OK);
        } catch (InvalidSearchInfoException e) {
            logger.info("Incorrect searchInfo : "+ e.getCorrectionCheckResult()+" Exiting SearchLogREST.logSearch()");
            HttpHeaders headers = new HttpHeaders();
            headers.set("errorCode", "" + e.getErrorCode());
            headers.set("errorMessage", "" + e.getErrorMessage());
            response = new ResponseEntity<>(searchInfoResult, headers, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Exception in SearchLogREST.logSearch(): " + getStackTrace(e));
            HttpHeaders headers = new HttpHeaders();
            headers.set("unknown error", ExceptionUtils.getFullStackTrace(e));
            response = new ResponseEntity<>(searchInfoResult, headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Exiting SearchLogREST.logSearch()");
        return response;
    }

    @RequestMapping(value = "/logSearch1", method = RequestMethod.POST,
            consumes = {"application/xml", "application/json"},
            produces = {"application/xml", "application/json"})
    @ResponseBody
    public SearchInfoResult logSearch(@RequestBody SearchInfo searchInfo, HttpServletResponse response) {
        logger.info("Entering SearchLogREST.logSearch(SearchInfo " + searchInfo + ")");
        SearchInfoResult searchInfoResult = null;
        try {

            //invoke business method
            searchInfoResult = logSearchBL(searchInfo);

        } catch (InvalidSearchInfoException e) {
            logger.info("Incorrect searchInfo : " + e.getCorrectionCheckResult() + " Exiting SearchLogREST.logSearch()");
            searchInfoResult = new SearchInfoResult(e.getCorrectionCheckResult());
            response.setHeader("errorMessage",getStackTrace(e));
        } catch (Exception e) {
            logger.error("Exception in SearchLogREST.logSearch(): " + getStackTrace(e) + "; exiting  SearchLogREST.logSearch()");
            response.setHeader("error", getStackTrace(e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        logger.info("Exiting SearchLogREST.logSearch()");
        return searchInfoResult;
    }
}
