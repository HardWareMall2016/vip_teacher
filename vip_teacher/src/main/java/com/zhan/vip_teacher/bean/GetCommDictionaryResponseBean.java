package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

/**
 * Created by WuYue on 2016/3/11.
 */
public class GetCommDictionaryResponseBean extends BaseResponseBean{

    private CommDictionary Data;

    public void setData(CommDictionary Data) {
        this.Data = Data;
    }

    public CommDictionary getData() {
        return Data;
    }
}
