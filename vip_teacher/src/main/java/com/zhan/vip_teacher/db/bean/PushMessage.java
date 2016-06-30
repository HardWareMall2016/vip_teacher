package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

/**
 * Created by WuYue on 2016/3/29.
 */
public class PushMessage {

    @PrimaryKey(column = "MessageID")
    private String MessageID;

    private boolean HasRead;
    private long SendTime;//消息的发送时间
    private long ReceiveTime;//消息的接收时间
    private String MessageTypeCode;
    private String MessageTypeName;
    private String Content;
    private String Title;

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String messageID) {
        MessageID = messageID;
    }

    public boolean isHasRead() {
        return HasRead;
    }

    public void setHasRead(boolean hasRead) {
        HasRead = hasRead;
    }

    public long getSendTime() {
        return SendTime;
    }

    public void setSendTime(long sendTime) {
        SendTime = sendTime;
    }

    public long getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        ReceiveTime = receiveTime;
    }

    public String getMessageTypeCode() {
        return MessageTypeCode;
    }

    public void setMessageTypeCode(String messageTypeCode) {
        MessageTypeCode = messageTypeCode;
    }

    public String getMessageTypeName() {
        return MessageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        MessageTypeName = messageTypeName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
