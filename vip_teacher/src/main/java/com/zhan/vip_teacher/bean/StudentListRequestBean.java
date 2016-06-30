package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/8.
 */
public class StudentListRequestBean extends BaseRequestBean implements Serializable {
    /**
     * UserID : 1
     * ProgressStatusSection : 2
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int UserID;
        private String ProgressStatusSection;
        private String StudentCEName ;

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public void setProgressStatusSection(String ProgressStatusSection) {
            this.ProgressStatusSection = ProgressStatusSection;
        }

        public void setStudentCEName(String StudentCEName){
            this.StudentCEName = StudentCEName ;
        }

        public int getUserID() {
            return UserID;
        }

        public String getProgressStatusSection() {
            return ProgressStatusSection;
        }

        public String getStudentCEName(){
            return StudentCEName ;
        }
    }
}
