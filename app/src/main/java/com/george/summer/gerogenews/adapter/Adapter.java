package com.george.summer.gerogenews.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.george.summer.gerogenews.NetNews;
import com.george.summer.gerogenews.R;

import java.util.List;

import utils.XImageUtil;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int ADS = 3;
    private static final int ADS_ONE = 4;
    private static final int FOOT = 5;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE=0;
    //正在加载...
    public static final int ISLOADING=1;
    //上拉加载的显示状态，初始为0
    private int load_more_status=0;

    List<NetNews> list;
    Context mContext;

    boolean isFromDB;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public Adapter(List<NetNews> list, Context context, boolean isFromDB) {
        this.list = list;
        this.mContext = context;
        this.isFromDB = isFromDB;
    }

    public Adapter(Context context,boolean isFromDB) {
        mContext = context;
        this.isFromDB = isFromDB;
    }

    public Adapter() {
    }

    public List<NetNews> getList() {
        return list;
    }

    public void setList(List<NetNews> list) {
        this.list = list;
    }

    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            if(list.get(position).getAds() != null){
                return ADS;
            }else{
                return ADS_ONE;
            }
        }else if(position == list.size()){
            return FOOT;
        }else {
            if(list.get(position).getBoardid() == null ||
                    list.get(position).getBoardid().equals("photoview_bbs") ||
                    list.get(position).getBoardid().equals("news2_bbs") ){
                return ONE;
            }else {
                return TWO;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if(viewType == ONE){
           View v = View.inflate(parent.getContext(), R.layout.layout_complex,null);
           return new Myholder1(v);
       }else if(viewType == TWO){
           View v = View.inflate(parent.getContext(),R.layout.layout_simple,null);
           return new Myholder2(v);
       }else if(viewType == ADS){
            View v =View.inflate(parent.getContext(),R.layout.layout_ads,null);
            return new Myholder3(v);
        }else if(viewType == ADS_ONE){
           View v =View.inflate(parent.getContext(),R.layout.layout_ads_adapter,null);
           return new Myholder4(v);
       }else  if(viewType == FOOT){
           View v = View.inflate(parent.getContext(),R.layout.layout_foot,null);
           return new Myholder5(v);
       }else {
           return null;
       }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Myholder1){
            ((Myholder1) holder).tv_title1.setText(list.get(position).getTitle());
            ((Myholder1) holder).tv_sourse1.setText(list.get(position).getSource());
            ((Myholder1) holder).tv_replycount1.setText(list.get(position).getReplyCount()+"跟帖");
            XImageUtil.display(((Myholder1) holder).img1,list.get(position).getImgsrc());
            ((Myholder1) holder).itemView.setTag(list.get(position));
//            ImageLoader.ImageListener listener1 = ImageLoader.getImageListener(((Myholder1) holder).img1,
//                    R.drawable.lording,R.drawable.error);
//            App.getLoader().get(list.get(position).getImgsrc(),listener1);

            if(list.get(position).getImgextra() == null){
                ((Myholder1) holder).img2.setVisibility(View.GONE);
                ((Myholder1) holder).img3.setVisibility(View.GONE);
            }else {
                XImageUtil.display(((Myholder1) holder).img2,list.get(position).getImgextra().get(0).getImgsrc());
                XImageUtil.display(((Myholder1) holder).img3,list.get(position).getImgextra().get(1).getImgsrc());
            }
        }else if(holder instanceof Myholder2){
            ((Myholder2) holder).tv_title.setText(list.get(position).getTitle());
            ((Myholder2) holder).tv_sourse.setText(list.get(position).getSource());
            ((Myholder2) holder).tv_replycount.setText(list.get(position).getReplyCount()+"跟帖");
            XImageUtil.display(((Myholder2) holder).img_icon,list.get(position).getImgsrc());
            ((Myholder2) holder).itemView.setTag(list.get(position));
        }else if(holder instanceof Myholder3){
            final Myholder3 myholder3 = (Myholder3) holder;
            final int len = list.get(position).getAds().size();
            myholder3.itemView.setTag(list.get(position));
            AdsViewagerAdapter adapter = new AdsViewagerAdapter(list.get(position).getAds());
            myholder3.mPager_ads.setAdapter(adapter);
            for(int i = 0; i < len; i++){
                ImageView img = new ImageView(mContext);
                img.setImageResource(R.drawable.dot_gray);
                myholder3.linear.addView(img);
//                LayoutParams继承于Android.View.ViewGroup.LayoutParams.
//                        其实这个LayoutParams类是用于child view（子视图） 向 parent view（父视图）
//                传达自己的意愿的一个东西（孩子想变成什么样向其父亲说明） 。
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams();
                layoutParams.leftMargin = 5;
                layoutParams.rightMargin = 5;
            }
            ImageView childAt = (ImageView) myholder3.linear.getChildAt(0);
            childAt.setImageResource(R.drawable.dot_white);
            myholder3.mPager_ads.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int pos = position % len;
                    for(int i = 0;i<myholder3.linear.getChildCount();i++){
                        ImageView childAt1 = (ImageView) myholder3.linear.getChildAt(i);
                        childAt1.setImageResource(R.drawable.dot_gray);
                    }
                    ((ImageView) myholder3.linear.getChildAt(pos)).setImageResource(R.drawable.dot_white);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else if(holder instanceof Myholder4){
            ((Myholder4) holder).tv.setText(list.get(position).getTitle());
            XImageUtil.display(((Myholder4) holder).iv,list.get(position).getImgsrc());
            ((Myholder4) holder).itemView.setTag(list.get(position));
        }else if(holder instanceof Myholder5){
            Myholder5 footer = (Myholder5) holder;
            switch(load_more_status){
                case PULLUP_LOAD_MORE:
                    footer.pb.setVisibility(View.GONE);
                    footer.tv.setVisibility(View.GONE);
//                    footer.tv.setText("上拉加载更多");
                    break;
                case ISLOADING:
                    footer.pb.setVisibility(View.VISIBLE);
                    footer.tv.setText("正在加载...");
                   break;
            }
        }

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list.get(position).getUrl() == null){
                        mOnItemClickListener.onItemClick(
                                "null",
                                list.get(position).getDocid(),list.get(position));
                    }else {
                        mOnItemClickListener.onItemClick(
                                list.get(position).getUrl(),
                                list.get(position).getDocid(),list.get(position));
                    }

                }
            });
        }

        if(isFromDB){
            if(mOnItemLongClickListener != null){
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemLongClickListener.onItemLongClick(list.get(position));
                        return true;
                    }
                });
            }
        }else {
            return;
        }
    }
//"http://3g.163.com/news/16/0907/09/C0BN7RIP000181KT.html"

    @Override
    public int getItemCount() {
        return list == null?0:list.size()+1;
    }

    public static class Myholder1 extends RecyclerView.ViewHolder{
        //complex--三张图或一张大图 ONE
        TextView tv_title1,tv_sourse1,tv_replycount1;
        ImageView img1,img2,img3;
        public Myholder1(View itemView) {
            super(itemView);
            tv_title1 = (TextView) itemView.findViewById(R.id.tv_title11);
            tv_sourse1 = (TextView) itemView.findViewById(R.id.tv_sourse1);
            tv_replycount1 = (TextView) itemView.findViewById(R.id.tv_replycount1);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
        }
    }

    public static class Myholder2 extends RecyclerView.ViewHolder{
        //simple--一张图 TWO
        TextView tv_title,tv_sourse,tv_replycount;
        ImageView img_icon;
        public Myholder2(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_sourse = (TextView) itemView.findViewById(R.id.tv_sourse);
            tv_replycount = (TextView) itemView.findViewById(R.id.tv_replycount);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
        }
    }

    public static class Myholder3 extends RecyclerView.ViewHolder{
        //ads--顶部的ViewPager ADS
        ViewPager mPager_ads;
        LinearLayout linear;
        public Myholder3(View itemView) {
            super(itemView);
            mPager_ads = (ViewPager) itemView.findViewById(R.id.viewpager_ads);
            linear = (LinearLayout) itemView.findViewById(R.id.linear_dot);
        }
    }

    public static class Myholder4 extends RecyclerView.ViewHolder{
        //ads--顶部一张大图 ADS_ONE
        ImageView iv;
        TextView tv;
        public Myholder4(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_ads);
            tv = (TextView) itemView.findViewById(R.id.tv_ads);
        }
    }

    public  static class Myholder5 extends RecyclerView.ViewHolder{
        //foot
        private ProgressBar pb;
        private TextView tv;
        public Myholder5(View itemView) {
            super(itemView);
            pb= (ProgressBar) itemView.findViewById(R.id.foot_probar1);
            tv = (TextView) itemView.findViewById(R.id.foot_tv);
        }
    }

    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(String url , String docid, NetNews netNews);
    }
    public  interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(NetNews netNews);
    }
}
