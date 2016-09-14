package com.george.summer.gerogenews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {
    private ArrayList<NewsType.NewsInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        String url = "http://c.m.163.com/nc/topicset/android/subscribe/manage/listspecial.html";
        RequestParams entity = new RequestParams(url);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                NewsType newsType = gson.fromJson(result, NewsType.class);
                list = newsType.gettList();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", listCheck(list));
                Intent intent = new Intent(AdsActivity.this, NewsActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(AdsActivity.this, "网络错误......", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private ArrayList<NewsType.NewsInfo> listCheck(ArrayList<NewsType.NewsInfo> list) {
        String[] name = { "段子", "图片", "本地", "热点", "网易号", "美女", "萌宠" };
        for(int i = 0; i < name.length; i++){
            for(int j = 0; j < list.size(); j++){
                if(list.get(j).getTname().equals(name[i])){
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
