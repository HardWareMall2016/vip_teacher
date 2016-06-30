package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

/**
 * Created by WuYue on 2016/3/16.
 */
public class DailyLesson {
    @PrimaryKey(column = "LessonId")
    public long LessonId;

    public long StartTime;
    public long EndTime;
    public String LessonType;
    public String LessonTypeName;
    public String CourseType;
    public String CourseTypeName;
    public String CourseSubType;
    public String CourseSubTypeName;
    public String LessonStatus;
    public String LessonStatusName;
    public String StudentName;
    public String signStatus;
    public String PublicClassTitle;

    public long getLessonId() {
        return LessonId;
    }

    public void setLessonId(long lessonId) {
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

    public String getLessonType() {
        return LessonType;
    }

    public void setLessonType(String lessonType) {
        LessonType = lessonType;
    }

    public String getLessonTypeName() {
        return LessonTypeName;
    }

    public void setLessonTypeName(String lessonTypeName) {
        LessonTypeName = lessonTypeName;
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

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getPublicClassTitle() {
        return PublicClassTitle;
    }

    public void setPublicClassTitle(String publicClassTitle) {
        PublicClassTitle = publicClassTitle;
    }
}
