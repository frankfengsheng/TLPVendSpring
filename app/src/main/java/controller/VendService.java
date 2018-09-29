package controller;


import android.content.res.Configuration;

import com.tcn.funcommon.TcnService;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 16:25
 * 邮箱：m68013@qq.com
 */
public class VendService extends TcnService {
    private static final String TAG = "VendService";
    private VendApplication m_VendApplication = null;
    private Thread.UncaughtExceptionHandler m_UncaughHandler = null;


    @Override
    public void onCreate() {
        super.onCreate();
        m_UncaughHandler = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                //任意一个线程异常后统一的处理
                StringWriter stringWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(stringWriter);
                ex.printStackTrace(writer); // 打印到输出流
                String exception =stringWriter.toString();stopSelf();
                TcnVendIF.getInstance().LoggerError(TAG, "setDefaultUncaughtExceptionHandler exception: "+exception);
            }
        };
        ////捕捉异常，并将具体异常信息写入日志中
        Thread.setDefaultUncaughtExceptionHandler(m_UncaughHandler);

        TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate()");
        m_VendApplication = (VendApplication)getApplication();
        VendIF.getInstance().initialize(m_VendApplication);
        TcnVendIF.getInstance().sendMsgToUIDelay(TcnVendEventID.RESTART_MAIN_ACTIVITY, -1, -1, -1,3000, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TcnVendIF.getInstance().LoggerDebug(TAG, "onConfigurationChanged newConfig: "+newConfig.orientation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VendIF.getInstance().deInitialize();
        m_UncaughHandler = null;
        Thread.setDefaultUncaughtExceptionHandler(null);
        TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
    }
}
