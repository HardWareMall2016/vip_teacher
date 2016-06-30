package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by WuYue on 2016/2/29.
 */
public class NoFeedbackListItemBean implements Serializable {
    private static final long serialVersionUID = 769353734097658280L;

    private int LessonId;
    private String StartTime;
    private String EndTime;
    private String CourseType;
    private String CourseTypeName;
    private String CourseSubType;
    private String CourseSubTypeName;
    private String StudentName;

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
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
}
