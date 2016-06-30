package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by WuYue on 2016/3/10.
 */
public class TeacherLessonRequest extends BaseRequestBean {

    /**
     * TeacherId : 1
     * StartTime : 2016-03-10 13:57:52
     * EndTime : 2016-03-10 13:57:52
     * Groupof : 1
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
        private int Groupof;

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setGroupof(int Groupof) {
            this.Groupof = Groupof;
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

        public int getGroupof() {
            return Groupof;
        }
    }
}
