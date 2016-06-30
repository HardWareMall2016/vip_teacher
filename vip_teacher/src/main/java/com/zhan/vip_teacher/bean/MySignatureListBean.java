package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureListBean extends BaseResponseBean implements Serializable {
    /**
     * TeacherId : 2294
     * LessonId : 762452
     * SignInStatus :
     * TeacherNo : 999999
     * TeacherName : 雅思老师排课
     * TeacherEName : YASI
     * CourseType : 雅思
     * CourseSubType : 听力
     * StudentName : 接口079
     * StudentGuid : 3cedb968-252d-4ec7-b5a7-f6bf1ff6531b
     * ConsultantName : 唐颖
     * ConsultantId : 1904
     * SignTime : null
     * LessonStartTime : 2016-03-30 13:00:00
     * LessonEndTime : 2016-03-30 14:30:00
     */

    private List<DataEntity> Data;

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity {
        private int TeacherId;
        private int LessonId;
        private String SignInStatus;
        private String TeacherNo;
        private String TeacherName;
        private String TeacherEName;
        private String CourseType;
        private String CourseSubType;
        private String StudentName;
        private String StudentGuid;
        private String ConsultantName;
        private int ConsultantId;
        private Object SignTime;
        private String LessonStartTime;
        private String LessonEndTime;
        private int CourseId ;

        public void setCourseId(int CourseId){
            this.CourseId = CourseId ;
        }

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setLessonId(int LessonId) {
            this.LessonId = LessonId;
        }

        public void setSignInStatus(String SignInStatus) {
            this.SignInStatus = SignInStatus;
        }

        public void setTeacherNo(String TeacherNo) {
            this.TeacherNo = TeacherNo;
        }

        public void setTeacherName(String TeacherName) {
            this.TeacherName = TeacherName;
        }

        public void setTeacherEName(String TeacherEName) {
            this.TeacherEName = TeacherEName;
        }

        public void setCourseType(String CourseType) {
            this.CourseType = CourseType;
        }

        public void setCourseSubType(String CourseSubType) {
            this.CourseSubType = CourseSubType;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public void setStudentGuid(String StudentGuid) {
            this.StudentGuid = StudentGuid;
        }

        public void setConsultantName(String ConsultantName) {
            this.ConsultantName = ConsultantName;
        }

        public void setConsultantId(int ConsultantId) {
            this.ConsultantId = ConsultantId;
        }

        public void setSignTime(Object SignTime) {
            this.SignTime = SignTime;
        }

        public void setLessonStartTime(String LessonStartTime) {
            this.LessonStartTime = LessonStartTime;
        }

        public void setLessonEndTime(String LessonEndTime) {
            this.LessonEndTime = LessonEndTime;
        }

        public int getTeacherId() {
            return TeacherId;
        }

        public int getLessonId() {
            return LessonId;
        }

        public String getSignInStatus() {
            return SignInStatus;
        }

        public String getTeacherNo() {
            return TeacherNo;
        }

        public String getTeacherName() {
            return TeacherName;
        }

        public String getTeacherEName() {
            return TeacherEName;
        }

        public String getCourseType() {
            return CourseType;
        }

        public String getCourseSubType() {
            return CourseSubType;
        }

        public String getStudentName() {
            return StudentName;
        }

        public String getStudentGuid() {
            return StudentGuid;
        }

        public String getConsultantName() {
            return ConsultantName;
        }

        public int getConsultantId() {
            return ConsultantId;
        }

        public Object getSignTime() {
            return SignTime;
        }

        public String getLessonStartTime() {
            return LessonStartTime;
        }

        public String getLessonEndTime() {
            return LessonEndTime;
        }

        public int getCourseId(){
            return CourseId ;
        }
    }
}
