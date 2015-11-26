package com.eric.xlee.adapter;

import java.util.ArrayList;
import java.util.Collection;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
    public ArrayList<View> mViews;

    public ViewPagerAdapter(ArrayList<View> views) {
        this.mViews = views;
    }

    public View getItem(int position) {
        return mViews.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        if (mViews.contains(object)) {
            return mViews.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mViews != null) {
            return mViews.size();
        }
        return 0;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        startUpdate((View) container);
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        finishUpdate((View) container);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View itemView = mViews.get(position);
        view.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object arg1) {
        return view == arg1;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object arg2) {
        if (position < mViews.size()) {
            view.removeView(mViews.get(position));
        }
    }

    public void addAll(Collection<View> list) {
        if (null != list) {
            mViews.addAll(list);
        }
    }

    public void add(View view) {
        if (null != view) {
            mViews.add(view);
        }
    }

    public void remove(View view) {
        mViews.remove(view);
    }

    public void remove(int index) {
        mViews.remove(index);
    }

    public void removeAll(Collection<?> collections) {
        mViews.removeAll(collections);
    }

    public void clear() {
        mViews.clear();
    }
}