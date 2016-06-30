package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MyLessonRequestBean extends BaseRequestBean implements Serializable {
    /**
     * StartTime : 2016-02-14 15:40:48
     * EndTime : 2016-03-14 15:40:48
     * TeacherId : 2294
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private String StartTime;
        private String EndTime;
        private int TeacherId;

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public String getStartTime() {
            return StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public int getTeacherId() {
            return TeacherId;
        }
    }
}
