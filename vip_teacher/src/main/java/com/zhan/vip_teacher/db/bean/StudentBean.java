package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/8.
 */
public class StudentBean implements Serializable {

    //唯一主键
    @PrimaryKey(column = "id")
    String id = UUID.randomUUID().toString();

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

    public String getSubCourseType_ToName() {
        return SubCourseType_ToName;
    }

    public void setSubCourseType_ToName(String subCourseType_ToName) {
        SubCourseType_ToName = subCourseType_ToName;
    }

    private String SubCourseType_ToName;

    private String StudentGuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
