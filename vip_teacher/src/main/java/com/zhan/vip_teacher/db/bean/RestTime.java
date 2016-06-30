package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

import java.util.UUID;

/**
 * Created by WuYue on 2016/3/17.
 */
public class RestTime {
    //唯一主键
    @PrimaryKey(column = "id")
    String id = UUID.randomUUID().toString();

    private String RestType;
    private String RestTypeName;
    private long StartTime;
    private long EndTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestType() {
        return RestType;
    }

    public void setRestType(String RestType) {
        this.RestType = RestType;
    }

    public String getRestTypeName() {
        return RestTypeName;
    }

    public void setRestTypeName(String RestTypeName) {
        this.RestTypeName = RestTypeName;
    }

    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void setEndTime(long endTime) {
        EndTime = endTime;
    }
}
