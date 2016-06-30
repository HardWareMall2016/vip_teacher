package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

/**
 * Created by WuYue on 2016/3/10.
 */
public class GetFeedbackResponse extends BaseResponseBean {
    /**
     * teacherClassFeedbackInfo : {"Id":1,"LessonId":1,"RecordUrl":"sample string 2","ImagesUrl":"sample string 3","OperatingCondition":"sample string 4","HomeworkComments":"sample string 5","LastHomework":"sample string 6","CourseContent":"sample string 7","Homework":"sample string 8","StudentFeedback":"sample string 9","ClassStartTime":"2016-03-15 11:30:17","ClassEndTime":"2016-03-15 11:30:17","StudentCourseState":"sample string 10","CreateBy":1,"CreateTime":"2016-03-15 11:30:17","UpdateBy":1,"UpdateTime":"2016-03-15 11:30:17","CourseId":1}
     * teacherStudentAreaInfo : {"LessonId":1,"CourseId":1,"CourseType":"sample string 1","CourseType_toName":"sample string 2","CourseSubType":"sample string 3","CourseSubType_toName":"sample string 4","StudentGuid":"sample string 5","CName":"sample string 6","StudyConsultant":1,"TrueName":"sample string 7","SpecialCount":1,"GuaranteeScore":"sample string 8","ProductId":1,"StartTime":"2016-03-15 11:30:17","EndTime":"2016-03-15 11:30:17","QQ":"sample string 9"}
     */

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
         * LessonId : 1
         * RecordUrl : sample string 2
         * ImagesUrl : sample string 3
         * OperatingCondition : sample string 4
         * HomeworkComments : sample string 5
         * LastHomework : sample string 6
         * CourseContent : sample string 7
         * Homework : sample string 8
         * StudentFeedback : sample string 9
         * ClassStartTime : 2016-03-15 11:30:17
         * ClassEndTime : 2016-03-15 11:30:17
         * StudentCourseState : sample string 10
         * CreateBy : 1
         * CreateTime : 2016-03-15 11:30:17
         * UpdateBy : 1
         * UpdateTime : 2016-03-15 11:30:17
         * CourseId : 1
         */

        private TeacherClassFeedbackInfoEntity teacherClassFeedbackInfo;
        /**
         * LessonId : 1
         * CourseId : 1
         * CourseType : sample string 1
         * CourseType_toName : sample string 2
         * CourseSubType : sample string 3
         * CourseSubType_toName : sample string 4
         * StudentGuid : sample string 5
         * CName : sample string 6
         * StudyConsultant : 1
         * TrueName : sample string 7
         * SpecialCount : 1
         * GuaranteeScore : sample string 8
         * ProductId : 1
         * StartTime : 2016-03-15 11:30:17
         * EndTime : 2016-03-15 11:30:17
         * QQ : sample string 9
         */

        private TeacherStudentAreaInfoEntity teacherStudentAreaInfo;

        public void setTeacherClassFeedbackInfo(TeacherClassFeedbackInfoEntity teacherClassFeedbackInfo) {
            this.teacherClassFeedbackInfo = teacherClassFeedbackInfo;
        }

        public void setTeacherStudentAreaInfo(TeacherStudentAreaInfoEntity teacherStudentAreaInfo) {
            this.teacherStudentAreaInfo = teacherStudentAreaInfo;
        }

        public TeacherClassFeedbackInfoEntity getTeacherClassFeedbackInfo() {
            return teacherClassFeedbackInfo;
        }

        public TeacherStudentAreaInfoEntity getTeacherStudentAreaInfo() {
            return teacherStudentAreaInfo;
        }

        public static class TeacherClassFeedbackInfoEntity {
            private int Id;
            private int LessonId;
            private String RecordUrl;
            private String ImagesUrl;
            private String OperatingCondition;
            private String HomeworkComments;
            private String LastHomework;
            private String CourseContent;
            private String Homework;
            private String StudentFeedback;
            private String ClassStartTime;
            private String ClassEndTime;
            private String StudentCourseState;
            private int CreateBy;
            private String CreateTime;
            private int UpdateBy;
            private String UpdateTime;
            private int CourseId;

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setLessonId(int LessonId) {
                this.LessonId = LessonId;
            }

            public void setRecordUrl(String RecordUrl) {
                this.RecordUrl = RecordUrl;
            }

            public void setImagesUrl(String ImagesUrl) {
                this.ImagesUrl = ImagesUrl;
            }

            public void setOperatingCondition(String OperatingCondition) {
                this.OperatingCondition = OperatingCondition;
            }

            public void setHomeworkComments(String HomeworkComments) {
                this.HomeworkComments = HomeworkComments;
            }

            public void setLastHomework(String LastHomework) {
                this.LastHomework = LastHomework;
            }

            public void setCourseContent(String CourseContent) {
                this.CourseContent = CourseContent;
            }

            public void setHomework(String Homework) {
                this.Homework = Homework;
            }

            public void setStudentFeedback(String StudentFeedback) {
                this.StudentFeedback = StudentFeedback;
            }

            public void setClassStartTime(String ClassStartTime) {
                this.ClassStartTime = ClassStartTime;
            }

            public void setClassEndTime(String ClassEndTime) {
                this.ClassEndTime = ClassEndTime;
            }

            public void setStudentCourseState(String StudentCourseState) {
                this.StudentCourseState = StudentCourseState;
            }

            public void setCreateBy(int CreateBy) {
                this.CreateBy = CreateBy;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public void setUpdateBy(int UpdateBy) {
                this.UpdateBy = UpdateBy;
            }

            public void setUpdateTime(String UpdateTime) {
                this.UpdateTime = UpdateTime;
            }

            public void setCourseId(int CourseId) {
                this.CourseId = CourseId;
            }

            public int getId() {
                return Id;
            }

            public int getLessonId() {
                return LessonId;
            }

            public String getRecordUrl() {
                return RecordUrl;
            }

            public String getImagesUrl() {
                return ImagesUrl;
            }

            public String getOperatingCondition() {
                return OperatingCondition;
            }

            public String getHomeworkComments() {
                return HomeworkComments;
            }

            public String getLastHomework() {
                return LastHomework;
            }

            public String getCourseContent() {
                return CourseContent;
            }

            public String getHomework() {
                return Homework;
            }

            public String getStudentFeedback() {
                return StudentFeedback;
            }

            public String getClassStartTime() {
                return ClassStartTime;
            }

            public String getClassEndTime() {
                return ClassEndTime;
            }

            public String getStudentCourseState() {
                return StudentCourseState;
            }

            public int getCreateBy() {
                return CreateBy;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public int getUpdateBy() {
                return UpdateBy;
            }

            public String getUpdateTime() {
                return UpdateTime;
            }

            public int getCourseId() {
                return CourseId;
            }
        }

        public static class TeacherStudentAreaInfoEntity {
            private int LessonId;
            private int CourseId;
            private String CourseType;
            private String CourseType_toName;
            private String CourseSubType;
            private String CourseSubType_toName;
            private String StudentGuid;
            private String CName;
            private int StudyConsultant;
            private String TrueName;
            private int SpecialCount;
            private String GuaranteeScore;
            private int ProductId;
            private String StartTime;
            private String EndTime;
            private String QQ;
            private String LastHomework;

            public void setLessonId(int LessonId) {
                this.LessonId = LessonId;
            }

            public void setCourseId(int CourseId) {
                this.CourseId = CourseId;
            }

            public void setCourseType(String CourseType) {
                this.CourseType = CourseType;
            }

            public void setCourseType_toName(String CourseType_toName) {
                this.CourseType_toName = CourseType_toName;
            }

            public void setCourseSubType(String CourseSubType) {
                this.CourseSubType = CourseSubType;
            }

            public void setCourseSubType_toName(String CourseSubType_toName) {
                this.CourseSubType_toName = CourseSubType_toName;
            }

            public void setStudentGuid(String StudentGuid) {
                this.StudentGuid = StudentGuid;
            }

            public void setCName(String CName) {
                this.CName = CName;
            }

            public void setStudyConsultant(int StudyConsultant) {
                this.StudyConsultant = StudyConsultant;
            }

            public void setTrueName(String TrueName) {
                this.TrueName = TrueName;
            }

            public void setSpecialCount(int SpecialCount) {
                this.SpecialCount = SpecialCount;
            }

            public void setGuaranteeScore(String GuaranteeScore) {
                this.GuaranteeScore = GuaranteeScore;
            }

            public void setProductId(int ProductId) {
                this.ProductId = ProductId;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public void setQQ(String QQ) {
                this.QQ = QQ;
            }

            public int getLessonId() {
                return LessonId;
            }

            public int getCourseId() {
                return CourseId;
            }

            public String getCourseType() {
                return CourseType;
            }

            public String getCourseType_toName() {
                return CourseType_toName;
            }

            public String getCourseSubType() {
                return CourseSubType;
            }

            public String getCourseSubType_toName() {
                return CourseSubType_toName;
            }

            public String getStudentGuid() {
                return StudentGuid;
            }

            public String getCName() {
                return CName;
            }

            public int getStudyConsultant() {
                return StudyConsultant;
            }

            public String getTrueName() {
                return TrueName;
            }

            public int getSpecialCount() {
                return SpecialCount;
            }

            public String getGuaranteeScore() {
                return GuaranteeScore;
            }

            public int getProductId() {
                return ProductId;
            }

            public String getStartTime() {
                return StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public String getQQ() {
                return QQ;
            }

            public String getLastHomework() {
                return LastHomework;
            }

            public void setLastHomework(String lastHomework) {
                LastHomework = lastHomework;
            }
        }
    }
}
