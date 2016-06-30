package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MyLessonBean extends BaseResponseBean implements Serializable {
    /**
     * ConfirmCount : 8
     * AleadyCount : 8
     * NoCount : 0
     * ListCourseStateType : [{"Name":"未上课","Id":0},{"Name":"已上课","Id":1},{"Name":"等待确认","Id":2}]
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int ConfirmCount;
        private int AleadyCount;
        private int NoCount;
        /**
         * Name : 未上课
         * Id : 0
         */

        private List<ListCourseStateTypeEntity> ListCourseStateType;

        public void setConfirmCount(int ConfirmCount) {
            this.ConfirmCount = ConfirmCount;
        }

        public void setAleadyCount(int AleadyCount) {
            this.AleadyCount = AleadyCount;
        }

        public void setNoCount(int NoCount) {
            this.NoCount = NoCount;
        }

        public void setListCourseStateType(List<ListCourseStateTypeEntity> ListCourseStateType) {
            this.ListCourseStateType = ListCourseStateType;
        }

        public int getConfirmCount() {
            return ConfirmCount;
        }

        public int getAleadyCount() {
            return AleadyCount;
        }

        public int getNoCount() {
            return NoCount;
        }

        public List<ListCourseStateTypeEntity> getListCourseStateType() {
            return ListCourseStateType;
        }

        public static class ListCourseStateTypeEntity {
            private String Name;
            private int Id;

            public void setName(String Name) {
                this.Name = Name;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public int getId() {
                return Id;
            }
        }
    }
}
