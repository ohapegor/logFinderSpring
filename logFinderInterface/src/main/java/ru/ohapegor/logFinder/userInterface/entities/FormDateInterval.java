package ru.ohapegor.logFinder.userInterface.entities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.entities.SignificantDateInterval;

import java.text.ParseException;

public class FormDateInterval {

    // final static Logger logger = Logger.getLogger(FormDateInterval.class.getName());
    private static final Logger logger = LogManager.getLogger();

    private FormDate dateFrom = new FormDate();
    private FormDate dateTo = new FormDate();

    public FormDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(FormDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public FormDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(FormDate dateTo) {
        this.dateTo = dateTo;
    }

    public SignificantDateInterval toSignificantDateInterval() throws ParseException {
        logger.info("Enter FormDateInterval.toSignificantDateInterval() dateFrom="+dateFrom+" dateTo"+dateTo);
        SignificantDateInterval significantDateInterval = new SignificantDateInterval();
        significantDateInterval.setDateFrom(dateFrom.getTime());
        significantDateInterval.setDateTo(dateTo.getTime());
        logger.info("Exit FormDateInterval.toSignificantDateInterval() dateFrom="+dateFrom.getTime().getTime()+" dateTo"+dateTo.getTime().getTime());
        return significantDateInterval;
    }

    @Override
    public String toString() {
        return "FormDateInterval{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }

    public void clearInterval(){
        dateFrom.clear();
        dateTo.clear();
    }

}
