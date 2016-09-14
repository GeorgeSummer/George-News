package com.george.summer.gerogenews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.george.summer.gerogenews.NetNews;
import com.george.summer.gerogenews.R;

import java.util.List;

import utils.XImageUtil;

/**
 * Created by Administrator on 2016/9/3.
 */
public class AdsViewagerAdapter extends PagerAdapter {
    List<NetNews.Ads> list;

    public AdsViewagerAdapter(List<NetNews.Ads> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = View.inflate(container.getContext(), R.layout.layout_ads_adapter,null);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_ads);
        TextView tv = (TextView) v.findViewById(R.id.tv_ads);
        XImageUtil.display(iv,list.get(position % list.size()).getImgsrc());
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv,
//                R.drawable.lording,R.drawable.error);
//        App.getLoader().get(list.get(position % list.size()).getImgsrc(),listener);
        tv.setText(list.get(position % list.size()).getTitle());
        container.addView(v);
        return v;
    }
}

