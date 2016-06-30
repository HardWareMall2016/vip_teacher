package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class StudentCourseBean implements Serializable {
    private String StartTime;
    private String EndTime;
    private String LessonState;
    private String LessonStateName;
    private String TeacherName;
    private String CourseType;
    private String CourseTypeName;
    private String CourseSubType;
    private String CourseSubTypeName;
    private long  LessonId ;

    public long getLessonId() {
        return LessonId;
    }

    public void setLessonId(long lessonId) {
        LessonId = lessonId;
    }


    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public void setLessonState(String LessonState) {
        this.LessonState = LessonState;
    }

    public void setLessonStateName(String LessonStateName) {
        this.LessonStateName = LessonStateName;
    }

    public void setTeacherName(String TeacherName) {
        this.TeacherName = TeacherName;
    }

    public void setCourseType(String CourseType) {
        this.CourseType = CourseType;
    }

    public void setCourseTypeName(String CourseTypeName) {
        this.CourseTypeName = CourseTypeName;
    }

    public void setCourseSubType(String CourseSubType) {
        this.CourseSubType = CourseSubType;
    }

    public void setCourseSubTypeName(String CourseSubTypeName) {
        this.CourseSubTypeName = CourseSubTypeName;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getLessonState() {
        return LessonState;
    }

    public String getLessonStateName() {
        return LessonStateName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public String getCourseType() {
        return CourseType;
    }

    public String getCourseTypeName() {
        return CourseTypeName;
    }

    public String getCourseSubType() {
        return CourseSubType;
    }

    public String getCourseSubTypeName() {
        return CourseSubTypeName;
    }
}