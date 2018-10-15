package com.tlp.vendspring;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;


import com.tcn.vendspring.R;
import com.tlp.vendspring.activity.MSLoginMenu;
import com.tlp.vendspring.fragment.MSBannerFragment;
import com.tlp.vendspring.fragment.MSGoodsFragment;


public class MSMainActivity extends FragmentActivity implements View.OnClickListener{

    private RelativeLayout rl_title;


    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlpmain);
        init_view();
    }
    private void init_view(){

        rl_title= (RelativeLayout) findViewById(R.id.tlp_rl_main_titlebar);
        fragmentManager=this.getFragmentManager();
        transaction=fragmentManager.beginTransaction();
        MSBannerFragment bannerfragment= MSBannerFragment.newInstance(null,null);
        MSGoodsFragment goodsFragment= MSGoodsFragment.newInstance(null,null);
        transaction.add(R.id.tlp_ly_advertisement_fragment,bannerfragment);
        transaction.add(R.id.tlp_ly_goods,goodsFragment);
        transaction.commit();

        rl_title.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tlp_rl_main_titlebar:
                Intent intent=new Intent(getApplicationContext(), MSLoginMenu.class);
                startActivity(intent);
                break;
        }
    }
}
