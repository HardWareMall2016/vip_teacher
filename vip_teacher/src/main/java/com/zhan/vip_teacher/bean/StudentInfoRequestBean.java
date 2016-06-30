package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/11.
 */
public class StudentInfoRequestBean extends BaseRequestBean implements Serializable {
    /**
     * Guid : sample string 1
     * CourseId : 2
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private String Guid;
        private int CourseId;

        public void setGuid(String Guid) {
            this.Guid = Guid;
        }

        public void setCourseId(int CourseId) {
            this.CourseId = CourseId;
        }

        public String getGuid() {
            return Guid;
        }

        public int getCourseId() {
            return CourseId;
        }
    }
}
