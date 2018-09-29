package com.tcn.funcommon.systeminfo;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by Songjiancheng on 2016/4/13.
 */
public class SystemInfo {

    private static final String CMD_CPU_INFO = "top -n 1";
    private static final String CMD_REBOOT = "su -c reboot";

    /*
     *第一行：User 35%, System 13%, IOW 0%, IRQ 0% // CPU占用率
     *第二行:User 109 + Nice 0 + Sys 40 + Idle 156 + IOW 0 + IRQ 0 + SIRQ 1 = 306
     */
    public static String getCPURunInfo() {
        String line = "";
        InputStream is = null;
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = runtime.exec(CMD_CPU_INFO);
            is = proc.getInputStream();
            InputStreamReader mInputStreamReader = new InputStreamReader(is);
            // 换成BufferedReader
            BufferedReader buf = new BufferedReader(mInputStreamReader);
            do {
                line = buf.readLine();
                // 前面有几个空行
                if ((line != null) && line.startsWith("User")) {
                    // 读到第一行时，我们再读取下一行
                    //line = buf.readLine();
                    break;
                }
            } while (true);
            if (is != null) {
                buf.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String getTotalUsage() {
        String strInfo = getCPURunInfo();
        String[] CPUusr = strInfo.split("%");
        String[] CPUusage = CPUusr[0].split("User");
        String[] SYSusage = CPUusr[1].split("System");
        String strCPUusage = CPUusage[1].trim();
        String strSYSusage = SYSusage[1].trim();
        Float fTotalUsage =  Float.valueOf(strCPUusage) + Float.valueOf(strSYSusage);
        String strTotalUsage = String.valueOf(fTotalUsage);

        return strTotalUsage;
    }

    public static String getUserUsage() {
        String strInfo = getCPURunInfo();
        String[] CPUusr = strInfo.split("%");
        String[] CPUusage = CPUusr[0].split("User");
        String strCPUusage = CPUusage[1].trim();

        return strCPUusage;
    }

    public static String getSysUsage() {
        String strInfo = getCPURunInfo();
        String[] CPUusr = strInfo.split("%");
        String[] SYSusage = CPUusr[1].split("System");
        String strSYSusage = SYSusage[1].trim();

        return strSYSusage;
    }

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize / 1024 /1024;
    }

    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize / 1024 /1024;
    }

    /**
     * 获取SDCARD剩余存储空间
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize / 1024 /1024;
        } else {
            return -1;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize / 1024 /1024;
        } else {
            return -1;
        }
    }

    public static void rebootDevice() {
        try {
            Runtime.getRuntime().exec(CMD_REBOOT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    /**
     * 这是使用adb shell命令来获取mac地址的方式
     * @return
     */
    public static String getMac() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static void execCmd(String cmd){
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

    public static void hideBar() {
        String ProcID = "79";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ProcID = "42"; // ICS AND NEWER
        }
        try {
            execCmd("service call activity " +ProcID+" s16 com.android.systemui");  //志强通达
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBar() {
        try {
            execCmd("am startservice -n com.android.systemui/.SystemUIService");   //志强通达
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
