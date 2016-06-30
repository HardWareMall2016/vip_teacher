package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by WuYue on 2016/3/11.
 */
public class SignInRequestBean extends BaseRequestBean {

    /**
     * TeacherId : 1
     * LessonId : 2
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
        private int LessonId;

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setLessonId(int LessonId) {
            this.LessonId = LessonId;
        }

        public int getTeacherId() {
            return TeacherId;
        }

        public int getLessonId() {
            return LessonId;
        }
    }
}
