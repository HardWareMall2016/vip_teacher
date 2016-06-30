package com.zhan.vip_teacher.bean;
import com.zhan.vip_teacher.base.BaseResponseBean;

/**
 * Created by WuYue on 2016/3/10.
 */
public class TeacherInfoResponseBean extends BaseResponseBean {
    /**
     * UserID : 1
     * WorkNo : sample string 2
     * UserName : sample string 3
     * PassWord : sample string 4
     * Email : sample string 5
     * Ename : sample string 6
     * TrueName : sample string 7
     * qq : sample string 8
     * Mobile : sample string 9
     * HomeAddr : sample string 10
     * Remark : sample string 11
     * CreateTime : 2016-04-13 13:19:56
     * HeadImgUrl : sample string 13
     * GroupOf : sample string 14
     * Token : sample string 15
     * Sex : sample string 16
     * SexName : sample string 17
     */

    private DataEntity Data;

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public DataEntity getData() {
        return Data;
    }

    public static class DataEntity {
        private int UserID;
        private String WorkNo;
        private String UserName;
        private String PassWord;
        private String Email;
        private String Ename;
        private String TrueName;
        private String qq;
        private String Mobile;
        private String HomeAddr;
        private String Remark;
        private String CreateTime;
        private String HeadImgUrl;
        private String GroupOf;
        private String Token;
        private String Sex;
        private String SexName;

        public void setUserID(int UserID) {
            this.UserID = UserID;
        }

        public void setWorkNo(String WorkNo) {
            this.WorkNo = WorkNo;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public void setPassWord(String PassWord) {
            this.PassWord = PassWord;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public void setEname(String Ename) {
            this.Ename = Ename;
        }

        public void setTrueName(String TrueName) {
            this.TrueName = TrueName;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public void setHomeAddr(String HomeAddr) {
            this.HomeAddr = HomeAddr;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setHeadImgUrl(String HeadImgUrl) {
            this.HeadImgUrl = HeadImgUrl;
        }

        public void setGroupOf(String GroupOf) {
            this.GroupOf = GroupOf;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public void setSexName(String SexName) {
            this.SexName = SexName;
        }

        public int getUserID() {
            return UserID;
        }

        public String getWorkNo() {
            return WorkNo;
        }

        public String getUserName() {
            return UserName;
        }

        public String getPassWord() {
            return PassWord;
        }

        public String getEmail() {
            return Email;
        }

        public String getEname() {
            return Ename;
        }

        public String getTrueName() {
            return TrueName;
        }

        public String getQq() {
            return qq;
        }

        public String getMobile() {
            return Mobile;
        }

        public String getHomeAddr() {
            return HomeAddr;
        }

        public String getRemark() {
            return Remark;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public String getHeadImgUrl() {
            return HeadImgUrl;
        }

        public String getGroupOf() {
            return GroupOf;
        }

        public String getToken() {
            return Token;
        }

        public String getSex() {
            return Sex;
        }

        public String getSexName() {
            return SexName;
        }
    }
}
