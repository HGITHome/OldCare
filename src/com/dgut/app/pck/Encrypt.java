package com.dgut.app.pck;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dgut.app.AppConstants;
import com.dgut.main.Constants;

public class Encrypt {

	  public static String MD5(String input)
	  {
	    return MD5(input, Charset.defaultCharset());
	  }

	  public static String MD5(String input, String charset)
	  {
	    return MD5(input, Charset.forName(charset));
	  }

	  public static String MD5(String input, Charset charset)
	  {
	    MessageDigest md = null;
	    try
	    {
	      md = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	      e.printStackTrace();
	    }

	    md.update(input.getBytes(charset));

	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	      'a', 'b', 'c', 'd', 'e', 'f' };
	    byte[] tmp = md.digest();
	    char[] str = new char[32];

	    int k = 0;
	    for (int i = 0; i < 16; i++) {
	      byte byte0 = tmp[i];
	      str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
	      str[(k++)] = hexDigits[(byte0 & 0xF)];
	    }

	    String result = new String(str);

	    return result;
	  }
//
//	  public static String encryptSES(String input, String key)
//	    throws Exception
//	  {
//	    return encryptSES(input, key, Charset.forName("GB2312"));
//	  }
//
//	  public static String encryptSES(String input, String key, Charset charset)
//	    throws Exception
//	  {
//	    Ses ses = new Ses(key);
//
//	    byte[] byte_input = input.getBytes(charset);
//	    int len = ses.getEncryptResultLength(byte_input);
//
//	    byte[] output = new byte[len];
//	    ses.encrypt(byte_input, output);
//
//	    return new BASE64Encoder().encode(output);
//	  }
//
//	  public static String decryptSES(String input, String key)
//	    throws Exception
//	  {
//	    return decryptSES(input, key, Charset.forName("GB2312"));
//	  }
//
//	  public static String decryptSES(String input, String key, Charset charset)
//	    throws Exception
//	  {
//	    Ses ses = new Ses(key);
//
//	    byte[] byte_input = new BASE64Decoder().decodeBuffer(input);
//	    byte[] temp_output = new byte[input.length()];
//
//	    int output_len = ses.decrypt(byte_input, byte_input.length, temp_output);
//
//	    byte[] ouput = new byte[output_len];
//	    System.arraycopy(temp_output, 0, ouput, 0, output_len);
//
//	    return new String(ouput, charset);
//	  }
//   加密
	  public static String encrypt3DES(String input, String key)
	  {
	    return encrypt3DES(input, key, Charset.forName("GB2312"));
	  }

	  public static String encrypt3DES(String input, String key, Charset charset)
	  {
	    try
	    {
	      return Byte.byte2hex(Des.encrypt(input.getBytes(charset.name()), key.getBytes()));
	    }
	    catch (Exception localException) {
	    }
	    return "";
	  }
//解密
	  public static String decrypt3DES(String input, String key)
	  {
	    return decrypt3DES(input, key, Charset.forName("GB2312"));
	  }
	
	  public static String decrypt3DES(String input, String key, Charset charset)
	  {
	    try
	    {
	      return new String(Des.decrypt(Byte.hex2byte(input.getBytes()), key.getBytes()), charset.name());
	    }
	    catch (Exception localException) {
	    }
	    return "";
	  }
	  
	  public static void main(String[] args){
		  String signData="_t=2016-07-21 02:15:07&mobile=13416955362&opt=2&pwd=778904ADF14F58F4";
		  String date=signData+"IAASIDuioponuYBIUNLIK123ikoIO";
		  System.out.println(date);
		  String secretCode=Encrypt.MD5(date,"utf-8");
		  System.out.println(secretCode);
		  System.out.println("----------");
		  System.out.println(Encrypt.encrypt3DES("123", Constants.ENCRYPTION_KEY,Charset.forName("utf-8")));
		  System.out.println(Encrypt.decrypt3DES("778904ADF14F58F4", Constants.ENCRYPTION_KEY,Charset.forName("GB2312")));
	  }
}
