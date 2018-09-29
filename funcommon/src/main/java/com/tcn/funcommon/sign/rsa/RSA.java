
package com.tcn.funcommon.sign.rsa;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA{

	//数字签名，密钥算法
	public static final String KEY_ALGORITHM="RSA";

	/**
	 * 数字签名
	 * 签名/验证算法
	 * */
	private static final String SIGN_ALGORITHMS_256 = "SHA256withRSA";
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	public static final String  SIGN_ALGORITHMS_MD5 = "MD5withRSA";

	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String sign_algorithms, String input_charset)
	{
        try
        {
        	PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
        	//KeyFactory keyf 				= KeyFactory.getInstance("RSA");
			KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
        	PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = null;
			if ((null == sign_algorithms) || (sign_algorithms.length() < 1)) {
				signature = java.security.Signature
						.getInstance(SIGN_ALGORITHMS);
			} else {
				signature = java.security.Signature
						.getInstance(sign_algorithms);
			}

            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );

            byte[] signed = signature.sign();
            return Base64.encode(signed);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
			TcnVendIF.getInstance().LoggerError("FreeMudIF", "FreeMudIF 11 sign e: "+e);
        }

        return null;
    }

	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key 支付宝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	        byte[] encodedKey = Base64.decode(ali_public_key);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS_MD5);

			signature.initVerify(pubKey);
			signature.update( content.getBytes(input_charset) );

			boolean bverify = signature.verify( Base64.decode(sign) );
			return bverify;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	* 解密
	* @param content 密文
	* @param private_key 商户私钥
	* @param input_charset 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }


	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}

	/**
	 * SHA256WithRSA签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static String sign256(String data, String privateKey,String input_charset) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
			SignatureException, UnsupportedEncodingException {

		try {
			PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			//KeyFactory keyf 				= KeyFactory.getInstance("RSA");
			KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
			PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

			Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS_256);

			signature.initSign(priKey);
			signature.update(data.getBytes(input_charset) );

			byte[] signed = signature.sign();
			return Base64.encode(signed);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			TcnVendIF.getInstance().LoggerError("sign256", "sign256 e: "+e);
		}
		return null;
	}

	public static boolean verify256(String data, String sign, String publicKey,String input_charset){
		if(data == null || sign == null || publicKey == null) {
			return false;
		}

		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			Signature signature = Signature.getInstance(SIGN_ALGORITHMS_256);

			signature.initVerify(pubKey);
			signature.update(data.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

}
