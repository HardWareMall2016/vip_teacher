package com.zhan.vip_teacher.ui.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WuYue on 2016/3/8.
 */
public class CachedViewPager extends ViewPager {
    public final static int DEF_CACHE_SIZE=2;

    //当天前后一个星期
    private  int PAGE_SIZE=14;
    private  int CACHE_SIZE=DEF_CACHE_SIZE;//必须>=2
    private ICacheView mICacheView;

    private CachedAdapter adapter;

    private boolean mFirst=true;
    private int mDefPosition=PAGE_SIZE/2;

    public CachedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initViewPager(int pageSize,int cacheSize,ICacheView iCacheView, final int defPos){
        PAGE_SIZE=pageSize>=3?pageSize:3;
        CACHE_SIZE=cacheSize>=2?cacheSize:2;

        adapter=new CachedAdapter();
        setAdapter(adapter);

        mICacheView=iCacheView;

        mDefPosition=defPos;
        setCurrentItem(defPos);

        addOnPageChangeListener(mPageChangeListener);

    }

    public View getPage(int position){
        return adapter.getPage(position);
    }

    private void validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener mPageChangeListener=new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            View selectedPage=adapter.getPage(position);
            if(selectedPage!=null){
                mICacheView.onPageSelected(selectedPage,position);
            }
            Log.i("CachedViewPager","onPageSelected position = "+position+" selectedPage : "+selectedPage);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class CachedAdapter extends PagerAdapter {
        private final LayoutInflater inflater;
        private final SparseArray<View> viewMap=new SparseArray<>();

        public CachedAdapter(){
            inflater = LayoutInflater.from(getContext());
            viewMap.clear();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return PAGE_SIZE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("CachedViewPager","instantiateItem position = "+position);

            View cachedView=getCachedView(container,position);

            if (cachedView == null) {
                cachedView = mICacheView.inflaterPageView(inflater,position);
                viewMap.put(position,cachedView);
            }

            mICacheView.initPageView(cachedView, position);

            container.addView(cachedView, 0);

            if(mFirst&&position==mDefPosition){
                mFirst=false;
                Log.i("CachedViewPager","onPageSelected position = "+position+" instantiatePage : "+cachedView);
                mICacheView.onPageSelected(cachedView,position);
            }

            return cachedView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(viewMap.get(position));
        }

        @Override
        public int getItemPosition(Object object)   {
            return POSITION_NONE;
        }

        private View getCachedView(ViewGroup container,int position){
            View cachedView =viewMap.get(position);
            //查找无用的View
            int cachedPosition=-1;
            for(int index=0;index<viewMap.size();index++){
                int pos=viewMap.keyAt(index);
                if(isCachedView(position, pos)){
                    cachedPosition=pos;
                    break;
                }
            }
            //有缓存View
            if(cachedView==null&&cachedPosition!=-1){
                cachedView=viewMap.get(cachedPosition);
                viewMap.delete(cachedPosition);
                container.removeView(cachedView);
                viewMap.put(position,cachedView);
            }
            return cachedView;
        }

        private boolean isCachedView(int curPosition, int position){
            int offset= Math.abs(position - curPosition);
            if(offset>CACHE_SIZE){
                return true;
            }else{
                return false;
            }
        }

        public View getPage(int position){
            return viewMap.get(position);
        }
    }

    public interface ICacheView{
        View inflaterPageView(LayoutInflater inflater,int position);
        void initPageView(View page,int position);
        void onPageSelected(View selectedPage,int position);
    }
}
