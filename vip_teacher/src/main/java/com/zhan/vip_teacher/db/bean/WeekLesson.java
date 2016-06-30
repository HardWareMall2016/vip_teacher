package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

/**
 * Created by WuYue on 2016/3/17.
 */
public class WeekLesson {
    @PrimaryKey(column = "LessonId")
    private int LessonId;
    private long StartTime;
    private long EndTime;
    private boolean IsOnLine;
    private boolean IsFirstLesson;
    private boolean IsFeedBack;
    private String CourseType;
    private String CourseTypeName;
    private String CourseSubType;
    private String CourseSubTypeName;
    private String StudentName;
    private String SignStatus;
    private String SignStatusName;
    private String LessonStatus;
    private String LessonStatusName;

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
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

    public boolean isOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(boolean isOnLine) {
        IsOnLine = isOnLine;
    }

    public boolean isFirstLesson() {
        return IsFirstLesson;
    }

    public void setIsFirstLesson(boolean isFirstLesson) {
        IsFirstLesson = isFirstLesson;
    }

    public boolean isFeedBack() {
        return IsFeedBack;
    }

    public void setIsFeedBack(boolean isFeedBack) {
        IsFeedBack = isFeedBack;
    }

    public String getCourseType() {
        return CourseType;
    }

    public void setCourseType(String courseType) {
        CourseType = courseType;
    }

    public String getCourseTypeName() {
        return CourseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        CourseTypeName = courseTypeName;
    }

    public String getCourseSubType() {
        return CourseSubType;
    }

    public void setCourseSubType(String courseSubType) {
        CourseSubType = courseSubType;
    }

    public String getCourseSubTypeName() {
        return CourseSubTypeName;
    }

    public void setCourseSubTypeName(String courseSubTypeName) {
        CourseSubTypeName = courseSubTypeName;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getSignStatus() {
        return SignStatus;
    }

    public void setSignStatus(String signStatus) {
        SignStatus = signStatus;
    }

    public String getSignStatusName() {
        return SignStatusName;
    }

    public void setSignStatusName(String signStatusName) {
        SignStatusName = signStatusName;
    }

    public String getLessonStatus() {
        return LessonStatus;
    }

    public void setLessonStatus(String lessonStatus) {
        LessonStatus = lessonStatus;
    }

    public String getLessonStatusName() {
        return LessonStatusName;
    }

    public void setLessonStatusName(String lessonStatusName) {
        LessonStatusName = lessonStatusName;
    }
}
