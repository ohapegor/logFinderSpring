package ru.ohapegor.logFinder.userInterface.controllers.interfaceConroller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.entities.*;
import ru.ohapegor.logFinder.userInterface.entities.FormDateInterval;
import ru.ohapegor.logFinder.userInterface.entities.Style;
import ru.ohapegor.logFinder.userInterface.services.webClients.SearchLogWebClient;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;


@ManagedBean(name = "itfBean", eager = true)
@SessionScoped
public class UserInterfaceBean{

    @EJB(beanName = "soapClient2")
    private SearchLogWebClient client;

    private static final Logger logger = LogManager.getLogger(UserInterfaceBean.class.getSimpleName());

    private List<FormDateInterval> formDateIntervals;
    private String regExp = ".*Log4j.*";
    private boolean realization = true;
    private String extension;
    private String location;
    private SearchInfo searchInfo;
    private SearchInfoResult result = new SearchInfoResult();
    private Style style = new Style();


    //add first date interval in form
    {
        formDateIntervals = new ArrayList<>();
        FormDateInterval firstInterval = new FormDateInterval();
        formDateIntervals.add(firstInterval);
    }

    //getters and setters
    public SearchInfoResult getResult() {
        return result;
    }

    public Style getStyle() {
        return style;
    }

    public void setResult(SearchInfoResult result) {
        this.result = result;
    }

    public String getRegExp() {
        return regExp;
    }

    public void addInterval() {
        formDateIntervals.add(new FormDateInterval());
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public List<FormDateInterval> getFormDateIntervals() {
        return formDateIntervals;
    }

    public void setFormDateIntervals(List<FormDateInterval> formDateIntervals) {
        this.formDateIntervals = formDateIntervals;
    }

    public boolean isRealization() {
        return realization;
    }

    public void setRealization(boolean realization) {
        this.realization = realization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    private CorrectionCheckResult correctionCheck() {

        logger.info("Entering correctionCheck(SearchInfo searchInfo)");

        searchInfo = new SearchInfo();
        searchInfo.setRealization(realization);
        searchInfo.setFileExtension(extension);
        searchInfo.setRegexp(regExp);
        searchInfo.setLocation(location);

        //3701
        //Missed async method file extension
        if (searchInfo.getRealization()) {
            try {
                FileExtension.valueOf(searchInfo.getFileExtension().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                logger.info(CorrectionCheckResult._3701.getErrorMessage() + ", exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
                throw new InvalidSearchInfoException(CorrectionCheckResult._3701);
            }
            logger.info(searchInfo.getFileExtension() + " - correct file extension.");
        }

        //37
        //Missed mandatory parameter
        if (searchInfo.getRegexp() == null || searchInfo.getRegexp().equals("")) {
            logger.info(CorrectionCheckResult._37.getErrorMessage() + " - regExp , exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }

        if (formDateIntervals == null || formDateIntervals.isEmpty()) {
            logger.info(CorrectionCheckResult._37.getErrorMessage() + " - formDateIntervals , UserInterfaceBean.exiting correctionCheck(SearchInfo searchInfo)");
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }

        //19
        //Incorrect time format
        List<SignificantDateInterval> significantDateIntervals = new ArrayList<>();
        logger.info("trying to parse form time formDateIntervals.size=" + formDateIntervals.size());
        try {
            for (FormDateInterval formDateInterval : formDateIntervals) {
                SignificantDateInterval significantDateInterval = new SignificantDateInterval();
                Calendar dateFrom = formDateInterval.getDateFrom().getTime();
                Calendar dateTo = formDateInterval.getDateTo().getTime();

                if (dateFrom == null) {
                    GregorianCalendar minGC = new GregorianCalendar();
                    minGC.setTime(new Date(0));
                    significantDateInterval.setDateFrom(minGC);
                } else {
                    significantDateInterval.setDateFrom(dateFrom);
                }

                if (dateTo == null) {
                    GregorianCalendar maxGC = new GregorianCalendar();
                    maxGC.setTime(new Date(Long.MAX_VALUE));
                    significantDateInterval.setDateTo(maxGC);
                } else {
                    significantDateInterval.setDateTo(dateTo);
                }

                significantDateIntervals.add(significantDateInterval);
                logger.info("Parse toSignificantDateInterval() successful" + significantDateIntervals);
            }
        } catch (Exception e) {
            logger.error("Exception while Parsing toSignificantDateInterval(): " + e);
            e.printStackTrace();
            throw new InvalidSearchInfoException(CorrectionCheckResult._19);
        }

        searchInfo.setDateIntervals(significantDateIntervals);



        for (SignificantDateInterval interval : significantDateIntervals) {
            if (interval.getDateFrom() == null || interval.getDateTo() == null) {
                logger.info("Date interval is missing or incorrect, exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
                throw new InvalidSearchInfoException(CorrectionCheckResult._37);
            }

            Calendar dateFrom = interval.getDateFrom();
            Calendar dateTo = interval.getDateTo();

            if (dateFrom.after(Calendar.getInstance())) {
                logger.info(CorrectionCheckResult._18.getErrorMessage() + " , exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
                throw new InvalidSearchInfoException(CorrectionCheckResult._18);
            }

            if (dateFrom.after(dateTo)) {
                logger.info(CorrectionCheckResult._18.getErrorMessage() + " , exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
                throw new InvalidSearchInfoException(CorrectionCheckResult._18);
            }
        }
        logger.info("SearchLogInfo is correct, exiting correctionCheck(SearchInfo searchInfo)");
        return CorrectionCheckResult._0;
    }

    public String logSearch() {
        logger.info("Entering UserInterfaceBean.logSearch()");
        try {
            //log user action
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            if (session.getAttribute("username") != null) {
                String  username = String.valueOf(session.getAttribute("username"));
                logger.fatal("User - " + username + " is searching logs with " + searchInfo);
            }

            correctionCheck();

            //invoke web service if info correct
            searchLogWithWebClient(searchInfo);
            if (result == null) {
                result = new SearchInfoResult();
                result.setEmptyResultMessage("Client returned NULL (web service is not reachable!)");
            }

        } catch (InvalidSearchInfoException e) {
            result = new SearchInfoResult();
            result.setErrorMessage(e.getErrorMessage());
            result.setErrorCode(e.getErrorCode());
        } catch (Exception e) {
            result = new SearchInfoResult();
            result.setErrorMessage(e.getMessage());
            logger.error("Exception in  UserInterfaceBean.logSearch(): " + e);
            e.printStackTrace();
        }
        logger.info("Exiting UserInterfaceBean.logSearch()");

        return realization ? "/pages/secured/asyncResult?faces-redirect=true" : "/pages/secured/syncResult?faces-redirect=true";
    }

    private void searchLogWithWebClient(SearchInfo searchInfo) {
        logger.info("Entering UserInterfaceBean.searchLogWithWebClient(SearchInfo searchInfo)");
        result = client.logSearch(searchInfo);
        logger.info("Exiting UserInterfaceBean.searchLogWithWebClient(SearchInfo searchInfo)");
    }

    public void download() {
        logger.info("Developer's log: Entering UserInterfaceBean.download()");
        ServletOutputStream output = null;
        InputStream input = null;
        FacesContext facesContext = null;
        try {
            facesContext = FacesContext.getCurrentInstance();
            //log user action

            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            if (session.getAttribute("username") != null) {
                String username = String.valueOf(session.getAttribute("username"));
                logger.fatal("User - " + username + " is downloading file:" + location);
            }

            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            response.reset();

            File file = new File(result.getFilePath());
            input = new BufferedInputStream(new FileInputStream(file));
            output = response.getOutputStream();

            response.addHeader("Content-Disposition", "attachment; filename=" + result.getFilePath());
            response.setContentLength((int) file.length());

            int read = 0;
            while ((read = input.read()) != -1) {
                output.write(read);
            }
            facesContext.responseComplete();
        } catch (Exception e) {
            logger.error("Exception in UserInterfaceBean.download() :" + e);
            e.printStackTrace();
        } finally {
            try {
                output.flush();
                output.close();} catch (Exception ignored) {}
            try {input.close();} catch (Exception ignored) {}
        }
        logger.info("Developer's log: Exiting UserInterfaceBean.download()");
    }

    public void deleteInterval(FormDateInterval interval) {
        formDateIntervals.remove(interval);
    }

    public String goBack() {
        return "/pages/secured/UserInterface?faces-redirect=true";
    }

    public void cleanAll() {
        location = "";
        //formDateIntervals = new ArrayList<>();
        for (FormDateInterval interval : formDateIntervals) {
            interval.clearInterval();
        }
        regExp = "";
    }

    public void setColorRGB() {
        style.setColorRGB();
    }

    public void setColorRgb(int r, int g, int b) {
        style.setR(r);
        style.setG(g);
        style.setB(b);
        style.setColorRGB();
    }

}
