package com.zhan.vip_teacher.bean;

/**
 * Created by Administrator on 2016/3/30.
 */
public class UpgradeBean {
    /**
     * status : 0
     * message :
     * datas : {"name":"小站教师平台","version":1,"type":"android","url":"http://git.zhan.com/appswitch/appswitch/raw/master/vip_teacher.apk","introduce":"","isUp":0,"versionLarge":1,"versionSmall":0.1,"updatedAt":1459909147,"createdAt":1459308559,"forcedUpdate":0}
     */

    private int status;
    private String message;
    /**
     * name : 小站教师平台
     * version : 1
     * type : android
     * url : http://git.zhan.com/appswitch/appswitch/raw/master/vip_teacher.apk
     * introduce :
     * isUp : 0
     * versionLarge : 1
     * versionSmall : 0.1
     * updatedAt : 1459909147
     * createdAt : 1459308559
     * forcedUpdate : 0
     */

    private DatasEntity datas;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDatas(DatasEntity datas) {
        this.datas = datas;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public DatasEntity getDatas() {
        return datas;
    }

    public static class DatasEntity {
        private String name;
        private int version;
        private String type;
        private String url;
        private String introduce;
        private int isUp;
        private int versionLarge;
        private double versionSmall;
        private int updatedAt;
        private int createdAt;
        private int forcedUpdate;

        public void setName(String name) {
            this.name = name;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public void setIsUp(int isUp) {
            this.isUp = isUp;
        }

        public void setVersionLarge(int versionLarge) {
            this.versionLarge = versionLarge;
        }

        public void setVersionSmall(double versionSmall) {
            this.versionSmall = versionSmall;
        }

        public void setUpdatedAt(int updatedAt) {
            this.updatedAt = updatedAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public void setForcedUpdate(int forcedUpdate) {
            this.forcedUpdate = forcedUpdate;
        }

        public String getName() {
            return name;
        }

        public int getVersion() {
            return version;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public String getIntroduce() {
            return introduce;
        }

        public int getIsUp() {
            return isUp;
        }

        public int getVersionLarge() {
            return versionLarge;
        }

        public double getVersionSmall() {
            return versionSmall;
        }

        public int getUpdatedAt() {
            return updatedAt;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public int getForcedUpdate() {
            return forcedUpdate;
        }
    }
}
