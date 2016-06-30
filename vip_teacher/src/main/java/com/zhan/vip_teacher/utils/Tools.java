package com.zhan.vip_teacher.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.zhan.framework.common.context.GlobalContext;
import com.zhan.framework.utils.ToastUtils;
import com.zhan.vip_teacher.R;
import com.zhan.vip_teacher.base.App;
import com.zhan.vip_teacher.base.BaseResponseBean;
import com.zhan.vip_teacher.bean.UserInfo;
import com.zhan.vip_teacher.ui.activity.MainActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by WuYue on 2016/3/7.
 */
public class Tools {

    public static String md5(String string) {
        byte[] hash = null;
        try {
            hash = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh,UTF-8 should be supported?", e);
        }
        return computeMD5(hash);
    }

    public static String computeMD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input, 0, input.length);
            byte[] md5bytes = md.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < md5bytes.length; i++) {
                String hex = Integer.toHexString(0xff & md5bytes[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 解析服务器时间，服务器是GMT+08时区
     * @param serverTimeStr
     * @return 返回 时间戳
     */
    public static long parseServerTime(String serverTimeStr) {
        if (TextUtils.isEmpty(serverTimeStr)) {
            return 0;
        }
        final String FORMAT = "yyyy-MM-dd HH:mm:ss";
        Date date = DateUtil.parseDate(serverTimeStr,FORMAT, TimeZone.getTimeZone("GMT+08"));
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }


    /***
     * 将时间戳装换为服务器时间字符串
     * 服务器是GMT+08时区
     * @param time
     * @return
     */
    public static String formatToServerTimeStr(long time){
        if(time>0){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            Date date = new Date(time);
            return dateFormat.format(date);
        }else{
            return null;
        }
    }


    /***
     * 装换Json
     *
     * @param json
     * @param beanClass
     * @return 如果转换出错 返回
     */
    public static <T extends BaseResponseBean> T parseJson(String json, Class<T> beanClass) {
        T bean = null;
        try{
            bean = JSON.parseObject(json, beanClass);
        }catch (JSONException ex){
            Log.e("Utils", "fromJson error : " + ex.getMessage());
        }

        return bean;
    }

    /**
     * 将Json String 转换为JsonObject 如果Json格式错误或Code!=0 返回null
     *
     * @param json
     * @param beanClass
     * @param <T>
     * @return 转换正确且Code==0返回 beanClass,否则返回null,并Toast 错误信息
     */
    public static <T extends BaseResponseBean> T parseJsonTostError(String json, Class<T> beanClass) {
        T bean = parseJson(json, beanClass);
        if (bean == null) {
            ToastUtils.toast(R.string.json_syntax_error);
        } else {
            if (bean.getCode() == 0) {
                return bean;
            }else if (bean.getCode() == 3) {
                ToastUtils.toast(R.string.utils_token_timeout);
                UserInfo.logout();
                Intent homePageIntent = new Intent(App.getInstance(), MainActivity.class);
                homePageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                App.getInstance().startActivity(homePageIntent);
            } else {
                ToastUtils.toast(bean.getMessage());
            }
        }
        return null;
    }


    public static String verifyResponseResult(BaseResponseBean bean) {
        String errorMsg=null;
        if (bean == null) {
            errorMsg=App.getInstance().getString(R.string.json_syntax_error);
        } else {
            if (bean.getCode() == 0) {
                errorMsg=null;
            }else if (bean.getCode() == 3) {
                errorMsg=App.getInstance().getString(R.string.utils_token_timeout);
                ToastUtils.toast(errorMsg);
                UserInfo.logout();
                Intent homePageIntent = new Intent(App.getInstance(), MainActivity.class);
                homePageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                App.getInstance().startActivity(homePageIntent);
            } else {
                errorMsg=bean.getMessage();
            }
        }
        return errorMsg;
    }

    public static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    public static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    public static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }

    public static void hideSoftInputFromWindow(View view) {
        InputMethodManager imm = (InputMethodManager) App.ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /***
     * 根据课程的子类名称获取对应的图标
     * @param subTypeName
     * @return
     */
    public static int getCourseTypeIconBySubTypeName(String subTypeName){
        int iconRes=R.drawable.course_type_def;
        Context ctx=App.getInstance();
        if(TextUtils.isEmpty(subTypeName)){
            iconRes=R.drawable.course_type_def;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_listening))){
            iconRes=R.drawable.course_type_listening;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_spoken))){
            iconRes=R.drawable.course_type_spoken;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_reading))){
            iconRes=R.drawable.course_type_reading;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_writing))){
            iconRes=R.drawable.course_type_writing;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_vocabulary))){
            iconRes=R.drawable.course_type_vocabulary;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_math))){
            iconRes=R.drawable.course_type_math;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_gap_filling))){
            iconRes=R.drawable.course_type_gap_filling;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_science))){
            iconRes=R.drawable.course_type_science;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_ratiocination))){
            iconRes=R.drawable.course_type_ratiocination;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_corrector))){
            iconRes=R.drawable.course_type_corrector;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_grammar))){
            iconRes=R.drawable.course_type_grammar;
        }

        return iconRes;
    }

    public static int getBlueCourseTypeIconBySubTypeName(String subTypeName){
        int iconRes=R.drawable.week_course_type_def;
        Context ctx= App.getInstance();
        if(TextUtils.isEmpty(subTypeName)){
            iconRes=R.drawable.week_course_type_def;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_listening))){
            iconRes=R.drawable.week_course_type_listening;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_spoken))){
            iconRes=R.drawable.week_course_type_spoken;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_reading))){
            iconRes=R.drawable.week_course_type_reading;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_writing))){
            iconRes=R.drawable.week_course_type_writing;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_vocabulary))){
            iconRes=R.drawable.week_course_type_vocabulary;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_math))){
            iconRes=R.drawable.week_course_type_math;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_gap_filling))){
            iconRes=R.drawable.week_course_type_gap_filling;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_science))){
            iconRes=R.drawable.week_course_type_science;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_ratiocination))){
            iconRes=R.drawable.week_course_type_ratiocination;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_corrector))){
            iconRes=R.drawable.week_course_type_corrector;
        }else if(subTypeName.contains(ctx.getString(R.string.course_type_grammar))){
            iconRes=R.drawable.week_course_type_grammar;
        }

        return iconRes;
    }

    /**
     * 下载并安装app
     *
     * @param url
     */
    public static void installApp(String url) {
        final DownloadManager systemService = (DownloadManager) App.ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "upgrade.apk");
        systemService.enqueue(request);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
                myDownloadQuery.setFilterById(reference);

                Cursor myDownload = systemService.query(myDownloadQuery);
                if (myDownload.moveToFirst()) {
                    int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                    String fileUri = myDownload.getString(fileUriIdx);

                    Intent ViewInstallIntent = new Intent(Intent.ACTION_VIEW);
                    ViewInstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ViewInstallIntent.setDataAndType(Uri.parse(fileUri), "application/vnd.android.package-archive");
                    context.startActivity(ViewInstallIntent);
                }
                myDownload.close();

                App.ctx.unregisterReceiver(this);
            }
        };
        App.ctx.registerReceiver(receiver, filter);
    }

}
