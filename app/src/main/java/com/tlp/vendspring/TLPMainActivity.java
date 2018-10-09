package com.tlp.vendspring;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.tcn.vendspring.R;
import com.tlp.vendspring.fragment.TlpBannerFragment;
import com.tlp.vendspring.fragment.TlpGoodsFragment;


public class TLPMainActivity extends FragmentActivity {

    private LinearLayout ly_advertisement;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlpmain);
        init_view();
    }
    private void init_view(){

        ly_advertisement= (LinearLayout) findViewById(R.id.tlp_ly_advertisement_fragment);
        fragmentManager=this.getFragmentManager();
        transaction=fragmentManager.beginTransaction();
        TlpBannerFragment bannerfragment=TlpBannerFragment.newInstance(null,null);
        TlpGoodsFragment goodsFragment=TlpGoodsFragment.newInstance(null,null);
        transaction.add(R.id.tlp_ly_advertisement_fragment,bannerfragment);
        transaction.add(R.id.tlp_ly_goods,goodsFragment);
        transaction.commit();

    }




}
