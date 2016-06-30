package com.zhan.vip_teacher.bean;
import com.zhan.vip_teacher.base.BaseResponseBean;

/**
 * Created by WuYue on 2016/3/14.
 */
public class TeacherClassFeedbackResponseBean extends BaseResponseBean{
    /**
     * Data : true
     */

    private boolean Data;

    public void setData(boolean Data) {
        this.Data = Data;
    }

    public boolean isData() {
        return Data;
    }
}
