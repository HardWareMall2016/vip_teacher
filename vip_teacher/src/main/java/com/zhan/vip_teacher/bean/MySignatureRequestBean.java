package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureRequestBean extends BaseRequestBean implements Serializable {
    /**
     * TeacherId : 1
     * StartTime : 2016-03-17 11:43:42
     * EndTime : 2016-03-17 11:43:42
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int TeacherId;
        private String StartTime;
        private String EndTime;

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public int getTeacherId() {
            return TeacherId;
        }

        public String getStartTime() {
            return StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }
    }
}
