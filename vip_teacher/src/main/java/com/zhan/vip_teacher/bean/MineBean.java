package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MineBean extends BaseResponseBean implements Serializable {
    /**
     * LessonCount : 3
     * NotSigninCount : 0
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int LessonCount;
        private int NotSigninCount;

        public void setLessonCount(int LessonCount) {
            this.LessonCount = LessonCount;
        }

        public void setNotSigninCount(int NotSigninCount) {
            this.NotSigninCount = NotSigninCount;
        }

        public int getLessonCount() {
            return LessonCount;
        }

        public int getNotSigninCount() {
            return NotSigninCount;
        }
    }
}
