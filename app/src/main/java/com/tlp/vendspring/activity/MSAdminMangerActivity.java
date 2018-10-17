package com.tlp.vendspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.vendspring.R;
import com.tlp.vendspring.BaseActivity;

/**
 * create by feng
 */
public class MSAdminMangerActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_shelf_manager;
    private Button btn_aisle_manager;
    private Button btn_goods_manager;
    private Button btn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manger);
        initTitle("菜单");
        init_view();
    }

    private void init_view()
    {
        btn_shelf_manager= (Button) findViewById(R.id.ms_admin_menu_btn_shelf_manger);
        btn_aisle_manager= (Button) findViewById(R.id.ms_admin_menu_btn_aisle_manger);
        btn_goods_manager= (Button) findViewById(R.id.ms_admin_menu_btn_goods_manger);
        btn_exit= (Button) findViewById(R.id.ms_admin_menu_btn_exit);
        btn_shelf_manager.setOnClickListener(this);
        btn_aisle_manager.setOnClickListener(this);
        btn_goods_manager.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ms_title_btn_back:
                this.finish();
                break;
            case R.id.ms_admin_menu_btn_shelf_manger:
               Intent intent=new Intent(getApplicationContext(),ShelfMangerActivity.class);
               startActivity(intent);
                break;
            case R.id.ms_admin_menu_btn_aisle_manger:
                this.finish();
                break;
            case R.id.ms_admin_menu_btn_goods_manger:
                this.finish();
                break;
            case R.id.ms_admin_menu_btn_exit:
              gotodesk();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    //返回桌面
    public void gotodesk(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);

    }
}
