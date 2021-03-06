package com.tcn.funcommon;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/28 19:19
 * 邮箱：m68013@qq.com
 */
public class TcnVendApplication extends Application {
    private Activity m_MainAct = null;
    private Activity m_CurrentAct = null;
    private List<Activity> activities;

    @Override
    public void onCreate() {
        activities = new ArrayList<Activity>();
        TcnVendIF.getInstance().LoggerDebug("TcnVendApplication", "onCreate");
        TcnVendIF.getInstance().init(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TcnVendIF.getInstance().LoggerDebug("TcnVendApplication", "newConfig: "+newConfig.orientation);
        TcnVendIF.getInstance().init(getApplicationContext());
    }

    public void setCurrentActivity(Activity activity){
        m_CurrentAct = activity;
    }

    public Activity getCurrentActivity(){
        return m_CurrentAct;
    }

    /**
     * 添加新打开的Activity
     * @return
     */
    public void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    /**
     *关闭所有容器里的的Activity
     * @return
     */
    public void closeActivity(){
        for (Activity a:activities) {
            a.finish();
        }
    }

    public void setMainActivity(Activity main){
        m_MainAct = main;
    }

    /**
     *关闭除了exception的所有容器里的的Activity
     * @return
     */
    public void closeActivityExceptMain() {
        for (Activity a:activities) {
            if (a.equals(m_MainAct)) {
                //do nothing
            } else {
                a.finish();
            }

        }
    }

}
