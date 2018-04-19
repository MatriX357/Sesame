package com.mxmbro.sesame.tasks;

import java.io.Serializable;

public class Task implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String What;
    private String Where;
    private boolean complete;

    private long id;

    public Task(String taskWhat, String taskWhere) {
        What = taskWhat;
        Where = taskWhere;
    }

    public String getWhat() {
        return What;
    }

    public String getWhere() {
        return Where;
    }

    public void setWhat(String What) {
        this.What = What;
    }

    public void setWhere(String Where) {
        this.Where = Where;
    }

    public String toString() {
        return Where+" "+What;
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
}
