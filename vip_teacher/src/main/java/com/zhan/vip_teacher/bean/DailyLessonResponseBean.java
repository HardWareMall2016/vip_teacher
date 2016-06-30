package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.util.List;

/**
 * Created by WuYue on 2016/3/8.
 */
public class DailyLessonResponseBean extends BaseResponseBean {


    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        /**
         * LessonId : 1
         * StudentCourseId : 2
         * SignStatus : sample string 3
         * SignStatusName : sample string 4
         * StartTime : 2016-03-15 13:18:29
         * EndTime : 2016-03-15 13:18:29
         * LessonType : sample string 7
         * LessonTypeName : sample string 8
         * CourseType : sample string 9
         * CourseTypeName : sample string 10
         * CourseSubType : sample string 11
         * CourseSubTypeName : sample string 12
         * LessonStatus : sample string 13
         * LessonStatusName : sample string 14
         * StudentName : sample string 15
         */

        private List<ListLessonEntity> ListLesson;

        public void setListLesson(List<ListLessonEntity> ListLesson) {
            this.ListLesson = ListLesson;
        }

        public List<ListLessonEntity> getListLesson() {
            return ListLesson;
        }

        public static class ListLessonEntity {
            private int LessonId;
            private int StudentCourseId;
            private String SignStatus;
            private String SignStatusName;
            private String StartTime;
            private String EndTime;
            private String LessonType;
            private String LessonTypeName;
            private String CourseType;
            private String CourseTypeName;
            private String CourseSubType;
            private String CourseSubTypeName;
            private String LessonStatus;
            private String LessonStatusName;
            private String StudentName;
            private String PublicClassTitle;

            public void setLessonId(int LessonId) {
                this.LessonId = LessonId;
            }

            public void setStudentCourseId(int StudentCourseId) {
                this.StudentCourseId = StudentCourseId;
            }

            public void setSignStatus(String SignStatus) {
                this.SignStatus = SignStatus;
            }

            public void setSignStatusName(String SignStatusName) {
                this.SignStatusName = SignStatusName;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public void setLessonType(String LessonType) {
                this.LessonType = LessonType;
            }

            public void setLessonTypeName(String LessonTypeName) {
                this.LessonTypeName = LessonTypeName;
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

            public void setLessonStatus(String LessonStatus) {
                this.LessonStatus = LessonStatus;
            }

            public void setLessonStatusName(String LessonStatusName) {
                this.LessonStatusName = LessonStatusName;
            }

            public void setStudentName(String StudentName) {
                this.StudentName = StudentName;
            }

            public int getLessonId() {
                return LessonId;
            }

            public int getStudentCourseId() {
                return StudentCourseId;
            }

            public String getSignStatus() {
                return SignStatus;
            }

            public String getSignStatusName() {
                return SignStatusName;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public String getLessonType() {
                return LessonType;
            }

            public String getLessonTypeName() {
                return LessonTypeName;
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

            public String getLessonStatus() {
                return LessonStatus;
            }

            public String getLessonStatusName() {
                return LessonStatusName;
            }

            public String getStudentName() {
                return StudentName;
            }

            public String getPublicClassTitle() {
                return PublicClassTitle;
            }

            public void setPublicClassTitle(String publicClassTitle) {
                PublicClassTitle = publicClassTitle;
            }
        }
    }
}
