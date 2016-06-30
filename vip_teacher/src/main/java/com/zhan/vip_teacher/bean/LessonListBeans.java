package com.zhan.vip_teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class LessonListBeans implements Serializable {
    private List<LessonListBean> comments;

    public LessonListBeans(List<LessonListBean> comments) {
        this.comments = comments;
    }

    public List<LessonListBean> getComments() {
        return comments;
    }
}
