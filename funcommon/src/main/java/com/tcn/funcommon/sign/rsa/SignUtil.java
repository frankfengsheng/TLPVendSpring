package com.tcn.funcommon.sign.rsa;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUtil {
	/** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilterWithoutSignType(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static String getParmas(Map<String,String> parmas){
        List<String> keys = new ArrayList<String>(parmas.keySet());

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = parmas.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static String getSignEncodeData(String signData, String private_key, String sign_algorithms) {
        String singedStr=RSA.sign(signData, private_key, sign_algorithms,"UTF-8");
        /*try {
            singedStr = URLEncoder.encode(singedStr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return singedStr;
    }

    public static String getSignData(Map<String,String> parmas, String private_key, String sign_algorithms) {
        Map<String,String> mParmas=paraFilter(parmas);
        String signStr=createLinkString(mParmas);
        if (signStr == null) {
            return null;
        }
        String singedStr=RSA.sign(signStr, private_key, sign_algorithms, "UTF-8");
        return singedStr;
    }

    public static String getSign256Data(boolean containSignType,Map<String,String> parmas, String private_key) {
        Map<String,String> mParmas = paraFilterWithoutSignType(parmas);
        if (containSignType) {
            mParmas = paraFilterWithoutSignType(parmas);
        } else {
            mParmas=paraFilter(parmas);
        }
        String signStr = createLinkString(mParmas);
        if (signStr == null) {
            return null;
        }
        String singedStr = null;
        try {
            singedStr = RSA.sign256(signStr, private_key, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return singedStr;
    }


    public static String getSignEncodeData(Map<String,String> parmas, String private_key, String sign_algorithms) {
        Map<String,String> mParmas=paraFilter(parmas);
        String signStr=createLinkString(mParmas);
        String singedStr=RSA.sign(signStr, private_key, sign_algorithms, "UTF-8");
        if (singedStr == null) {
            return null;
        }
        try {
            singedStr = URLEncoder.encode(singedStr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return singedStr;
    }
}
