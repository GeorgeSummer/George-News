package com.george.summer.gerogenews.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.george.summer.gerogenews.NewsType;
import com.george.summer.gerogenews.myfragment.FragmentNews;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ViewpageAdapter extends FragmentPagerAdapter {
    private List<NewsType.NewsInfo> list;

    public ViewpageAdapter(FragmentManager fm,List<NewsType.NewsInfo> list) {
        super(fm);
        this.list = list;
    }

    public List<NewsType.NewsInfo> getList() {
        return list;
    }

    public void setList(List<NewsType.NewsInfo> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        String tname = list.get(position).getTname();
        String tid = list.get(position).getTid();
        bundle.putString("tname",tname);
        bundle.putString("tid",tid);
        return FragmentNews.getInstance(bundle);
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list == null?"未命名":list.get(position).getTname();
    }
}
