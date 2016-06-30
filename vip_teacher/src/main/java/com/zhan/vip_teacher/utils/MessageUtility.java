package com.zhan.vip_teacher.utils;

import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.db.DBHelper;
import com.zhan.vip_teacher.db.bean.PushMessage;

import org.aisen.orm.extra.Extra;
import org.aisen.orm.utils.FieldUtils;

import java.util.List;

/**
 * Created by WuYue on 2016/3/16.
 */
public class MessageUtility {
    private static <T> Extra getExtra(Class<T> messageCls){
        String userId=String.valueOf(UserInfo.getCurrentUser().getUserID());
        Extra extra = new Extra(userId, messageCls.getSimpleName());
        return extra;
    }

    public static <T extends PushMessage> List<T> findMessages(Class<T> messageCls,String type) {
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return null;
        }
        Extra extra = getExtra(messageCls);
        String selection = String.format(" %s = ? and %s = ? and %s=? ", FieldUtils.OWNER, FieldUtils.KEY,"MessageTypeCode");
        String[] selectionArgs = new String[]{ extra.getOwner(), extra.getKey() ,type};

        List<T> beanList = DBHelper.getVipTeacherSqlite().select(messageCls, selection, selectionArgs, null, null, "SendTime desc", "200");
        return beanList;
    }

    public static <T extends PushMessage> T findLatestMessage(Class<T> messageCls,String type) {
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return null;
        }
        Extra extra = getExtra(messageCls);
        String selection = String.format(" %s = ? and %s = ? and %s=? ", FieldUtils.OWNER, FieldUtils.KEY,"MessageTypeCode");
        String[] selectionArgs = new String[]{ extra.getOwner(), extra.getKey() ,type};

        List<T> beanList = DBHelper.getVipTeacherSqlite().select(messageCls, selection, selectionArgs, null, null, "SendTime desc", "1");
        if(beanList!=null&&beanList.size()>0){
            return beanList.get(0);
        }
        return null;
    }

    public static <T extends PushMessage> T findLatestMessage(Class<T> messageCls) {
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return null;
        }
        Extra extra = getExtra(messageCls);
        String selection = String.format(" %s = ? and %s = ?", FieldUtils.OWNER, FieldUtils.KEY);
        String[] selectionArgs = new String[]{ extra.getOwner(), extra.getKey()};

        List<T> beanList = DBHelper.getVipTeacherSqlite().select(messageCls, selection, selectionArgs, null, null, "SendTime desc", "1");
        if(beanList!=null&&beanList.size()>0){
            return beanList.get(0);
        }
        return null;
    }


    public static <T extends PushMessage> void insertMessage(T message){
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return ;
        }

        if(message==null){
            return;
        }

        //先删除重复消息
        Extra extra = getExtra(message.getClass());
        DBHelper.getVipTeacherSqlite().deleteById(extra, message.getClass(),message.getMessageID());

        //插入
        DBHelper.getVipTeacherSqlite().insert(extra, message);
    }

    public static <T extends PushMessage> void updateMessage(T message){
        if(UserInfo.getCurrentUser()==null||!UserInfo.getCurrentUser().isLogined()){
            return ;
        }

        if(message==null){
            return;
        }

        Extra extra = getExtra(message.getClass());
        /*String selection = String.format(" %s = ? and %s = ? and %s=? ", FieldUtils.OWNER, FieldUtils.KEY,"MessageID");
        String[] selectionArgs = new String[]{ extra.getOwner(), extra.getKey() ,message.getMessageID()};*/
        DBHelper.getVipTeacherSqlite().update(extra, message);
    }

    public static <T> void clearMessage(Class<T> responseCls){
        DBHelper.getVipTeacherSqlite().deleteAll(null,responseCls);
    }
}
