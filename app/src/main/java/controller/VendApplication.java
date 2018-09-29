package controller;

import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnVendApplication;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:53
 * 邮箱：m68013@qq.com
 */
public class VendApplication extends TcnVendApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        TcnShareUseData.getInstance().setBoardType(TcnConstant.DEVICE_CONTROL_TYPE[5]);
        TcnShareUseData.getInstance().setSerPortGroupMapFirst("0");
        TcnShareUseData.getInstance().setBoardBaudRate("9600");
        TcnShareUseData.getInstance().setBoardSerPortFirst("/dev/ttyS0");  //根据驱动板实际接的串口来设置
    }
}
