package com.tcn.funcommon;

import android.content.Context;
import android.os.Environment;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.tcn.funcommon.fileoperation.FileOperation;
import com.tcn.funcommon.media.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Songjiancheng on 2016/4/15.
 */
public class TcnLog {
    private static final String TAG = "TcnLog";
    private static TcnLog m_Instance = null;

    private static final String LOG_FILE_NAME = "microlog.txt";
    private static final String LOG_FOLDER_NAME = "TcnLog";
    private static final String LOG_FLAG = "--Tcn--";

    private volatile boolean m_bOpenLog = true;
    private volatile boolean m_bLogChecked = false;
    private Context m_Context = null;
    private Logger m_Logger = null;

    public static synchronized TcnLog getInstance() {
        if (null == m_Instance) {
            m_Instance = new TcnLog();
        }
        return m_Instance;
    }

    public void initLog(Context context) {
        m_Logger = LoggerFactory.getLogger();
        PropertyConfigurator.getConfigurator(context).configure();
        FileAppender fa = (FileAppender)m_Logger.getAppender(1);
        fa.setAppend(true);
    }

    public String getLoggerYearMonthDay() {
        return TcnUtility.getTime(TcnConstant.YEAR_M_D);
    }

    public String getLoggerY_M_D() {
        return TcnUtility.getTime(TcnConstant.Y_M_D);
    }

    public String getLoggerYMD() {
        return TcnUtility.getTime(TcnConstant.YMD);
    }

    public void LoggerDebug(String tag, String msg) {
        if (m_bOpenLog && (m_Logger != null)) {
            try {
                m_Logger.debug(tag + LOG_FLAG + TcnUtility.getTime(TcnConstant.YEAR) + "--" + msg + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void LoggerInfo(String tag, String msg) {
        if (m_bOpenLog && (m_Logger != null)) {
            try {
                m_Logger.info(tag + LOG_FLAG + TcnUtility.getTime(TcnConstant.YEAR) + "--" + msg + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void LoggerError(String tag, String msg) {
        if (m_bOpenLog && (m_Logger != null)) {
            try {
                m_Logger.error(tag + LOG_FLAG + TcnUtility.getTime(TcnConstant.YEAR) + "--" + msg + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean logFileSizeLessThanXM(int dataM, String fileName) {
        boolean bRet = false;
        File file = new File(fileName);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                long available = fis.available();
                if((0 == available) || (available > dataM*1048576)) {
                    //
                } else {
                    bRet = true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LoggerError(TAG,"logFileSizeLessThanXM FileNotFoundException e: "+e);
            } catch (IOException e) {
                e.printStackTrace();
                LoggerError(TAG,"logFileSizeLessThanXM 3 IOException e: "+e);
            }
        }

        return bRet;
    }

    private void clearLog(File file) {
        BufferedWriter bw = null;
        try {
            FileWriter fw =  new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write("");
        } catch (Exception e) {
            // TODO: handle exception
            LoggerError(TAG,"logFileCheck1 Exception e: "+e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LoggerError(TAG,"logFileCheck 2 IOException e: "+e);
                }
            }
        }
    }

    private boolean isNeedDelete(String fileName) {
        boolean bRet = false;
        int index = fileName.lastIndexOf("-");
        int indexPoint = fileName.lastIndexOf(".");
        if ((fileName.length() <= (indexPoint) ) || (index >= indexPoint)) {
            bRet = true;
            return bRet;
        }
        String timeData = fileName.substring(index+1,indexPoint);
        if (timeData.length() != 8) {
            bRet = true;
            return bRet;
        }
        if (!TcnUtility.isDigital(timeData)) {
            bRet = true;
            return bRet;
        }
        String strCurrentTime = getLoggerYMD();
        if (strCurrentTime.length() != 8) {
            bRet = true;
            return bRet;
        }

        int iYear = Integer.parseInt(timeData.substring(0,4));
        int iYearCurrent = Integer.parseInt(strCurrentTime.substring(0,4));

        int iMonth = Integer.parseInt(timeData.substring(4,6));
        int iMonthCurrent = Integer.parseInt(strCurrentTime.substring(4,6));

        int iDay = Integer.parseInt(timeData.substring(6,8));
        int iDayCurrent = Integer.parseInt(strCurrentTime.substring(6,8));

        if (0 == (iYearCurrent - iYear)) {
            if (1 == (iMonthCurrent - iMonth)) {
                if ((iDay <= 15) && (iDayCurrent > 20)) {
                    bRet = true;
                } else if ((iDay <= 20) && (iDayCurrent > 25)) {
                    bRet = true;
                } else {

                }
            } else if ((iMonthCurrent - iMonth) >= 2) {
                bRet = true;
            } else {

            }
        } else if (1 == (iYearCurrent - iYear)) {
            if ((1 == iMonthCurrent) && (12 == iMonth)) {
                if ((iDay <= 15) && (iDayCurrent > 20)) {
                    bRet = true;
                } else if ((iDay <= 20) && (iDayCurrent > 25)) {
                    bRet = true;
                } else {

                }
            }
        } else if ((iYearCurrent - iYear) >= 2) {
            bRet = true;
        } else {

        }


        return bRet;
    }

    private String getLogFileStartAndEndTime(String filePathAndName) {

        String strStartAndEndTime = getLoggerYMD();
        String strFirstLine = FileOperation.instance().readFileFirstLine(LOG_FLAG,filePathAndName);
        if ((null == strFirstLine) || (!strFirstLine.contains(LOG_FLAG))) {
            return strStartAndEndTime;
        }
        int indexTcn = strFirstLine.indexOf(LOG_FLAG);
        if (strFirstLine.length() < (indexTcn + 7 + 10)) {
            return strStartAndEndTime;
        }
        String startTime = strFirstLine.substring(indexTcn + 7,indexTcn + 7 + 10);
        startTime = startTime.replace("/","");
        if (!TcnUtility.isDigital(startTime)) {
            return strStartAndEndTime;
        }
        if (startTime.length() != 8) {
            return strStartAndEndTime;
        }
        strStartAndEndTime = startTime+"-"+strStartAndEndTime;
        return strStartAndEndTime;
    }


    private void checkLogFile()
    {
        String logFilePath = Utils.getExternalStorageDirectory() + "/" + LOG_FOLDER_NAME;
        try {
            File fileDir = new File(logFilePath);
            if (!fileDir.exists() || !fileDir.isDirectory()) { //如果文件夹不存在 则建立新文件夹
                return;
            }
            File[] files = fileDir.listFiles();
            if (files.length > 3) {
                for(int i=0 ;i < files.length ;i++) {
                    files[i].delete();
                }
            } else {
                for(int i=0 ;i < files.length ;i++)
                {
                    if(files[i].isFile())
                    {
                        String filename = files[i].getName();
                        if(isNeedDelete(filename))
                        {
                            files[i].delete();
                        }
                    } else {
                        files[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerError(TAG,"Exception e: "+e);
        }

    }


    public void setLogChecked(boolean checked) {
        m_bLogChecked = checked;
    }

    public void logFileCheck() {
        if (m_bLogChecked) {
            return;
        }
        m_bLogChecked = true;
        String logFilePath = Utils.getExternalStorageDirectory();
        String mFilePath = logFilePath + "/" + LOG_FILE_NAME;
        File file = new File(mFilePath);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis=new FileInputStream(file);
                long available = fis.available();
                LoggerDebug(TAG,"logFileCheck available: "+available);
                if((available >= 60*1048576)) { //大于60M
                    checkLogFile();
                    String logFileName = TcnShareUseData.getInstance().getMachineID()+ "-"+getLogFileStartAndEndTime(mFilePath)+".txt";
                    FileOperation.instance().copyFile(mFilePath,logFilePath+"/"+LOG_FOLDER_NAME,logFileName);
                    clearLog(file);
                } else if(0 == available) {
                    clearLog(file);
                } else {

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LoggerError(TAG,"logFileCheck FileNotFoundException e: "+e);
            } catch (IOException e) {
                e.printStackTrace();
                LoggerError(TAG,"logFileCheck 3 IOException e: "+e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LoggerError(TAG,"logFileCheck 4 IOException e: "+e);
                    }
                }

            }
        }
    }
}
