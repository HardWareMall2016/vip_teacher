package com.zhan.vip_teacher.bean;

/**
 * Created by Administrator on 2016/3/2.
 */
public class StudentRequestBean {
    private String Token;

    private DataEntity Data;
    private String UserId;

    public void setToken(String Token) {
        this.Token = Token;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getToken() {
        return Token;
    }

    public DataEntity getData() {
        return Data;
    }

    public String getUserId() {
        return UserId;
    }

    public static class DataEntity {
        private String UserName;
        private String PassWorde;

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public void setPassWorde(String PassWorde) {
            this.PassWorde = PassWorde;
        }

        public String getUserName() {
            return UserName;
        }

        public String getPassWorde() {
            return PassWorde;
        }
    }
}
