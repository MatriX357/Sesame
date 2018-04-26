package com.mxmbro.sesame.tasks;

import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String What;
    private String Where;
    private Date Date;
    private boolean complete;

    private long id;

    public Task(String taskWhat, String taskWhere, Date taskDate) {
        What = taskWhat;
        Where = taskWhere;
        Date = taskDate;

    }

    public String getWhat() {
        return What;
    }

    public String getWhere() {
        return Where;
    }

    public Date getDate() {
        return Date;
    }

    public void setWhat(String What) {
        this.What = What;
    }

    public void setWhere(String Where) {
        this.Where = Where;
    }

    public void setDate(Date Date) {
        this.Date = Date;
    }

    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        System.out.println(c.getTimeInMillis());
        return c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR) + " " + Where + " " + What;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void toggleComplete() {
        complete = !complete;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDateString() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        return c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
    }
}
