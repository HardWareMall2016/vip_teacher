package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseRequestBean;

/**
 * Created by WuYue on 2016/3/10.
 */
public class GetFeedbackRequest extends BaseRequestBean {

    /**
     * Data : 1
     */

    private int Data;

    public void setData(int Data) {
        this.Data = Data;
    }

    public int getData() {
        return Data;
    }
}
