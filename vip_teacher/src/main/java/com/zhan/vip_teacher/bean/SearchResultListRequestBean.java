package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/7.
 */
public class SearchResultListRequestBean extends BaseRequestBean implements Serializable {
    /**
     * studentCEName : 刘兴云
     * userID : 2132
     */

    private DataEntity data;
    /**
     * data : {"studentCEName":"刘兴云","userID":2132}
     * token : e6fee96e-11f7-4dd3-a322-93f50cc078bd
     * userID : 2132
     */

    private String token;
    private int userID;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public DataEntity getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public int getUserID() {
        return userID;
    }

    public static class DataEntity {
        private String studentCEName;
        private int userID;

        public void setStudentCEName(String studentCEName) {
            this.studentCEName = studentCEName;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getStudentCEName() {
            return studentCEName;
        }

        public int getUserID() {
            return userID;
        }
    }
}
