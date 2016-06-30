package com.zhan.vip_teacher.receiver.bean;

import java.io.Serializable;

/**
 * Created by WuYue on 2016/1/12.
 */
public class MessageBean implements Serializable {

    //上课提醒
    public final static String MSG_TYPE_NOTIFY_FOR_CLASS="3";

    /**
     * Status : 0
     * MessageID : 759417_1
     * CreateTime : 2016-01-12 13:57:07
     * MessageTypeCode : 3
     * MessageTypeName : 上课提醒
     * Content :  明天下午(01月12日),15:00 托福-口语 记得上课哟!!
     * Title : 上课提醒
     * BusId : BusId
     */

    private String Status;
    private String MessageID;
    private String CreateTime;//这是北京时间
    private String MessageTypeCode;
    private String MessageTypeName;
    private String Content;
    private String Title;
    private String BusId;

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setMessageID(String MessageID) {
        this.MessageID = MessageID;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public void setMessageTypeCode(String MessageTypeCode) {
        this.MessageTypeCode = MessageTypeCode;
    }

    public void setMessageTypeName(String MessageTypeName) {
        this.MessageTypeName = MessageTypeName;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setBusId(String BusId) {
        this.BusId = BusId;
    }

    public String getStatus() {
        return Status;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getMessageTypeCode() {
        return MessageTypeCode;
    }

    public String getMessageTypeName() {
        return MessageTypeName;
    }

    public String getContent() {
        return Content;
    }

    public String getTitle() {
        return Title;
    }

    public String getBusId() {
        return BusId;
    }
}
