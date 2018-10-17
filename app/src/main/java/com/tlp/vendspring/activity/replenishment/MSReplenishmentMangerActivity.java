package com.tlp.vendspring.activity.replenishment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcn.vendspring.R;
import com.tlp.vendspring.BaseActivity;
import com.tlp.vendspring.activity.admin.AisleMangerActivity;
import com.tlp.vendspring.activity.admin.ShelfMangerActivity;

/**
 * create by feng
 */
public class MSReplenishmentMangerActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_shelf_manager;
    private Button btn_aisle_manager;
    private Button btn_inventory_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replenishment_manger);
        initTitle("菜单");
        init_view();
    }

    private void init_view()
    {
        btn_shelf_manager= (Button) findViewById(R.id.ms_admin_menu_btn_shelf_manger);
        btn_aisle_manager= (Button) findViewById(R.id.ms_admin_menu_btn_aisle_manger);
        btn_inventory_change= (Button) findViewById(R.id.ms_admin_menu_btn_inventory_change);

        btn_shelf_manager.setOnClickListener(this);
        btn_aisle_manager.setOnClickListener(this);
        btn_inventory_change.setOnClickListener(this);

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
               nextView(getApplicationContext(),AisleMangerActivity.class);
                break;
            case R.id.ms_admin_menu_btn_inventory_change:
                this.finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
