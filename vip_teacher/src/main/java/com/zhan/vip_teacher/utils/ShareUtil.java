package com.zhan.vip_teacher.utils;

/**
 * Created by Administrator on 2016/3/8.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.zhan.framework.common.context.GlobalContext;

public class ShareUtil {
    private static final String PREFERENCE_NAME = "VIPTEACHER";
    public static final String REMIND_SETTING ="remind_setting";
    public static final String REMIND_SETTING_VOICE ="remind_setting_voice";
    public static final String REMIND_SETTING_POPUP ="remind_setting_popup";
    public static final String REMIND_SETTING_SHOCK ="remind_setting_shock";

    //version版本  判断引导页是否显示
    public static final String VERSION = "version";

    //boolean 开关
    public static final String VALUE_TURN_OFF ="0";
    public static final String VALUE_TURN_ON ="1";

    //极光推送别名
    public static final String JPUSH_ALIAS = "JPush_Alias";

    //我的学生引导页
    public static final String STUDENT_PAGE = "student";
    //我的课次引导页
    public static final String MYLESSON_PAGE = "mylesson";
    //我的签到引导页
    public static final String MYSIGNATURE_PAGE = "mysignature";
    //周课表引导页
    public static final String WEEKCOURSE_PAGE = "weekcourse";
    //周课表引导页
    public static final String DAILY_COURSE_PAGE = "dailycourse";


    private static SharedPreferences getInstance() {
        return GlobalContext._context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor edit = getInstance().edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static String getStringValue(String key) {
        return getInstance().getString(key, null);
    }
    public static String getStringValue(String key,String def) {
        return getInstance().getString(key, def);
    }

    public static int getIntValue(String key,int def) {
        return getInstance().getInt(key, def);
    }

    public static void setIntValue(String key,int value) {
        SharedPreferences.Editor edit = getInstance().edit();
        edit.putInt(key, value);
        edit.commit();
    }
}
