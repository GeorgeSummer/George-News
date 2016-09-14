package com.george.summer.gerogenews.myfragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.george.summer.gerogenews.NewsType;
import com.george.summer.gerogenews.R;
import com.george.summer.gerogenews.adapter.ViewpageAdapter;
import com.george.summer.gerogenews.basefragment.BlankFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/8/31.
 */
public class Fragment1 extends BlankFragment {
    TabPageIndicator mIndicator;
    ViewPager mPager;
    FragmentManager mManager;
    ArrayList<NewsType.NewsInfo> list;

    @Override
    public int getLayoutId() {
        return R.layout.layout_fragment1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mManager = getFragmentManager();
        getlist();
    }

    private void getlist() {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = (ArrayList<NewsType.NewsInfo>) bundle.getSerializable("list");
            ViewpageAdapter adapter = new ViewpageAdapter(getFragmentManager(),list);
            mPager.setAdapter(adapter);
            mIndicator.setViewPager(mPager);
            mIndicator.setVisibility(View.VISIBLE);
        }
    }

    public static Fragment1 getInstance(Bundle bundle){
        Fragment1 fg = new Fragment1();
        if(bundle != null){
            fg.setArguments(bundle);
        }
        return fg;
    }
}
