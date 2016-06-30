package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class StudentCourseListBean extends BaseResponseBean implements Serializable {
    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        /**
         * StartTime : 2016-03-30 13:00:00
         * EndTime : 2016-03-30 14:30:00
         * LessonState : 0
         * LessonStateName : 未上课
         * TeacherName : 雅思老师排课
         * CourseType : 3
         * CourseTypeName : 雅思
         * CourseSubType : 254
         * CourseSubTypeName : 听力
         */

        private List<ListClassEntity> ListClass;

        public void setListClass(List<ListClassEntity> ListClass) {
            this.ListClass = ListClass;
        }

        public List<ListClassEntity> getListClass() {
            return ListClass;
        }

        public static class ListClassEntity {
            private String StartTime;
            private String EndTime;
            private String LessonState;
            private String LessonStateName;
            private String TeacherName;
            private String CourseType;
            private String CourseTypeName;
            private String CourseSubType;
            private String CourseSubTypeName;
            private long LessonId ;

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
            public long getLessonId() {
                return LessonId;
            }
            public void setLessonId(long lessonId) {
                LessonId = lessonId;
            }

        }
    }
}

