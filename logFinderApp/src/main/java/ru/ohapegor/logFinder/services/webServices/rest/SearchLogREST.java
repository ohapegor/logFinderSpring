package ru.ohapegor.logFinder.services.webServices.rest;

import ru.ohapegor.logFinder.entities.InvalidSearchInfoException;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.services.file.generator.FileGeneratorService;
import ru.ohapegor.logFinder.services.logSearch.SearchLogService;
import ru.ohapegor.logFinder.services.webServices.AbstractWebService;


import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Path("/SearchLogService")
@Stateless
public class SearchLogREST extends AbstractWebService implements Serializable {

    @POST
    @Path("/logSearch")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response logSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogREST.logSearch(SearchInfo searchInfo)" + searchInfo);
        Response response = null;
        try {
            SearchInfoResult searchInfoResult = logSearchBL(searchInfo);
            response = Response.ok(searchInfoResult).build();
        } catch (Exception e) {
            if (e.getCause() instanceof InvalidSearchInfoException) {
                InvalidSearchInfoException ex = (InvalidSearchInfoException) e.getCause();
                logger.info("Incorrect searchInfo Exiting SearchLogREST.logSearch(SearchInfo searchInfo)");
                response = Response.ok().header("errorCode", ex.getErrorCode())
                        .header("errorMessage", ex.getErrorMessage()).build();
            } else {
                logger.error("Exception in SearchLogREST.logSearch(): " + e + "; exiting  SearchLogREST.logSearch()");
                e.printStackTrace();
                response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("exception", e.toString()).build();
            }
        }
        return response;
    }

}
