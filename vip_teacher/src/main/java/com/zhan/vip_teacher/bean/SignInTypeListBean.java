package com.zhan.vip_teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/17.
 */
public class SignInTypeListBean implements Serializable {
    private int SignInType;
    private String SignInTypeName;

    public void setSignInType(int SignInType) {
        this.SignInType = SignInType;
    }

    public void setSignInTypeName(String SignInTypeName) {
        this.SignInTypeName = SignInTypeName;
    }

    public int getSignInType() {
        return SignInType;
    }

    public String getSignInTypeName() {
        return SignInTypeName;
    }

}
