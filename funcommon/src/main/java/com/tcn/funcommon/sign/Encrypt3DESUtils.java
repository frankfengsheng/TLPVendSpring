package com.tcn.funcommon.sign;

import com.tcn.funcommon.sign.rsa.Base64;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/7/24.
 */
public class Encrypt3DESUtils {
    byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
    /**
     * ECB加密,不要IV
     * @param keyData 密钥
     * @param dataContent 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static String des3EncodeECB(String keyData, String dataContent,String charsetName) {
        String strOut = null;
        if ((null == keyData) || (keyData.length() < 1) || (null == dataContent) || (keyData.length() < 1)) {
            return strOut;
        }

        try {
            byte[] key = Base64.decode(keyData);
            byte[] dataTmp = dataContent.getBytes(charsetName);
            byte[] data = null;
            if ("Unicode".equalsIgnoreCase(charsetName)) {
                data = new byte[dataTmp.length - 2];
                int j = 0;
                for (int i = 0; i < dataTmp.length; i++) {
                    if (i >= 2) {
                        data[j] = dataTmp[i];
                        j++;
                    }
                }
            } else {
                data = dataTmp;
            }

            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] bOut = cipher.doFinal(data);
            strOut = Base64.encode(bOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strOut;
    }

    /**
     * ECB解密,不要IV
     * @param key 密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, deskey);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }

    /**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);

        return bOut;
    }

    /**
     * CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {

        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(data);

        return bOut;

    }

}
