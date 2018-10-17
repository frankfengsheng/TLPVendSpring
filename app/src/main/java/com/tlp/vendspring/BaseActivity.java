package com.tlp.vendspring;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.vendspring.R;

public class BaseActivity extends FragmentActivity {
    Context context;
    TextView tv_title;
    Button btn_back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        context=this;
    }
    /**
     * 初始化标题栏
     */
    public void initTitle(String title){
        tv_title= (TextView) findViewById(R.id.ms_tv_title_name);
        btn_back= (Button) findViewById(R.id.ms_title_btn_back);
        if(tv_title!=null)tv_title.setText(title);
        if(btn_back!=null)btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    /**
     * 跳转到下一个页面
      */
    public void nextView(Context context,Class<?> next) {
        Intent intent = new Intent(context, next);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(btn_back!=null)
        {
            btn_back.setOnClickListener(null);
            btn_back=null;
        }
        tv_title=null;
        context=null;
    }

}
