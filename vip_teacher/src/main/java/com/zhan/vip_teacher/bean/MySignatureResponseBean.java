package com.zhan.vip_teacher.bean;

import com.zhan.vip_teacher.base.BaseResponseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class MySignatureResponseBean extends BaseResponseBean implements Serializable {
    /**
     * OnTime : 1
     * BeLate : 1
     * Caveat : null
     * ForgetSignIn : null
     * SignInTotal : 3
     * SignInTypeModelList : [{"SignInType":781,"SignInTypeName":"准时"},{"SignInType":782,"SignInTypeName":"警告"},{"SignInType":783,"SignInTypeName":"迟到"},{"SignInType":784,"SignInTypeName":"忘记签到"}]
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int OnTime;
        private int BeLate;
        private int Caveat;
        private int ForgetSignIn;
        private int SignInTotal;
        /**
         * SignInType : 781
         * SignInTypeName : 准时
         */

        private List<SignInTypeModelListEntity> SignInTypeModelList;

        public void setOnTime(int OnTime) {
            this.OnTime = OnTime;
        }

        public void setBeLate(int BeLate) {
            this.BeLate = BeLate;
        }

        public void setCaveat(int Caveat) {
            this.Caveat = Caveat;
        }

        public void setForgetSignIn(int ForgetSignIn) {
            this.ForgetSignIn = ForgetSignIn;
        }

        public void setSignInTotal(int SignInTotal) {
            this.SignInTotal = SignInTotal;
        }

        public void setSignInTypeModelList(List<SignInTypeModelListEntity> SignInTypeModelList) {
            this.SignInTypeModelList = SignInTypeModelList;
        }

        public int getOnTime() {
            return OnTime;
        }

        public int getBeLate() {
            return BeLate;
        }

        public int getCaveat() {
            return Caveat;
        }

        public int getForgetSignIn() {
            return ForgetSignIn;
        }

        public int getSignInTotal() {
            return SignInTotal;
        }

        public List<SignInTypeModelListEntity> getSignInTypeModelList() {
            return SignInTypeModelList;
        }

        public static class SignInTypeModelListEntity {
            private int SignInType;
            private String SignInTypeName;

            public void setSignInType(int SignInType) {
                this.SignInType = SignInType;
            }

            public void setSignInTypeName(String SignInTypeName) {
                this.SignInTypeName = SignInTypeName;
            }

            public int getSignInType() {
                return SignInType;
            }

            public String getSignInTypeName() {
                return SignInTypeName;
            }
        }
    }
}
