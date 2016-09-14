package com.george.summer.gerogenews;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import utils.MySQLiteDataBase;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView wv;
    private ProgressBar pb;
    ImageView iv;
    TextView tv1,tv2,tv3,tv4;
    PopupWindow window;
    Bundle bundle;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        wv = (WebView) findViewById(R.id.webview1);
        pb = (ProgressBar) findViewById(R.id.article_pb) ;
        iv = (ImageView) findViewById(R.id.article_iv);
        tv1 = (TextView) findViewById(R.id.article_tv1);
        tv2 = (TextView) findViewById(R.id.article_tv2);
        tv3 = (TextView) findViewById(R.id.article_tv3);
        tv4 = (TextView) findViewById(R.id.article_tv4);
        ShareSDK.initSDK(this);
        db = new MySQLiteDataBase(this).getWritableDatabase();
        bundle = getIntent().getExtras();
        init();
        iv.setOnClickListener(this);
    }

    private void init() {
        int len = "C0HJ4UKA05159864".length()+2;
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        final String docid = bundle.getString("docid");
        String path = bundle.getString("url");

            if(docid.length() > len){
                Toast.makeText(ArticleActivity.this, "找不到资源，请浏览其他页面！", Toast.LENGTH_SHORT).show();
                finish();
             }else{
                String url = "http://c.m.163.com/nc/article/" + docid + "/full.html";
                x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try{
                            String str = new JSONObject(result).getString(docid);
                            NewsBody newsBody = new Gson().fromJson(str, NewsBody.class);
                            String before = "<img src=\"";
                            String after = "\"/> </img>";
                            for(int i =0 ;i<newsBody.img.size();i++){
                                newsBody.body = newsBody.body.replace(newsBody.img.get(i).ref,
                                        before + newsBody.img.get(i).src + after);
                                //                        wv.loadData(newsBody.body, "text/html", "utf-8");
                                wv.loadDataWithBaseURL(null, newsBody.body, "text/html", "utf-8", null);
                                tv1.setText(newsBody.title);
                                tv2.setText(newsBody.source);
                                tv3.setText(newsBody.ptime);
                                tv4.setText(newsBody.digest);
                                if(newsBody.sourceinfo != null){
                                    tv2.setTextColor(Color.BLUE);
                                }
                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset();
            }
        });

        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    pb.setVisibility(View.INVISIBLE);
                    wv.setVisibility(View.VISIBLE);
                } else {
                    // 加载中
                    pb.setVisibility(View.VISIBLE);
                    wv.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.article_iv:
                showPopuWindow();
                break;
            case R.id.popu_btn1:
                showShare();
                Toast.makeText(ArticleActivity.this, "分享", Toast.LENGTH_SHORT).show();
                window.dismiss();
                break;
            case R.id.popu_btn2:
                Toast.makeText(ArticleActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                NetNews netNews = (NetNews) bundle.getSerializable("netNews");
                saveDB(netNews);
                window.dismiss();
                break;
        }

    }

    private void saveDB(NetNews netNews) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(netNews);
            oos.flush();
            byte[] bytes = baos.toByteArray();
            ContentValues values = new ContentValues();
            values.put("news",bytes);
            db.insert("NewsTable",null,values);
            //db.execSQL("insert into NewsTable(news) value(?)",new Object[]{bytes});
            baos.close();
            oos.close();
            db.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void showPopuWindow() {
        View view = View.inflate(this,R.layout.layout_popuwindow,null);
        window = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        view.findViewById(R.id.popu_btn1).setOnClickListener(this);
        view.findViewById(R.id.popu_btn2).setOnClickListener(this);
        window.setTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        window.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 设置好参数之后再show
        window.showAsDropDown(iv);
    }

    private void imgReset() {
        wv.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
