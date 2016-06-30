package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MineRequestBean extends BaseRequestBean implements Serializable {
    /**
     * UserID : 1
     * QueryDate : 2016-04-06 11:50:55
     * EndDate : 2016-04-06 11:50:55
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
        private String QueryDate;
        private String EndDate;

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public void setQueryDate(String QueryDate) {
            this.QueryDate = QueryDate;
        }

        public void setEndDate(String EndDate) {
            this.EndDate = EndDate;
        }

        public int getUserID() {
            return UserID;
        }

        public String getQueryDate() {
            return QueryDate;
        }

        public String getEndDate() {
            return EndDate;
        }
    }
}
