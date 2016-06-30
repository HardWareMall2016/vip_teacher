package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

import java.util.UUID;

/**
 * Created by WuYue on 2016/3/17.
 */
public class WeekPublicLesson {

    @PrimaryKey(column = "CourseId")
    private int CourseId;

    private String ClassTitle;

    private long StartTime;

    private long EndTime;

    private String SignStatus;

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int courseId) {
        CourseId = courseId;
    }

    public String getClassTitle() {
        return ClassTitle;
    }

    public void setClassTitle(String classTitle) {
        ClassTitle = classTitle;
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

    public String getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(String signStatus) {
        SignStatus = signStatus;
    }
}
