package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/1.
 */
public class StudentCourseContent implements Serializable {

    private String studentGuid ;
    private String studentName ;

    public String getStudentGuid() {
        return studentGuid;
    }

    public void setStudentGuid(String studentGuid) {
        this.studentGuid = studentGuid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

}
