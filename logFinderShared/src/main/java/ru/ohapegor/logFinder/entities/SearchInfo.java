package ru.ohapegor.logFinder.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonRootName("searchInfo")
public class SearchInfo {

    private String regexp;

    private List<SignificantDateInterval> dateIntervals;

    private String location;

    private boolean realization;

    private String fileExtension;

    @XmlElement
    @JsonProperty
    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @XmlElement
    @JsonProperty
    public List<SignificantDateInterval> getDateIntervals() {
        return dateIntervals;
    }

    public void setDateIntervals(List<SignificantDateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }

    @XmlElement
    @JsonProperty
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlElement
    @JsonProperty
    public boolean getRealization() {
        return realization;
    }

    public void setRealization(boolean realization) {
        this.realization = realization;
    }

    @XmlElement
    @JsonProperty
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public static SearchInfo of(String regexp,String location, List<SignificantDateInterval> dateIntervals, FileExtension fileExtension){
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setRegexp(regexp);
        searchInfo.setDateIntervals(dateIntervals);
        searchInfo.setLocation(location);
        searchInfo.setFileExtension(fileExtension.name().toLowerCase());
        return searchInfo;
    }

    @Override
    public String toString() {
        return "SearchInfo{" +
                "regexp='" + regexp + '\'' +
                ", dateIntervals=" + dateIntervals +
                ", location='" + location + '\'' +
                ", realization=" + realization +
                ", fileExtension='" + fileExtension + '\'' +
                '}';
    }
}
