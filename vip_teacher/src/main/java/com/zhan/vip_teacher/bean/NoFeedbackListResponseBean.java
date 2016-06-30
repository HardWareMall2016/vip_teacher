package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WuYue on 2016/3/10.
 */
public class NoFeedbackListResponseBean extends BaseResponseBean implements Serializable{

    /**
     * LessonId : 1
     * StartTime : 2016-03-10 17:31:03
     * EndTime : 2016-03-10 17:31:03
     * CourseType : sample string 4
     * CourseTypeName : sample string 5
     * CourseSubType : sample string 6
     * CourseSubTypeName : sample string 7
     * StudentName : sample string 8
     */

    private List<DataEntity> Data;

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity {
        private int LessonId;
        private String StartTime;
        private String EndTime;
        private String CourseType;
        private String CourseTypeName;
        private String CourseSubType;
        private String CourseSubTypeName;
        private String StudentName;

        public void setLessonId(int LessonId) {
            this.LessonId = LessonId;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
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

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public int getLessonId() {
            return LessonId;
        }

        public String getStartTime() {
            return StartTime;
        }

        public String getEndTime() {
            return EndTime;
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

        public String getStudentName() {
            return StudentName;
        }
    }
}
