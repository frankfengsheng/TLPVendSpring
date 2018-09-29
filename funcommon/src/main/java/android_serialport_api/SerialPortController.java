package android_serialport_api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * Created by songjiancheng on 2016/4/11.
 */
public class SerialPortController {
    private static final String TAG = "SerialPortController";

    private static SerialPortController m_Instance = null;

    public static final int SERIAL_PORT_RECEIVE_DATA        = 70;
    public static final int SERIAL_PORT_CONFIG_ERROR        = 71;
    public static final int SERIAL_PORT_SECURITY_ERROR      = 72;
    public static final int SERIAL_PORT_UNKNOWN_ERROR       = 73;

    public static final int SERIAL_PORT_RECEIVE_DATA_NEW        = 74;

    public static final int SERIAL_PORT_RECEIVE_DATA_THIRD        = 75;

    public static final int SERIAL_PORT_RECEIVE_DATA_FOURTH       = 76;


    public static final int SERIAL_PORT_RECEIVE_NO_DATA     = 84;

    public static final int SERIAL_PORT_WRITE_DATA_STR            = 86;
    public static final int SERIAL_PORT_WRITE_DATA_NEW_STR       = 87;
    public static final int SERIAL_PORT_WRITE_DATA_BYTES            = 88;
    public static final int SERIAL_PORT_WRITE_DATA_NEW_BYTES       = 89;

    private static final int WRITE_DATA_TIME       = 300;

    private volatile int m_iTimeCount = 0;
    private volatile int m_iTimeCountNew = 0;
    private SerialPort m_SerialPort = null;
    private SerialPortFinder m_SerialPortFinder = null;
    private InputStream m_InputStream = null;
    private OutputStream m_OutputStream = null;
    private ReadThread m_ReadThread = null;

    private SerialPort m_SerialPortNew = null;
    private InputStream m_InputStreamNew = null;
    private OutputStream m_OutputStreamNew = null;
    private ReadThreadNew m_ReadThreadNew = null;

    private SerialPort m_SerialPortThird = null;
    private InputStream m_InputStreamThird = null;
    private OutputStream m_OutputStreamThird = null;
    private ReadThreadThird m_ReadThreadThird = null;

    private SerialPort m_SerialPortFourth = null;
    private InputStream m_InputStreamFourth = null;
    private OutputStream m_OutputStreamFourth = null;
    private ReadThreadFourth m_ReadThreadFourth = null;


    private Context mContext = null;

    public static synchronized SerialPortController getInstance() {
        if (null == m_Instance) {
            m_Instance = new SerialPortController();
        }
        return m_Instance;
    }

    public SerialPortController() {
        m_SerialPortFinder = new SerialPortFinder();
    }

    public void init(Context context) {
        mContext = context;
    }

    private long lastClickTime;
    public long getTimeBetween() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD;
    }

    private long lastClickTimeNew;
    public long getTimeBetweenNew() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTimeNew;
        lastClickTimeNew = time;
        return timeD;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPort(String devicekey, String baudratekey)
            throws SecurityException, IOException, InvalidParameterException {
        if (null == m_SerialPort) {
			/* Read serial port parameters */
            SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
            String path = sp.getString(devicekey, "");
            int baudrate = Integer.decode(sp.getString(baudratekey, "-1"));
			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

			/* Open the serial port */
            m_SerialPort = new SerialPort(new File(path), baudrate, 0);
            // mSerialPort = new SerialPort(new File("/dev/ttyS0"), baudrate,0);
        }
        return m_SerialPort;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPortNew(String devicekey, String baudratekey)
            throws SecurityException, IOException, InvalidParameterException {
        if (null == m_SerialPortNew) {
            /* Read serial port parameters */
            SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
            String path = sp.getString(devicekey, "");
            int baudrate = Integer.decode(sp.getString(baudratekey, "-1"));
			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            m_SerialPortNew = new SerialPort(new File(path), baudrate, 0);
            // mSerialPort = new SerialPort(new File("/dev/ttyS0"), baudrate,0);
        }

        return m_SerialPortNew;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPortThird(String devicekey, String baudratekey)
            throws SecurityException, IOException, InvalidParameterException {
        if (null == m_SerialPortThird) {
            /* Read serial port parameters */
            SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
            String path = sp.getString(devicekey, "");
            int baudrate = Integer.decode(sp.getString(baudratekey, "-1"));
			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            m_SerialPortThird = new SerialPort(new File(path), baudrate, 0);
            // mSerialPort = new SerialPort(new File("/dev/ttyS0"), baudrate,0);
        }

        return m_SerialPortThird;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPortFourth(String devicekey, String baudratekey)
            throws SecurityException, IOException, InvalidParameterException {
        if (null == m_SerialPortFourth) {
            /* Read serial port parameters */
            SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
            String path = sp.getString(devicekey, "");
            int baudrate = Integer.decode(sp.getString(baudratekey, "-1"));
			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            m_SerialPortFourth = new SerialPort(new File(path), baudrate, 0);
            // mSerialPort = new SerialPort(new File("/dev/ttyS0"), baudrate,0);
        }

        return m_SerialPortFourth;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPort(String dev,int baudrate) throws SecurityException, IOException, InvalidParameterException {
        if (m_SerialPort == null) {

			/* Open the serial port */
            m_SerialPort = new SerialPort(new File(dev), baudrate,0);
        }
        return m_SerialPort;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPortNew(String dev,int baudrate) throws SecurityException, IOException, InvalidParameterException {
        if (m_SerialPortNew == null) {
            /* Open the serial port */
            m_SerialPortNew = new SerialPort(new File(dev), baudrate,0);
        }

        return m_SerialPortNew;
    }

    /**
     * 得到串口
     */
    public SerialPort getSerialPortThird(String dev,int baudrate) throws SecurityException, IOException, InvalidParameterException {
        if (m_SerialPortThird == null) {
            /* Open the serial port */
            m_SerialPortThird = new SerialPort(new File(dev), baudrate,0);
        }

        return m_SerialPortThird;
    }


    /**
     * 得到串口
     */
    public SerialPort getSerialPortFourth(String dev,int baudrate) throws SecurityException, IOException, InvalidParameterException {
        if (m_SerialPortFourth == null) {
            /* Open the serial port */
            m_SerialPortFourth = new SerialPort(new File(dev), baudrate,0);
        }

        return m_SerialPortFourth;
    }


    public SerialPortFinder getSerialPortFinder() {
        return m_SerialPortFinder;
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPort(String devicekey, String baudratekey) {

        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPort = getSerialPort(devicekey, baudratekey);

            m_OutputStream = m_SerialPort.getOutputStream();
            m_InputStream = m_SerialPort.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThread) {
                m_ReadThread.interrupt();
                m_ReadThread = null;
            }
            m_ReadThread = new ReadThread();
            m_ReadThread.setName("ReadThread");
            m_ReadThread.setRun(true);
            m_ReadThread.setPriority(Thread.MAX_PRIORITY);
            m_ReadThread.start();
        } catch (SecurityException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_SECURITY_ERROR);
            }
        } catch (IOException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_UNKNOWN_ERROR);
            }
        } catch (InvalidParameterException e) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "1 InvalidParameterException e: " + e);
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_CONFIG_ERROR);
            }
        }
    }

    public void openSerialPort(int type, String devicekey, String baudratekey) {
        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPort = getSerialPort(devicekey, baudratekey);

            m_OutputStream = m_SerialPort.getOutputStream();
            m_InputStream = m_SerialPort.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThread) {
                m_ReadThread.interrupt();
                m_ReadThread = null;
            }
            m_ReadThread = new ReadThread();
            m_ReadThread.setName("ReadThread");
            m_ReadThread.setRun(true);
            m_ReadThread.setBoardType(type);
            m_ReadThread.setPriority(Thread.MAX_PRIORITY);
            m_ReadThread.start();
        } catch (SecurityException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_SECURITY_ERROR);
            }
        } catch (IOException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_UNKNOWN_ERROR);
            }
        } catch (InvalidParameterException e) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "2 InvalidParameterException e: " + e);
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_CONFIG_ERROR);
            }
        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPortNew(String devicekey, String baudratekey) {

        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortNew = getSerialPortNew(devicekey, baudratekey);

            m_OutputStreamNew = m_SerialPortNew.getOutputStream();
            m_InputStreamNew = m_SerialPortNew.getInputStream();

            if (null != m_ReadThreadNew) {
                m_ReadThreadNew.interrupt();
                m_ReadThreadNew = null;
            }
            m_ReadThreadNew = new ReadThreadNew();
            m_ReadThreadNew.setName("ReadThreadNew");
            m_ReadThreadNew.setRun(true);
            m_ReadThreadNew.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadNew.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    public void openSerialPortNew(int type, String devicekey, String baudratekey) {
        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortNew = getSerialPortNew(devicekey, baudratekey);

            m_OutputStreamNew = m_SerialPortNew.getOutputStream();
            m_InputStreamNew = m_SerialPortNew.getInputStream();

            if (null != m_ReadThreadNew) {
                m_ReadThreadNew.interrupt();
                m_ReadThreadNew = null;
            }
            m_ReadThreadNew = new ReadThreadNew();
            m_ReadThreadNew.setName("ReadThreadNew");
            m_ReadThreadNew.setRun(true);
            m_ReadThreadNew.setBoardType(type);
            m_ReadThreadNew.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadNew.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPortThird(String devicekey, String baudratekey) {

        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortThird = getSerialPortThird(devicekey, baudratekey);

            m_OutputStreamThird = m_SerialPortThird.getOutputStream();
            m_InputStreamThird = m_SerialPortThird.getInputStream();

            if (null != m_ReadThreadThird) {
                m_ReadThreadThird.interrupt();
                m_ReadThreadThird = null;
            }
            m_ReadThreadThird = new ReadThreadThird();
            m_ReadThreadThird.setName("ReadThreadThird");
            m_ReadThreadThird.setRun(true);
            m_ReadThreadThird.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadThird.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    public void openSerialPortThird(int type, String devicekey, String baudratekey) {
        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortThird = getSerialPortThird(devicekey, baudratekey);

            m_OutputStreamThird = m_SerialPortThird.getOutputStream();
            m_InputStreamThird = m_SerialPortThird.getInputStream();

            if (null != m_ReadThreadThird) {
                m_ReadThreadThird.interrupt();
                m_ReadThreadThird = null;
            }
            m_ReadThreadThird = new ReadThreadThird();
            m_ReadThreadThird.setName("ReadThreadThird");
            m_ReadThreadThird.setRun(true);
            m_ReadThreadThird.setBoardType(type);
            m_ReadThreadThird.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadThird.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }


    /**
     * 打开并监视串口
     */
    public void openSerialPortFourth(String devicekey, String baudratekey) {

        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortFourth = getSerialPortFourth(devicekey, baudratekey);

            m_OutputStreamFourth = m_SerialPortFourth.getOutputStream();
            m_InputStreamFourth = m_SerialPortFourth.getInputStream();

            if (null != m_ReadThreadFourth) {
                m_ReadThreadFourth.interrupt();
                m_ReadThreadFourth = null;
            }
            m_ReadThreadFourth = new ReadThreadFourth();
            m_ReadThreadFourth.setName("ReadThreadFourth");
            m_ReadThreadFourth.setRun(true);
            m_ReadThreadFourth.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadFourth.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    public void openSerialPortFourth(int type, String devicekey, String baudratekey) {
        try {
            //m_SerialPort = m_application.getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortFourth = getSerialPortFourth(devicekey, baudratekey);

            m_OutputStreamFourth = m_SerialPortFourth.getOutputStream();
            m_InputStreamFourth = m_SerialPortFourth.getInputStream();

            if (null != m_ReadThreadFourth) {
                m_ReadThreadFourth.interrupt();
                m_ReadThreadFourth = null;
            }
            m_ReadThreadFourth = new ReadThreadFourth();
            m_ReadThreadFourth.setName("ReadThreadFourth");
            m_ReadThreadFourth.setRun(true);
            m_ReadThreadFourth.setBoardType(type);
            m_ReadThreadFourth.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadFourth.start();

        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPort(String dev,int baudrate) {

        try {
            //m_SerialPort = getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPort = getSerialPort(dev, baudrate);
            m_OutputStream = m_SerialPort.getOutputStream();
            m_InputStream = m_SerialPort.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThread) {
                m_ReadThread.interrupt();
                m_ReadThread = null;
            }
            m_ReadThread = new ReadThread();
            m_ReadThread.setName("ReadThread");
            m_ReadThread.setRun(true);
            m_ReadThread.setPriority(Thread.MAX_PRIORITY);
            m_ReadThread.start();
        } catch (SecurityException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_SECURITY_ERROR);
            }
        } catch (IOException e) {
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_UNKNOWN_ERROR);
            }
        } catch (InvalidParameterException e) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "3 InvalidParameterException e: " + e);
            if (m_ReceiveHandler != null) {
                m_ReceiveHandler.sendEmptyMessage(SERIAL_PORT_CONFIG_ERROR);
            }
        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPortNew(String dev,int baudrate) {
        try {
            //m_SerialPort = getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortNew = getSerialPortNew(dev, baudrate);
            m_OutputStreamNew = m_SerialPortNew.getOutputStream();
            m_InputStreamNew = m_SerialPortNew.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThreadNew) {
                m_ReadThreadNew.interrupt();
                m_ReadThreadNew = null;
            }
            m_ReadThreadNew = new ReadThreadNew();
            m_ReadThreadNew.setName("ReadThreadNew");
            m_ReadThreadNew.setRun(true);
            m_ReadThreadNew.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadNew.start();
        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPortThird(String dev,int baudrate) {
        try {
            //m_SerialPort = getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortThird = getSerialPortThird(dev, baudrate);
            m_OutputStreamThird = m_SerialPortThird.getOutputStream();
            m_InputStreamThird = m_SerialPortThird.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThreadThird) {
                m_ReadThreadThird.interrupt();
                m_ReadThreadThird = null;
            }
            m_ReadThreadThird = new ReadThreadThird();
            m_ReadThreadThird.setName("ReadThreadThird");
            m_ReadThreadThird.setRun(true);
            m_ReadThreadThird.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadThird.start();
        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    /**
     * 打开并监视串口
     */
    public void openSerialPortFourth(String dev,int baudrate) {
        try {
            //m_SerialPort = getSerialPort("/dev/ttyS1", 19200);//（写死串口）
            m_SerialPortFourth = getSerialPortFourth(dev, baudrate);
            m_OutputStreamFourth = m_SerialPortFourth.getOutputStream();
            m_InputStreamFourth = m_SerialPortFourth.getInputStream();

			/* Create a receiving thread */
            if (null != m_ReadThreadFourth) {
                m_ReadThreadFourth.interrupt();
                m_ReadThreadFourth = null;
            }
            m_ReadThreadFourth = new ReadThreadFourth();
            m_ReadThreadFourth.setName("ReadThreadFourth");
            m_ReadThreadFourth.setRun(true);
            m_ReadThreadFourth.setPriority(Thread.MAX_PRIORITY);
            m_ReadThreadFourth.start();
        } catch (SecurityException e) {

        } catch (IOException e) {

        } catch (InvalidParameterException e) {

        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (m_OutputStream != null) {
            try {
                m_OutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_OutputStream = null;
        }

        if (m_InputStream != null) {
            try {
                m_InputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_InputStream = null;
        }

        if (m_SerialPort != null) {
            m_SerialPort.close();
            m_SerialPort.closeStream();
            m_SerialPort = null;
        }

        if (m_ReadThread != null) {
            m_ReadThread.interrupt();
            m_ReadThread = null;
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPortNew() {
        if (m_OutputStreamNew != null) {
            try {
                m_OutputStreamNew.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_OutputStreamNew = null;
        }

        if (m_InputStreamNew != null) {
            try {
                m_InputStreamNew.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_InputStreamNew = null;
        }
        if (m_SerialPortNew != null) {
            m_SerialPortNew.close();
            m_SerialPortNew.closeStream();
            m_SerialPortNew = null;
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPortThird() {
        if (m_OutputStreamThird != null) {
            try {
                m_OutputStreamThird.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_OutputStreamThird = null;
        }

        if (m_InputStreamThird != null) {
            try {
                m_InputStreamThird.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_InputStreamThird = null;
        }
        if (m_SerialPortThird != null) {
            m_SerialPortThird.close();
            m_SerialPortThird.closeStream();
            m_SerialPortThird = null;
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPortFourth() {
        if (m_OutputStreamFourth != null) {
            try {
                m_OutputStreamFourth.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_OutputStreamFourth = null;
        }

        if (m_InputStreamFourth != null) {
            try {
                m_InputStreamFourth.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m_InputStreamFourth = null;
        }
        if (m_SerialPortFourth != null) {
            m_SerialPortFourth.close();
            m_SerialPortFourth.closeStream();
            m_SerialPortFourth = null;
        }
    }

    public void writeData(String str) {
        if(null == str) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData str is null");
            return;
        }
        try {
            if (null != m_OutputStream) {
                if ((getTimeBetween() >= WRITE_DATA_TIME) || (m_iTimeCount > 1)) {
                    m_iTimeCount = 0;
                    m_OutputStream.write(str.getBytes());
                    m_OutputStream.flush();
                    TcnVendIF.getInstance().LoggerDebug(TAG, "writeData 向串口写数据  str: " + str);
                } else {
                    if (m_ReceiveHandler != null) {
                        m_iTimeCount++;
                        Message message = m_ReceiveHandler.obtainMessage();
                        message.what = SERIAL_PORT_WRITE_DATA_STR;
                        message.obj = str;
                        m_ReceiveHandler.sendMessageDelayed(message,WRITE_DATA_TIME*m_iTimeCount);
                    }
                }

            }
        } catch (IOException e) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData 向串口写数据错误 e：" + e + " str: " + str);
        }
    }

    public void writeDataImmediately(String data) {
        if ((null == m_OutputStream) || (null == data)) {
            return;
        }
        try {
            m_OutputStream.write(data.getBytes());
            m_OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataImmediatelyNew(byte[] bytes) {
        if ((null == m_OutputStreamNew) || (null == bytes)) {
            return;
        }
        try {
            m_OutputStreamNew.write(bytes);
            m_OutputStreamNew.flush();
        } catch (IOException e) {
            e.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataImmediatelyNew IOException e：" + e);
        }
    }

    public void writeDataNew(String str) {
        if(null == str) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataNew str is null");
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "writeDataNew 向串口写数据  str: " + str);
        try {
            if (null != m_OutputStreamNew) {
                if ((getTimeBetweenNew() >= WRITE_DATA_TIME) || (m_iTimeCountNew > 1)) {
                    m_iTimeCountNew = 0;
                    m_OutputStreamNew.write(str.getBytes());
                    m_OutputStreamNew.flush();
                } else {
                    if (m_ReceiveHandlerNew != null) {
                        m_iTimeCountNew++;
                        Message message = m_ReceiveHandlerNew.obtainMessage();
                        message.what = SERIAL_PORT_WRITE_DATA_NEW_STR;
                        message.obj = str;
                        m_ReceiveHandlerNew.sendMessageDelayed(message,WRITE_DATA_TIME*m_iTimeCountNew);
                    }
                }

            }
        } catch (IOException e) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataNew 向串口写数据错误 e：" + e + " str: " + str);
        }
    }

    public void writeDataImmediately(byte[] bytes) {
        if ((null == m_OutputStream) || (null == bytes)) {
            return;
        }
        try {
            m_OutputStream.write(bytes);
            m_OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataImmediately IOException e：" + e);
        }
    }

    public void writeData(byte[] bytes) {
        if(null == bytes) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData bytes is null");
            return;
        }
        try {
            if (null != m_OutputStream) {
                if ((getTimeBetween() >= WRITE_DATA_TIME) || (m_iTimeCount > 1)) {
                    m_iTimeCount = 0;
                    m_OutputStream.write(bytes);
                    m_OutputStream.flush();
                } else {
                    if (m_ReceiveHandler != null) {
                        m_iTimeCount++;
                        Message message = m_ReceiveHandler.obtainMessage();
                        message.what = SERIAL_PORT_WRITE_DATA_BYTES;
                        message.obj = bytes;
                        m_ReceiveHandler.sendMessageDelayed(message,WRITE_DATA_TIME*m_iTimeCount);
                    }
                }

            }
        } catch (IOException e) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeData 向串口写数据错误 e：" + e);
        }
    }

    public void writeDataNew(byte[] bytes) {
        if(null == bytes) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataNew bytes is null");
            return;
        }
        try {
            if (null != m_OutputStreamNew) {
                if ((getTimeBetweenNew() >= WRITE_DATA_TIME)  || (m_iTimeCountNew > 1)) {
                    m_iTimeCountNew = 0;
                    m_OutputStreamNew.write(bytes);
                    m_OutputStreamNew.flush();
                } else {
                    if (m_ReceiveHandlerNew != null) {
                        m_iTimeCountNew++;
                        Message message = m_ReceiveHandlerNew.obtainMessage();
                        message.what = SERIAL_PORT_WRITE_DATA_NEW_BYTES;
                        message.obj = bytes;
                        m_ReceiveHandlerNew.sendMessageDelayed(message,WRITE_DATA_TIME*m_iTimeCountNew);
                    }
                }

            }
        } catch (IOException e) {
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataNew 向串口写数据错误 e：" + e);
        }
    }

    public void writeDataImmediatelyThird(byte[] bytes) {
        if ((null == m_OutputStreamThird) || (null == bytes)) {
            return;
        }
        try {
            m_OutputStreamThird.write(bytes);
            m_OutputStreamThird.flush();
        } catch (IOException e) {
            e.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataImmediatelyThird IOException e：" + e);
        }
    }

    public void writeDataImmediatelyFourth(byte[] bytes) {
        if ((null == m_OutputStreamFourth) || (null == bytes)) {
            return;
        }
        try {
            m_OutputStreamFourth.write(bytes);
            m_OutputStreamFourth.flush();
        } catch (IOException e) {
            e.printStackTrace();
            TcnVendIF.getInstance().LoggerError(TAG, "writeDataImmediatelyFourth IOException e：" + e);
        }
    }

    private Handler m_ReceiveHandler = null;
    public void setHandler(Handler receiveHandler) {
        m_ReceiveHandler = receiveHandler;
    }

    private Handler m_ReceiveHandlerNew = null;
    public void setHandlerNew(Handler receiveHandlerNew) {
        m_ReceiveHandlerNew = receiveHandlerNew;
    }

    private Handler m_ReceiveHandlerThird = null;
    public void setHandlerThird(Handler receiveHandlerThird) {
        m_ReceiveHandlerThird = receiveHandlerThird;
    }

    private Handler m_ReceiveHandlerFourth = null;
    public void setHandlerFourth(Handler receiveHandlerFourth) {
        m_ReceiveHandlerFourth = receiveHandlerFourth;
    }

    private Handler m_ReceiveHandlerCard = null;
    public void setHandlerCard(Handler receiveHandlerCard) {
        m_ReceiveHandlerCard = receiveHandlerCard;
    }

    private Handler m_ReceiveHandlerMDB = null;
    public void setHandlerMDB(Handler receiveHandlerMdb) {
        m_ReceiveHandlerMDB = receiveHandlerMdb;
    }

    private Handler m_ReceiveHandlerDgtDsp = null;
    public void setHandlerDgtDsp(Handler receiveHandlerDgtDsp) {
        m_ReceiveHandlerDgtDsp = receiveHandlerDgtDsp;
    }

    private Handler m_ReceiveHandlerKey = null;
    public void setHandlerKey(Handler receiveHandlerKey) {
        m_ReceiveHandlerKey = receiveHandlerKey;
    }


    /**
     * 监视串口得到串口发送的数据
     *
     * @author Administrator
     *
     */
    private class ReadThread extends Thread {

        private boolean bIsRun;
        private int iBoardType = -1;

        public boolean getRun() {
            return bIsRun;
        }

        public void setRun(boolean bRun) {
            this.bIsRun = bRun;
        }

        public void setBoardType(int type) {
            this.iBoardType = type;
        }

        @Override
        public void run() {
            super.run();

            if (null == m_InputStream) {
                TcnVendIF.getInstance().LoggerError(TAG, "readthread m_InputStream is null");
                return;
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "进入readthread啦------getRun(): " + getRun());
            byte[] buffer = null;
            int byteCount = 0;
            while (bIsRun && !isInterrupted()) {

                try {
                    buffer = new byte[512];
                    byteCount = m_InputStream.read(buffer);
                    if (byteCount > 0) {
                        if (m_ReceiveHandler != null) {
                            Message message = m_ReceiveHandler.obtainMessage();
                            message.what = SERIAL_PORT_RECEIVE_DATA; //发送到另一个线程
                            message.arg1 = byteCount;
                            message.arg2 = iBoardType;
                            message.obj = buffer;
                            m_ReceiveHandler.sendMessage(message);
                        } else {
                            TcnVendIF.getInstance().LoggerError(TAG, "m_ReceiveHandler is null" );
                        }
                    }

                } catch (IOException e) {
                    TcnVendIF.getInstance().LoggerError(TAG, "ReadThread IOException e: " + e);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * 监视串口得到串口发送的数据
     *
     * @author Administrator
     *
     */
    private class ReadThreadNew extends Thread {

        private boolean bIsRun;
        private int iBoardType = -1;

        public boolean getRun() {
            return bIsRun;
        }

        public void setRun(boolean bRun) {
            this.bIsRun = bRun;
        }

        public void setBoardType(int type) {
            this.iBoardType = type;
        }

        @Override
        public void run() {
            super.run();

            if (null == m_InputStreamNew) {
                TcnVendIF.getInstance().LoggerError(TAG, "ReadThreadNew m_InputStreamNew is null");
                return;
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "进入ReadThreadNew啦------getRun(): " + getRun());
            byte[] buffer = null;
           // String readNew = "";
            int byteCount = 0;

            while (bIsRun && !isInterrupted()) {

                try {
                    buffer = new byte[512];
                    byteCount = m_InputStreamNew.read(buffer);
                    if (byteCount > 0) {
                        if (m_ReceiveHandlerNew != null) {
                            Message message = m_ReceiveHandlerNew.obtainMessage();
                            message.what = SERIAL_PORT_RECEIVE_DATA_NEW;
                            message.arg1 = byteCount;
                            message.arg2 = iBoardType;
                            message.obj = buffer;
                            m_ReceiveHandlerNew.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    TcnVendIF.getInstance().LoggerError(TAG, "ReadThreadNew IOException e: " + e);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * 监视串口得到串口发送的数据
     *
     * @author Administrator
     *
     */
    private class ReadThreadThird extends Thread {

        private boolean bIsRun;
        private int iBoardType = -1;

        public boolean getRun() {
            return bIsRun;
        }

        public void setRun(boolean bRun) {
            this.bIsRun = bRun;
        }

        public void setBoardType(int type) {
            this.iBoardType = type;
        }

        @Override
        public void run() {
            super.run();

            if (null == m_InputStreamThird) {
                TcnVendIF.getInstance().LoggerError(TAG, "ReadThreadThird m_InputStreamThird is null");
                return;
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "进入ReadThreadNew啦------getRun(): " + getRun());
            byte[] buffer = null;
            // String readNew = "";
            int byteCount = 0;

            while (bIsRun && !isInterrupted()) {

                try {
                    buffer = new byte[512];
                    byteCount = m_InputStreamThird.read(buffer);
                    if (byteCount > 0) {
                        if (m_ReceiveHandlerThird != null) {
                            Message message = m_ReceiveHandlerThird.obtainMessage();
                            message.what = SERIAL_PORT_RECEIVE_DATA_THIRD;
                            message.arg1 = byteCount;
                            message.arg2 = iBoardType;
                            message.obj = buffer;
                            m_ReceiveHandlerThird.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    TcnVendIF.getInstance().LoggerError(TAG, "ReadThreadThird IOException e: " + e);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * 监视串口得到串口发送的数据
     *
     * @author Administrator
     *
     */
    private class ReadThreadFourth extends Thread {

        private boolean bIsRun;
        private int iBoardType = -1;

        public boolean getRun() {
            return bIsRun;
        }

        public void setRun(boolean bRun) {
            this.bIsRun = bRun;
        }

        public void setBoardType(int type) {
            this.iBoardType = type;
        }

        @Override
        public void run() {
            super.run();

            if (null == m_InputStreamFourth) {
                TcnVendIF.getInstance().LoggerError(TAG, "ReadThreadFourth m_InputStreamFourth is null");
                return;
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "进入ReadThreadNew啦------getRun(): " + getRun());
            byte[] buffer = null;
            // String readNew = "";
            int byteCount = 0;

            while (bIsRun && !isInterrupted()) {

                try {
                    buffer = new byte[512];
                    byteCount = m_InputStreamFourth.read(buffer);
                    if (byteCount > 0) {
                        if (m_ReceiveHandlerFourth != null) {
                            Message message = m_ReceiveHandlerFourth.obtainMessage();
                            message.what = SERIAL_PORT_RECEIVE_DATA_FOURTH;
                            message.arg1 = byteCount;
                            message.arg2 = iBoardType;
                            message.obj = buffer;
                            m_ReceiveHandlerFourth.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    TcnVendIF.getInstance().LoggerError(TAG, "m_InputStreamFourth IOException e: " + e);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

}
