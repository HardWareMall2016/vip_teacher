package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class LessonListBean implements Serializable {

    private String StartTime;
    private String EndTime;
    private String LessonStatus;
    private String LessonStatusName;
    private String StudentName;
    private String CourseType;
    private String CourseTypeName;
    private String CourseSubType;
    private String CourseSubTypeName;
    private String StudentGuid ;
    private int CourseId ;

    public void setCourseId(int CourseId){
        this.CourseId = CourseId ;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public void setLessonStatus(String LessonStatus) {
        this.LessonStatus = LessonStatus;
    }

    public void setLessonStatusName(String LessonStatusName) {
        this.LessonStatusName = LessonStatusName;
    }

    public void setStudentGuid(String StudentGuid){
        this.StudentGuid = StudentGuid ;
    }

    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
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

    public int getCourseId(){
        return CourseId ;
    }
    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getLessonStatus() {
        return LessonStatus;
    }

    public String getLessonStatusName() {
        return LessonStatusName;
    }

    public String getStudentName() {
        return StudentName;
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

    public String getStudentGuid(){
        return StudentGuid ;
    }
}