package controller;

import android.os.Message;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.vend.controller.TcnVendCMDDef;
import com.tcn.funcommon.vend.controller.TcnVendIF;

/**
 * Created by Administrator on 2016/6/30.
 */
public class VendIF {
    private static final String TAG = "VendIF";
    private static VendIF m_Instance = null;

    public static synchronized VendIF getInstance() {
        if (null == m_Instance) {
            m_Instance = new VendIF();
        }
        return m_Instance;
    }

    public void initialize(VendApplication application) {
        UICommon.getInstance().setApplication(application);
        if (application != null) {
            UIComBack.getInstance().setContext(application.getApplicationContext());
        }
        registerListener ();
    }


    public void deInitialize() {
        unregisterListener();
    }

    public void registerListener () {
        UICommon.getInstance().registerListener();
        TcnVendIF.getInstance().setOnVendListener(m_VendListener,false);
    }

    public void unregisterListener() {
        UICommon.getInstance().unregisterListener();
        TcnVendIF.getInstance().setOnVendListener(null,false);
        TcnVendIF.getInstance().setOnCommunicationListener(null,false);
    }

    private void OnDoorSwitch(int door) {
        TcnVendIF.getInstance().LoggerDebug(TAG,"OnDoorSwitch door: "+door);
    }

    private VendVendListener m_VendListener = new VendVendListener();
    private class VendVendListener implements TcnVendIF.VendListener {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TcnVendCMDDef.HANDLER_THREAD_VEND_STARED:
                    break;
               case TcnVendCMDDef.HANDLER_THREAD_VEND_STARED_DELAY:

                    break;
                default:
                    break;
            }
        }
    }
}
