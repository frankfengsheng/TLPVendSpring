package com.tcn.funcommon.vend.protocol.DriveControl;

/**
 * Created by Administrator on 2017/7/1.
 */
public class DriveUtility {
    public static String getChecksumHex(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "";
        }
        if (hexdata.startsWith("0x") || hexdata.startsWith("0X")) {
            hexdata = hexdata.substring(2);
        }
        if (hexdata.length() == 1) {
            hexdata = "0"+hexdata;
        }

        int total = 0;
        int len = hexdata.length();
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2);
        }
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

    public static long hexStringToDecimal(String hexData) {
        if(hexData==null||hexData.length()<1) {
            throw new RuntimeException("字符串不合法");
        }
        long sum=0;
        int iLength = hexData.length();
        for (int i = 0; i < iLength; i++) {
            long iData = 1 ;
            String tmp = hexData.substring(i,i+1);
            if ("A".equalsIgnoreCase(tmp)) {
                iData = 10;
            } else if ("B".equalsIgnoreCase(tmp)) {
                iData = 11;
            } else if ("C".equalsIgnoreCase(tmp)) {
                iData = 12;
            } else if ("D".equalsIgnoreCase(tmp)) {
                iData = 13;
            } else if ("E".equalsIgnoreCase(tmp)) {
                iData = 14;
            } else if ("F".equalsIgnoreCase(tmp)) {
                iData = 15;
            } else if ("0".equals(tmp)) {
                iData = 0;
            } else if ("1".equals(tmp)) {
                iData = 1;
            } else if ("2".equals(tmp)) {
                iData = 2;
            } else if ("3".equals(tmp)) {
                iData = 3;
            } else if ("4".equals(tmp)) {
                iData = 4;
            } else if ("5".equals(tmp)) {
                iData = 5;
            } else if ("6".equals(tmp)) {
                iData = 6;
            } else if ("7".equals(tmp)) {
                iData = 7;
            } else if ("8".equals(tmp)) {
                iData = 8;
            } else if ("9".equals(tmp)) {
                iData = 9;
            } else {

            }
            for (int j = 0; j < (iLength - i - 1); j++) {
                iData = iData * 16;
            }
            sum = sum + iData;
        }
        return sum;
    }

    //有符号
    public static short hex2StringToDecimal(String hex2Data) {
        if ((null == hex2Data) || (hex2Data.length() != 2) || (hex2Data.contains("0x")) || (hex2Data.contains("0X"))) {
            return -1;
        }
        int ret = Integer.parseInt(hex2Data, 16);
        ret = ((ret & 0x80) > 0) ? (ret - 0x100) : (ret);
        return (short) ret;
    }

    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        if (hexString.startsWith("0x") || hexString.startsWith("0X")) {
            hexString = hexString.substring(2);
        }
        if (hexString.length() == 1) {
            hexString = "0"+hexString;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
