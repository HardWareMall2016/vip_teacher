package com.zhan.vip_teacher.bean;

/**
 * Created by WuYue on 2016/4/19.
 */
public class EventSignInfoBean {
    int teacherId;
    int lessId;
    int success;
    String message;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getLessId() {
        return lessId;
    }

    public void setLessId(int lessId) {
        this.lessId = lessId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
