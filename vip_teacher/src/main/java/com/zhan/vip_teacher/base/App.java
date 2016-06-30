package com.zhan.vip_teacher.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhan.framework.common.context.GlobalContext;

import java.io.File;

/**
 * Created by Administrator on 2016/3/1.
 */
public class App extends GlobalContext {

    public static App ctx;

    public int mFabLeftMargin ;
    public int mFabTopMargin ;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
    }

    public int getFabLeftMargin() {
        return mFabLeftMargin;
    }

    public void setFabLeftMargin(int fabLeftMargin) {
        mFabLeftMargin = fabLeftMargin;
    }

    public int getFabTopMargin() {
        return mFabTopMargin;
    }

    public void setFabTopMargin(int fabTopMargin) {
        mFabTopMargin = fabTopMargin;
    }
}
