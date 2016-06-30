package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.util.List;

/**
 * Created by WuYue on 2016/3/10.
 */
public class TeacherLessonResponse extends BaseResponseBean {

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        /**
         * Id : 1
         * StartTime : 2016-03-10 13:57:52
         * EndTime : 2016-03-10 13:57:52
         * HolidayName : sample string 4
         * HolidayRemark : sample string 5
         * HolidayIsvalid : sample string 6
         */

        private List<ListHolidayInfoEntity> ListHolidayInfo;
        /**
         * RestType : sample string 1
         * RestTypeName : sample string 2
         * StartTime : 2016-03-10 13:57:52
         * EndTime : 2016-03-10 13:57:52
         */

        private List<ListRestTimeEntity> ListRestTime;
        /**
         * RestType : sample string 1
         * RestTypeName : sample string 2
         * StartTime : 2016-03-10 13:57:52
         * EndTime : 2016-03-10 13:57:52
         */

        private List<ListConferenceRestEntity> ListConferenceRest;
        /**
         * LessonId : 1
         * StartTime : 2016-03-10 13:57:52
         * EndTime : 2016-03-10 13:57:52
         * IsOnLine : true
         * IsFirstLesson : true
         * IsFeedBack : true
         * CourseType : sample string 7
         * CourseTypeName : sample string 8
         * CourseSubType : sample string 9
         * CourseSubTypeName : sample string 10
         * StudentName : sample string 11
         * SignStatus : sample string 12
         * SignStatusName : sample string 13
         * LessonStatus : sample string 14
         * LessonStatusName : sample string 15
         */

        private List<ListLessonInfoEntity> ListLessonInfo;
        /**
         * CourseId : 1
         * ClassTitle : sample string 2
         * StartTime : 2016-03-10 13:57:52
         * EndTime : 2016-03-10 13:57:52
         * SignStatus : sample string 3
         */

        private List<ListPublicClassEntity> ListPublicClass;

        public void setListHolidayInfo(List<ListHolidayInfoEntity> ListHolidayInfo) {
            this.ListHolidayInfo = ListHolidayInfo;
        }

        public void setListRestTime(List<ListRestTimeEntity> ListRestTime) {
            this.ListRestTime = ListRestTime;
        }

        public void setListConferenceRest(List<ListConferenceRestEntity> ListConferenceRest) {
            this.ListConferenceRest = ListConferenceRest;
        }

        public void setListLessonInfo(List<ListLessonInfoEntity> ListLessonInfo) {
            this.ListLessonInfo = ListLessonInfo;
        }

        public void setListPublicClass(List<ListPublicClassEntity> ListPublicClass) {
            this.ListPublicClass = ListPublicClass;
        }

        public List<ListHolidayInfoEntity> getListHolidayInfo() {
            return ListHolidayInfo;
        }

        public List<ListRestTimeEntity> getListRestTime() {
            return ListRestTime;
        }

        public List<ListConferenceRestEntity> getListConferenceRest() {
            return ListConferenceRest;
        }

        public List<ListLessonInfoEntity> getListLessonInfo() {
            return ListLessonInfo;
        }

        public List<ListPublicClassEntity> getListPublicClass() {
            return ListPublicClass;
        }

        public static class ListHolidayInfoEntity {
            private int Id;
            private String StartTime;
            private String EndTime;
            private String HolidayName;
            private String HolidayRemark;
            private String HolidayIsvalid;

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public void setHolidayName(String HolidayName) {
                this.HolidayName = HolidayName;
            }

            public void setHolidayRemark(String HolidayRemark) {
                this.HolidayRemark = HolidayRemark;
            }

            public void setHolidayIsvalid(String HolidayIsvalid) {
                this.HolidayIsvalid = HolidayIsvalid;
            }

            public int getId() {
                return Id;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public String getHolidayName() {
                return HolidayName;
            }

            public String getHolidayRemark() {
                return HolidayRemark;
            }

            public String getHolidayIsvalid() {
                return HolidayIsvalid;
            }
        }

        public static class ListRestTimeEntity {
            private String RestType;
            private String RestTypeName;
            private String StartTime;
            private String EndTime;

            public void setRestType(String RestType) {
                this.RestType = RestType;
            }

            public void setRestTypeName(String RestTypeName) {
                this.RestTypeName = RestTypeName;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getRestType() {
                return RestType;
            }

            public String getRestTypeName() {
                return RestTypeName;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }
        }

        public static class ListConferenceRestEntity {
            private String RestType;
            private String RestTypeName;
            private String StartTime;
            private String EndTime;

            public void setRestType(String RestType) {
                this.RestType = RestType;
            }

            public void setRestTypeName(String RestTypeName) {
                this.RestTypeName = RestTypeName;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getRestType() {
                return RestType;
            }

            public String getRestTypeName() {
                return RestTypeName;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }
        }

        public static class ListLessonInfoEntity {
            private int LessonId;
            private String StartTime;
            private String EndTime;
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

            public void setLessonId(int LessonId) {
                this.LessonId = LessonId;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public void setIsOnLine(boolean IsOnLine) {
                this.IsOnLine = IsOnLine;
            }

            public void setIsFirstLesson(boolean IsFirstLesson) {
                this.IsFirstLesson = IsFirstLesson;
            }

            public void setIsFeedBack(boolean IsFeedBack) {
                this.IsFeedBack = IsFeedBack;
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

            public void setSignStatus(String SignStatus) {
                this.SignStatus = SignStatus;
            }

            public void setSignStatusName(String SignStatusName) {
                this.SignStatusName = SignStatusName;
            }

            public void setLessonStatus(String LessonStatus) {
                this.LessonStatus = LessonStatus;
            }

            public void setLessonStatusName(String LessonStatusName) {
                this.LessonStatusName = LessonStatusName;
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

            public boolean isIsOnLine() {
                return IsOnLine;
            }

            public boolean isIsFirstLesson() {
                return IsFirstLesson;
            }

            public boolean isIsFeedBack() {
                return IsFeedBack;
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

            public String getSignStatus() {
                return SignStatus;
            }

            public String getSignStatusName() {
                return SignStatusName;
            }

            public String getLessonStatus() {
                return LessonStatus;
            }

            public String getLessonStatusName() {
                return LessonStatusName;
            }
        }

        public static class ListPublicClassEntity {
            private int CourseId;
            private String ClassTitle;
            private String StartTime;
            private String EndTime;
            private String SignStatus;

            public void setCourseId(int CourseId) {
                this.CourseId = CourseId;
            }

            public void setClassTitle(String ClassTitle) {
                this.ClassTitle = ClassTitle;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public void setSignStatus(String SignStatus) {
                this.SignStatus = SignStatus;
            }

            public int getCourseId() {
                return CourseId;
            }

            public String getClassTitle() {
                return ClassTitle;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public String getSignStatus() {
                return SignStatus;
            }
        }
    }
}
