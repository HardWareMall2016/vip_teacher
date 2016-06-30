package com.zhan.vip_teacher.utils;

import com.alibaba.fastjson.JSON;
import com.zhan.vip_teacher.base.BaseRequestBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.DBHelper;
import com.zhan.vip_teacher.db.bean.DailyLesson;
import com.zhan.vip_teacher.db.bean.MineResultBean;
import com.zhan.vip_teacher.db.bean.StudentBean;
import com.zhan.vip_teacher.db.bean.WeekLesson;
import com.zhan.vip_teacher.db.bean.WeekPublicLesson;

import org.aisen.orm.extra.Extra;
import java.util.List;

/**
 * Created by WuYue on 2016/3/16.
 */
public class CacheUtility {
    public static Extra getExtra(String url,BaseRequestBean requestBean){
        //只比对具体参数部分，BaseRequestBean中的值始终保持不变
        //每次登录后token都发生变化,UserId 已经在Extra.owner中体现了
        String originToken=requestBean.getToken();
        int originUserId=requestBean.getUserID();
        //去掉差异化的值
        requestBean.setToken("");
        requestBean.setUserID(0);
        String key=url+":"+ JSON.toJSONString(requestBean);
        //还原原始值
        requestBean.setToken(originToken);
        requestBean.setUserID(originUserId);

        String userId=String.valueOf(UserInfo.getCurrentUser().getUserID());
        Extra extra = new Extra(userId, Tools.md5(key));
        return extra;
    }

    public static <T> List<T> findCacheData(String url,BaseRequestBean requestBean, Class<T> responseCls) {
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return null;
        }
        Extra extra = getExtra(url, requestBean);
        List<T> beanList = DBHelper.getCacheSqlite().select(extra, responseCls);

        return beanList;
    }

    public static <T> void addCacheDataList(String url, BaseRequestBean requestBean, List<T> dataList,Class<T> cls){
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return ;
        }
        if(dataList==null){
            return;
        }
        //先清空对应的缓存
        Extra extra = getExtra(url, requestBean);
        DBHelper.getCacheSqlite().deleteAll(extra, cls);

        if(dataList.size()>0){
            DBHelper.getCacheSqlite().insert(extra, dataList);
        }

        /*List<DailyLesson> tempList = DBHelper.getCacheSqlite().select(DailyLesson.class,null,null,null,null,"StartTime asc",null);
        Log.i("CacheUtility", "===================Total Size = " + tempList.size() + "====================");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (DailyLesson lesson:tempList){
            Log.i("CacheUtility","LessonId = "+lesson.getLessonId()+" Start Time = "+dateFormat.format(lesson.getStartTime()));
        }*/
    }

    public static <T> void clearCacheData(Class<T> responseCls){
        DBHelper.getCacheSqlite().deleteAll(null,responseCls);
    }

    public static void clearAllCacheData(){
        DBHelper.getCacheSqlite().deleteAll(null, DailyLesson.class);
        DBHelper.getCacheSqlite().deleteAll(null, WeekLesson.class);
        DBHelper.getCacheSqlite().deleteAll(null, WeekPublicLesson.class);
        DBHelper.getCacheSqlite().deleteAll(null, StudentBean.class);
        DBHelper.getCacheSqlite().deleteAll(null, MineResultBean.class);
    }

    public static <T> T findData(Class<T> responseCls,long lessonId) {
        T data = DBHelper.getCacheSqlite().selectById(null, responseCls, lessonId);
        return data;
    }
}
