package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/18.
 */
public class SearchContent implements Serializable {
    private String guid;
    private int courseId;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
