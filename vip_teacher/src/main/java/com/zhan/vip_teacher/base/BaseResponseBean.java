package com.zhan.vip_teacher.base;

public class BaseResponseBean {
    /**
     * Code : 0
     * Message :
     */
    private int Code;
    private String Message;
    private String ServerTime;

    public void setCode(int Code) {
        this.Code = Code;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }
}
