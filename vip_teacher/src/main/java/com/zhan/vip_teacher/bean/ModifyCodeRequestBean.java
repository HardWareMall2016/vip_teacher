package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by Administrator on 2016/3/8.
 */
public class ModifyCodeRequestBean extends BaseRequestBean {
    /**
     * UserID : 1
     * OldPassWord : sample string 2
     * NewPassWord : sample string 3
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
        private String OldPassWord;
        private String NewPassWord;

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public void setOldPassWord(String OldPassWord) {
            this.OldPassWord = OldPassWord;
        }

        public void setNewPassWord(String NewPassWord) {
            this.NewPassWord = NewPassWord;
        }

        public int getUserID() {
            return UserID;
        }

        public String getOldPassWord() {
            return OldPassWord;
        }

        public String getNewPassWord() {
            return NewPassWord;
        }
    }
}
