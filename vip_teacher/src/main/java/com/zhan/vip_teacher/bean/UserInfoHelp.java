package com.zhan.vip_teacher.bean;

/**
 * Created by Administrator on 2016/3/7.
 */
public class UserInfoHelp {
    public static void updateUserInfo(UserInfoBean bean, UserInfo user) {
        user.setUserID(bean.getData().getUserID());
        user.setWorkNo(bean.getData().getWorkNo());
        user.setUserName(bean.getData().getUserName());
        user.setEmail(bean.getData().getEmail());
        user.setEname(bean.getData().getEname());
        user.setTrueName(bean.getData().getTrueName());
        user.setQq(bean.getData().getQq());
        user.setMobile(bean.getData().getMobile());
        user.setHomeAddr(bean.getData().getHomeAddr());
        user.setRemark(bean.getData().getRemark());
        user.setCreateTime(bean.getData().getCreateTime());
        user.setGroupOf(bean.getData().getGroupOf());
        user.setToken(bean.getData().getToken());
        user.setSex(bean.getData().getSex());
        user.setSexName(bean.getData().getSexName());
        user.setHeadImgUrl(bean.getData().getHeadImgUrl());

        user.setIsLogined(true);

        UserInfo.saveLoginUserInfo(user);
    }

    public static void updateTeacherInfo(TeacherInfoResponseBean bean) {
        UserInfo user=UserInfo.getCurrentUser();
        user.setWorkNo(bean.getData().getWorkNo());
        user.setUserName(bean.getData().getUserName());
        user.setEmail(bean.getData().getEmail());
        user.setEname(bean.getData().getEname());
        user.setTrueName(bean.getData().getTrueName());
        user.setQq(bean.getData().getQq());
        user.setMobile(bean.getData().getMobile());
        user.setHomeAddr(bean.getData().getHomeAddr());
        user.setRemark(bean.getData().getRemark());
        user.setCreateTime(bean.getData().getCreateTime());
        user.setGroupOf(bean.getData().getGroupOf());
        user.setSex(bean.getData().getSex());
        user.setSexName(bean.getData().getSexName());
        user.setHeadImgUrl(bean.getData().getHeadImgUrl());

        user.setIsLogined(true);

        UserInfo.saveLoginUserInfo(user);
    }
}
