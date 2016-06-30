package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BasePersistObject;

/**
 * Created by Wuyue on 2015/10/14.
 */
public class UserInfo extends BasePersistObject {
    private boolean isLogined=false;

    private int UserID;
    private String WorkNo;
    private String UserName;
    private String Email;
    private String Ename;
    private String TrueName;
    private String qq;
    private String Mobile;
    private String HomeAddr;
    private String Remark;
    private String CreateTime;
    private String GroupOf;
    private String Token;
    private String Sex;
    private String SexName;

    private String HeadImgUrl;

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getSexName() {
        return SexName;
    }

    public void setSexName(String sexName) {
        SexName = sexName;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = headImgUrl;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getWorkNo() {
        return WorkNo;
    }

    public void setWorkNo(String workNo) {
        WorkNo = workNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getHomeAddr() {
        return HomeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        HomeAddr = homeAddr;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getGroupOf() {
        return GroupOf;
    }

    public void setGroupOf(String groupOf) {
        GroupOf = groupOf;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    private static UserInfo sUserInfo=null;

    public static UserInfo getCurrentUser() {
        if(sUserInfo==null){
            sUserInfo=getPersisObject(UserInfo.class);
        }
        return sUserInfo;
    }

    public static void saveLoginUserInfo(UserInfo user) {
        //如果登录的不是同一个用户就清除相关数据
        /*if(getCurrentUser()==null||!getCurrentUser().getUserID().equals(user.getUserID())){
            PushMessageInfo.clearPushMessageInfo();
        }*/
        persisObject(user);
        sUserInfo=user;
    }

    public static void logout() {
        if(sUserInfo!=null){
            sUserInfo.setIsLogined(false);
            persisObject(sUserInfo);
        }
        //PushMessageInfo.clearPushMessageInfo();
        /*deletePersistObject(UserInfo.class);
        sUserInfo=null;*/
    }
}
