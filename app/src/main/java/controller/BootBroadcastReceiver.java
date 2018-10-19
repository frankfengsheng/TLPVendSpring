package controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.vendspring.MSMainActivity;
import com.tcn.vendspring.MainAct;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:30
 * 邮箱：m68013@qq.com
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String action_boot="android.intent.action.BOOT_COMPLETED";
    private static final int START_COMMAND = 1;
    private Context m_context = null;
    private Intent m_intent_Service;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == context) {
            return;
        }
        TcnLog.getInstance().initLog(context);
        TcnVendIF.getInstance().LoggerDebug("onReceive","VendReceiver intent.getAction(): "+intent.getAction());
        m_context = context;
        if(action_boot.equals(intent.getAction())){

            //启动服务与主板进行通讯
            m_intent_Service = new Intent(m_context, VendService.class);
            m_context.startService(m_intent_Service);

//            Intent mIntent = new Intent(m_context,MainAct.class);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            m_context.startActivity(mIntent);
//            if (m_handler != null) {
//                m_handler.sendEmptyMessageDelayed(START_COMMAND, 3000);
//            }

        }
    }

    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case START_COMMAND:
                    Intent mIntent = new Intent(m_context,MSMainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_context.startActivity(mIntent);
                    break;
                default:
                    break;
            }

        }
    };
}
