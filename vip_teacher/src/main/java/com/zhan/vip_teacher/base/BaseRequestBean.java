package com.zhan.vip_teacher.base;

import com.zhan.vip_teacher.bean.UserInfo;

/**
 * Created by WuYue on 2015/12/10.
 */
public class BaseRequestBean {

    public BaseRequestBean(){
        if(UserInfo.getCurrentUser()!=null){
            setUserID(UserInfo.getCurrentUser().getUserID());
            setToken(UserInfo.getCurrentUser().getToken());
        }
    }

    /**
     * Token :
     * Data : {}
     * UserID :
     */

    private String Token;
    private int UserID;

    public void setToken(String Token) {
        this.Token = Token;
    }

    public void setUserID(int UserId) {
        this.UserID = UserId;
    }

    public String getToken() {
        return Token;
    }

    public int getUserID() {
        return UserID;
    }
}
