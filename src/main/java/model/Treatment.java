package model;

import utils.DateUtils;
import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long tid;
    private long pid;
    private long cid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private String description;
    private String remarks;
    private boolean locked;

    public Treatment(long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    public Treatment(long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, long cid) {
        this.pid = pid;
        this.cid = cid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    public Treatment(long tid, long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    /**
     * Constructor for Treatments
     *
     * @param tid The treatment id
     * @param pid The patient id
     * @param cid The caregiver id
     * @param date The date when the treatment was
     * @param begin The time when the treatment began
     * @param end The end when the treatment stopped
     * @param description The description of the treatment
     * @param remarks The remarks of the treatment
     * @param locked status if the treatment is locked
     */
    public Treatment(long tid, long pid, long cid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, boolean locked) {
        this.tid = tid;
        this.pid = pid;
        this.cid = cid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.locked = locked;
    }

    public Treatment(long tid, long pid, long cid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.tid = tid;
        this.pid = pid;
        this.cid = cid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    public long getTid() {
        return tid;
    }

    public long getCid() {
        return cid;
    }

    public long getPid() {
        return this.pid;
    }

    public String getDate() {
        return date.toString();
    }

    public String getBegin() {
        return begin.toString();
    }

    public String getEnd() {
        return end.toString();
    }

    public void setDate(String s_date) {
        LocalDate date = DateUtils.convertStringToLocalDate(s_date);
        this.date = date;
    }

    public void setBegin(String begin) {
        LocalTime time = DateUtils.convertStringToLocalTime(begin);
        this.begin = time;
    }

    public void setEnd(String end) {
        LocalTime time = DateUtils.convertStringToLocalTime(end);
        this.end = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nDescription: " + this.description +
                "\nRemarks: " + this.remarks + "\n";
    }
}