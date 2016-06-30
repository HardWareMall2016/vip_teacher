package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureListRequestBean extends BaseRequestBean implements Serializable {
    /**
     * TeacherId : 1
     * SignStatus : sample string 2
     * StartTime : 2016-03-17 13:49:04
     * EndTime : 2016-03-17 13:49:04
     * PagedIndex : 3
     * PagedSize : 4
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
        private String SignStatus;
        private String StartTime;
        private String EndTime;
        private int PagedIndex;
        private int PagedSize;

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setSignStatus(String SignStatus) {
            this.SignStatus = SignStatus;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setPagedIndex(int PagedIndex) {
            this.PagedIndex = PagedIndex;
        }

        public void setPagedSize(int PagedSize) {
            this.PagedSize = PagedSize;
        }

        public int getTeacherId() {
            return TeacherId;
        }

        public String getSignStatus() {
            return SignStatus;
        }

        public String getStartTime() {
            return StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public int getPagedIndex() {
            return PagedIndex;
        }

        public int getPagedSize() {
            return PagedSize;
        }
    }
}
