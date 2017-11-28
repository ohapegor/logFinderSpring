package ru.ohapegor.logFinder.userInterface.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;

public class FormDate {
    
    private static final Logger logger = LogManager.getLogger();

    private Calendar calendar = Calendar.getInstance();

    private String dd = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
    private String MM = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
    private String yyyy = String.valueOf(calendar.get(Calendar.YEAR));
    private String HH = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
    private String mm = String.format("%02d", calendar.get(Calendar.MINUTE));
    private String ss = String.format("%02d", calendar.get(Calendar.SECOND));

    public Calendar getTime() {
        logger.info("Entering FormDate.getTime()");
        if (notSpecified()) return null;
        Calendar time = parseFormTime();
        logger.info("Parse formDate successful, time = " + time.getTime() + "exiting FormDate.getTime()");
        return time;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getMM() {
        return MM;
    }

    public void setMM(String MM) {
        this.MM = MM;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getHH() {
        return HH;
    }

    public void setHH(String HH) {
        this.HH = HH;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    @Override
    public String toString() {
        return "FormDate{" +
                "dd='" + dd + '\'' +
                ", MM='" + MM + '\'' +
                ", yyyy='" + yyyy + '\'' +
                ", HH='" + HH + '\'' +
                ", mm='" + mm + '\'' +
                ", ss='" + ss + '\'' +
                '}';
    }

    public boolean notSpecified() {
        return ((dd == null || dd.equals(""))
                && (MM == null || MM.equals(""))
                && (yyyy == null || yyyy.equals(""))
                && (HH == null || HH.equals(""))
                && (mm == null || mm.equals(""))
                && (ss == null || ss.equals("")));
    }


    private Calendar parseFormTime() {
        //check day
        int dd = Integer.parseInt(this.dd);
        if (dd < 1 || dd > 31) throw new IllegalArgumentException("incorrect day dd = " + this.dd);

        int MM = Integer.parseInt(this.MM);
        if (MM < 1 || MM > 12) throw new IllegalArgumentException("incorrect month MM = " + this.MM);

        int YYYY = Integer.parseInt(this.yyyy);

        int HH = Integer.parseInt(this.HH);
        if (HH < 0 || HH > 59) throw new IllegalArgumentException("incorrect incorrect hour  HH = " + this.HH);

        int mm = Integer.parseInt(this.mm);
        if (mm < 0 || mm > 59) throw new IllegalArgumentException("incorrect minute mm = " + this.mm);

        int ss = Integer.parseInt(this.ss);
        if (ss < 0 || ss > 59) throw new IllegalArgumentException("incorrect second ss = " + this.ss);

        Calendar calendar = Calendar.getInstance();
        calendar.set(YYYY, MM - 1, dd, HH, mm, ss);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

    public void clear() {
        dd = "";
        mm = "";
        ss = "";
        HH = "";
        MM = "";
        yyyy = "";
    }


}
