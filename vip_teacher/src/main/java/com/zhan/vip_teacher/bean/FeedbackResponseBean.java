package com.zhan.vip_teacher.bean;

/**
 * Created by WuYue on 2016/4/6.
 */
public class FeedbackResponseBean {
    private int retcode;
    private String retmsg;
    private Object data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
