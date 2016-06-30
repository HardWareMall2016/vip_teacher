package com.zhan.vip_teacher.bean;

/**
 * Created by WuYue on 2016/4/6.
 */
public class FeedbackRequestBean {
    private String appid;
    private String subsystem;
    private String content;
    private String checkValue;
    private String checktime;
    private String msgkey;
    private String numberdesc;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public String getMsgkey() {
        return msgkey;
    }

    public void setMsgkey(String msgkey) {
        this.msgkey = msgkey;
    }

    public String getNumberdesc() {
        return numberdesc;
    }

    public void setNumberdesc(String numberdesc) {
        this.numberdesc = numberdesc;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }
}
