package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MyLessonListRequestBean extends BaseRequestBean implements Serializable {
    /**
     * TeacherId : 2294
     * StartTime : 2016-01-14 15:40:43
     * EndTime : 2016-03-14 15:40:43
     * RequestType : 2
     * Index : 1
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
        private int RequestType;
        private int Index;

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setRequestType(int RequestType) {
            this.RequestType = RequestType;
        }

        public void setIndex(int Index) {
            this.Index = Index;
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

        public int getRequestType() {
            return RequestType;
        }

        public int getIndex() {
            return Index;
        }
    }
}
