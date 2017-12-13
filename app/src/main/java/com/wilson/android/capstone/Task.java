package com.wilson.android.capstone;

import java.util.Date;
import java.util.UUID;


public class Task {

    private String mTaskName;
    private UUID mId;
    private Date mDate;

    private Boolean isCompleted = false;

    public Task(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Task(UUID id){
        this.mId = id;
        mDate = new Date();
    }

    public void setIsCompleted(boolean bool){
        isCompleted = bool;
    }

    public void setDate(Date date){
        mDate = date;
    }

    public UUID getId(){
        return mId;
    }
    public Date getDateCompleted() {
        return mDate;
    }
    public String getTaskName() {
        return mTaskName;
    }
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    public void setmTaskName (String s){
        mTaskName = s;
    }

}
