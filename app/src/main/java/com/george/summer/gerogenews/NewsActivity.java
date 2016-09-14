package com.george.summer.gerogenews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.george.summer.gerogenews.basefragment.BlankFragment;
import com.george.summer.gerogenews.myfragment.Fragment1;
import com.george.summer.gerogenews.myfragment.Fragment2;
import com.george.summer.gerogenews.myfragment.Fragment3;
import com.george.summer.gerogenews.myfragment.Fragment4;

public class NewsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener
,BlankFragment.OnFragmentInteractionListener {
    RadioGroup group;
    Fragment1 f1;
    Fragment2 f2;
    Fragment3 f3;
    Fragment4 f4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        group = (RadioGroup) findViewById(R.id.radiogroup1);
        f2 = Fragment2.getInstance(getIntent().getBundleExtra("bundle"));
        f3 = new Fragment3();
        f4 = new Fragment4();
        f1 = Fragment1.getInstance(getIntent().getBundleExtra("bundle"));
        //        group.check(R.id.radiobtn1);
        //        getSupportFragmentManager().beginTransaction().add(R.id.framelayout1,f1).commit();
        addFragment(f1);
        group.setOnCheckedChangeListener(this);
    }

    private void addFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout1, f,
                f.getClass().getSimpleName()).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i){
            case R.id.radiobtn1:
                showFragment(f1);
                break;
            case R.id.radiobtn2:
                showFragment(f2);
                break;
            case R.id.radiobtn3:
                showFragment(f3);
                break;
            case R.id.radiobtn4:
                showFragment(f4);
                break;
        }
    }

    private void showFragment(Fragment f) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(f.getClass().getSimpleName());
        if(fragment == null){
            addFragment(f);
        }
        Fragment[] fs = { f1, f2, f3, f4 };
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        for(Fragment tf : fs){
            tr.hide(tf);
        }
        tr.show(f).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String url, String docid,NetNews netNews) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("netNews",netNews);
        bundle.putString("url",url);
        bundle.putString("docid",docid);
        Intent intent = new Intent(this,ArticleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
