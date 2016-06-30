package com.zhan.vip_teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class SignatureListBeans implements Serializable {
    private List<SignatureListBean> comments;

    public SignatureListBeans(List<SignatureListBean> comments) {
        this.comments = comments;
    }

    public List<SignatureListBean> getComments() {
        return comments;
    }
}
