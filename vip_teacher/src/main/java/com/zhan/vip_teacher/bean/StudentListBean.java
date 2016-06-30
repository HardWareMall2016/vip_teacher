package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public class StudentListBean extends BaseResponseBean implements Serializable {
    /**
     * CourseType : 2
     * CourseType_ToName : 托福
     * ScheduleCount : 8
     * ProgressStatus : 2
     * ProgressStatus_ToName : 阶段排课
     * CName : 谢欣雨
     * HeadImgUrl : http://192.168.41.60:8012/HeadImg/Student/1887725e-dcdc-435d-b0cf-a4f28eb79a66.jpg
     * TrueName : 付依凤
     * SpecialCount : 96
     * StudyConsultant : 1103
     * ProductId : 2685
     * CourseId : 31235
     * StudentGuid : b5c97ff3-21d5-4b1b-859f-9ce73e11f2c8
     */

    private List<DataEntity> Data;

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity {
        private String CourseType;
        private String CourseType_ToName;
        private int ScheduleCount;
        private int ProgressStatus;
        private String ProgressStatus_ToName;
        private String CName;
        private String HeadImgUrl;
        private String TrueName;
        private int SpecialCount;
        private int StudyConsultant;
        private int ProductId;
        private int CourseId;
        private String StudentGuid;
        private String SubCourseType_ToName ;

        public String getSubCourseType_ToName() {
            return SubCourseType_ToName;
        }

        public void setSubCourseType_ToName(String subCourseType_ToName) {
            SubCourseType_ToName = subCourseType_ToName;
        }


        public void setCourseType(String CourseType) {
            this.CourseType = CourseType;
        }

        public void setCourseType_ToName(String CourseType_ToName) {
            this.CourseType_ToName = CourseType_ToName;
        }

        public void setScheduleCount(int ScheduleCount) {
            this.ScheduleCount = ScheduleCount;
        }

        public void setProgressStatus(int ProgressStatus) {
            this.ProgressStatus = ProgressStatus;
        }

        public void setProgressStatus_ToName(String ProgressStatus_ToName) {
            this.ProgressStatus_ToName = ProgressStatus_ToName;
        }

        public void setCName(String CName) {
            this.CName = CName;
        }

        public void setHeadImgUrl(String HeadImgUrl) {
            this.HeadImgUrl = HeadImgUrl;
        }

        public void setTrueName(String TrueName) {
            this.TrueName = TrueName;
        }

        public void setSpecialCount(int SpecialCount) {
            this.SpecialCount = SpecialCount;
        }

        public void setStudyConsultant(int StudyConsultant) {
            this.StudyConsultant = StudyConsultant;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public void setCourseId(int CourseId) {
            this.CourseId = CourseId;
        }

        public void setStudentGuid(String StudentGuid) {
            this.StudentGuid = StudentGuid;
        }

        public String getCourseType() {
            return CourseType;
        }

        public String getCourseType_ToName() {
            return CourseType_ToName;
        }

        public int getScheduleCount() {
            return ScheduleCount;
        }

        public int getProgressStatus() {
            return ProgressStatus;
        }

        public String getProgressStatus_ToName() {
            return ProgressStatus_ToName;
        }

        public String getCName() {
            return CName;
        }

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public String getTrueName() {
            return TrueName;
        }

        public int getSpecialCount() {
            return SpecialCount;
        }

        public int getStudyConsultant() {
            return StudyConsultant;
        }

        public int getProductId() {
            return ProductId;
        }

        public int getCourseId() {
            return CourseId;
        }

        public String getStudentGuid() {
            return StudentGuid;
        }
    }
}
