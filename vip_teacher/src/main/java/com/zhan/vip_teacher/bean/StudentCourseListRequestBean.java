package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/11.
 */
public class StudentCourseListRequestBean extends BaseRequestBean implements Serializable {
    /**
     * StudentGuid : 3cedb968-252d-4ec7-b5a7-f6bf1ff6531b
     * TeacherId : 2294
     * Type : 0
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private String StudentGuid;
        private int TeacherId;
        private int Type;

        public void setStudentGuid(String StudentGuid) {
            this.StudentGuid = StudentGuid;
        }

        public void setTeacherId(int TeacherId) {
            this.TeacherId = TeacherId;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getStudentGuid() {
            return StudentGuid;
        }

        public int getTeacherId() {
            return TeacherId;
        }

        public int getType() {
            return Type;
        }
    }
}
