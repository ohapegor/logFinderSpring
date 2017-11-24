package ru.ohapegor.logFinder.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.Calendar;

@XmlRootElement(name = "significantDateInterval")
@JsonRootName("significantDateInterval")
public class SignificantDateInterval {

    private Calendar dateFrom;

    private Calendar dateTo;

    public SignificantDateInterval() {

    }

    private SignificantDateInterval(Calendar dateFrom, Calendar dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public static SignificantDateInterval of(Calendar dateFrom, Calendar dateTo){
        return new SignificantDateInterval(dateFrom,dateTo);
    }

    @XmlElement(name = "dateFrom")
    @XmlSchemaType(name = "dateTime")
    @JsonProperty("dateFrom")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Calendar getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Calendar dateFrom) {
        this.dateFrom = dateFrom;
    }

    @XmlElement(name = "dateTo")
    @XmlSchemaType(name = "dateTime")
    @JsonProperty("dateTo")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Calendar getDateTo() {
        return dateTo;
    }

    public void setDateTo(Calendar dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignificantDateInterval)) return false;

        SignificantDateInterval interval = (SignificantDateInterval) o;

        if (dateFrom != null ? !dateFrom.equals(interval.dateFrom) : interval.dateFrom != null) return false;
        return dateTo != null ? dateTo.equals(interval.dateTo) : interval.dateTo == null;
    }

    @Override
    public int hashCode() {
        int result = dateFrom != null ? dateFrom.hashCode() : 0;
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignificantDateInterval{" +
                "dateFrom=" + dateFrom.getTime() +
                ", dateTo=" + dateTo.getTime() +
                '}';
    }
}
