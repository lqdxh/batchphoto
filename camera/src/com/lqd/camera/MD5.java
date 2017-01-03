package com.lqd.camera;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 对外提供getMD5(String)方法
 * @author randyjia
 *
 */
public class MD5 {
	
	public static String getMD5(String val) throws NoSuchAlgorithmException{
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch(Exception e){
			
		}
		
		if (null == md5){
			return val;
		}else{
			md5.update(val.getBytes());
			byte[] m = md5.digest();//加密
			String s =getString(m);
			return s;
		}
		
}
	private static String getString(byte[] b){
		StringBuffer sb = new StringBuffer();
		int ch;
		 for(int i = 0; i < b.length; i ++){
			 ch = b[i]; 
			 if(ch<0) ch+= 256; 
			 if(ch<16) 
				 sb.append("0"); 
			 sb.append(Integer.toHexString(ch)); 
		 }
		 return sb.toString();
}
}

