package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by WuYue on 2016/3/8.
 */
public class DailyLessonRequestBean extends BaseRequestBean {

    /**
     * TeacherId : 1
     * StartTime : 2016-03-08 13:13:09
     * EndTime : 2016-03-08 13:13:09
     */

    private DataEntity Data;
    /**
     * Data : {"TeacherId":1,"StartTime":"2016-03-08 13:13:09","EndTime":"2016-03-08 13:13:09"}
     * UserID : 2
     */


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
