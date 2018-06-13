package com.mxmbro.sesame.tasks;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Task implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;
    private String Task;
    private String Location;
    private Date Date;
    private boolean Complete;
    private String Priority;
    private String Extra_info;

    public Task(String taskName, String task, String taskWhere, Date taskDate) {
        Name = taskName;
        Task = task;
        Location = taskWhere;
        Date = taskDate;
        Complete = false;
        Priority = "normal";
        Extra_info = "";

    }

    public Date getDate() {
        return Date;
    }

    public String getDateString() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        return c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
    }

    public String getExtraInfo() {
        return Extra_info;
    }

    public String getID() {
        return ID;
    }

    public String getLocation() {
        return Location;
    }

    public String getName() {
        return Name;
    }

    public String getPriority() {
        return Priority;
    }

    public String getTask() {
        return Task;
    }

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        System.out.println(c.getTimeInMillis());
        return c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR) + " " + Location + " " + Task;
    }

    public boolean isComplete() {
        return Complete;
    }

    public void setDate(Date Date) {
        this.Date = Date;
    }

    public void setExtraInfo(String extra_info) {
        Extra_info = extra_info;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPriority(String Priority){
        this.Priority = Priority;
    }

    public void setTask(String Task) {
        this.Task = Task;
    }

    public void setComplete(boolean complete) {
        this.Complete = complete;
    }

    public void toggleComplete() {
        Complete = !Complete;
    }
}
