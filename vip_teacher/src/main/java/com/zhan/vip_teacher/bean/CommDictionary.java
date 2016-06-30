package com.zhan.vip_teacher.bean;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.RequestHandle;
import com.zhan.framework.network.HttpRequestHandler;
import com.zhan.framework.network.HttpRequestUtils;
import com.zhan.vip_teacher.base.BasePersistObject;
import com.zhan.vip_teacher.base.Constants;
import com.zhan.vip_teacher.network.ApiUrls;
import com.zhan.vip_teacher.utils.Tools;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WuYue on 2016/3/11.
 */
public class CommDictionary extends BasePersistObject {
    private long LastQueryTime=0;

    public interface GetCallback{
        void onGetResult(CommDictionary data);
    }

    private static CommDictionary sCommDictionary=null;

    private static RequestHandle sRequestHandle;

    private static CommDictionary getInstance() {
        if(sCommDictionary==null){
            sCommDictionary=getPersisObject(CommDictionary.class);
        }
        return sCommDictionary;
    }

    public static void getCommDictionary(final GetCallback callback){
        //本地已保存,且没有过期直接返回
        if(getInstance()!=null){
            long diff=System.currentTimeMillis()-getInstance().getLastQueryTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if(days<1){
                Log.i("CommDictionary", "return cached Data");
                callback.onGetResult(getInstance());
                return;
            }
        }

        Log.i("CommDictionary", "Request network Data");
        HttpRequestUtils.releaseRequest(sRequestHandle);
        //本地没有保存，需要联网获取
        sRequestHandle=HttpRequestUtils.startRequest(ApiUrls.GET_COM_DIC,null,new HttpRequestHandler(){
            @Override
            public void onRequestFinished(ResultCode resultCode, String result) {
                super.onRequestFinished(resultCode, result);
                switch (resultCode){
                    case success:
                        try{
                            GetCommDictionaryResponseBean bean=Tools.parseJson(result,GetCommDictionaryResponseBean.class);
                            if(bean!=null&&bean.getData()!=null){
                                bean.getData().setLastQueryTime(System.currentTimeMillis());

                                //去掉课程异常
                                List<CourseStatusEntity> courseStatusList=bean.getData().getCourseStatus();
                                CourseStatusEntity removeItem=null;
                                for (CourseStatusEntity courseStatus:courseStatusList){
                                    if(Constants.COURSE_STATUS_ABNORMAL.equals(courseStatus.getId())){
                                        removeItem=courseStatus;
                                        break;
                                    }
                                }
                                if(removeItem!=null){
                                    courseStatusList.remove(removeItem);
                                }
                                persisObject(bean.getData());
                                sCommDictionary=bean.getData();
                            }
                        }catch (JSONException ex){
                            Log.e("CommDictionary", "fromJson error : " + ex.getMessage());
                        }
                        break;
                    case failed:
                    case timeout:
                    case noNetwork:
                        break;
                }
                callback.onGetResult(getInstance());
            }
        });
    }

    private List<CourseStatusEntity> CourseStatus;

    private List<CourseProgressEntity> CourseProgress;

    private List<HomeworkScoreEntity> HomeworkScore;

    private List<SignStatusEntity> SignStatus;

    public void setCourseStatus(List<CourseStatusEntity> CourseStatus) {
        this.CourseStatus = CourseStatus;
    }

    public void setCourseProgress(List<CourseProgressEntity> CourseProgress) {
        this.CourseProgress = CourseProgress;
    }

    public void setHomeworkScore(List<HomeworkScoreEntity> HomeworkScore) {
        this.HomeworkScore = HomeworkScore;
    }

    public void setSignStatus(List<SignStatusEntity> SignStatus) {
        this.SignStatus = SignStatus;
    }

    public List<CourseStatusEntity> getCourseStatus() {
        return CourseStatus;
    }

    public List<CourseProgressEntity> getCourseProgress() {
        return CourseProgress;
    }

    public List<HomeworkScoreEntity> getHomeworkScore() {
        return HomeworkScore;
    }

    public List<SignStatusEntity> getSignStatus() {
        return SignStatus;
    }

    public static class CourseStatusEntity implements Serializable{
        private String Id;
        private String Name;

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public String getName() {
            return Name;
        }
    }

    public static class CourseProgressEntity implements Serializable{
        private String Id;
        private String Name;

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public String getName() {
            return Name;
        }
    }

    public static class HomeworkScoreEntity implements Serializable{
        private String Id;
        private String Name;

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public String getName() {
            return Name;
        }
    }

    public static class SignStatusEntity implements Serializable{
        private String Id;
        private String Name;

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getId() {
            return Id;
        }

        public String getName() {
            return Name;
        }
    }

    public long getLastQueryTime() {
        return LastQueryTime;
    }

    public void setLastQueryTime(long lastQueryTime) {
        LastQueryTime = lastQueryTime;
    }
}
