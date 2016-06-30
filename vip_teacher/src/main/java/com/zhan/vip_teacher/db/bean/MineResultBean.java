package com.zhan.vip_teacher.db.bean;

import org.aisen.orm.annotation.PrimaryKey;

import java.util.UUID;

/**
 * Created by WuYue on 2016/4/15.
 */
public class MineResultBean {

    @PrimaryKey(column = "id")
    String id = UUID.randomUUID().toString();

    private int lessonCount;

    private int notSigninCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public int getNotSigninCount() {
        return notSigninCount;
    }

    public void setNotSigninCount(int notSigninCount) {
        this.notSigninCount = notSigninCount;
    }
}
