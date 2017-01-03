package com.lqd.camera;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.net.ConnectivityManager;

public class ValidUtils {

	static final String SERVERADDR="http://www.2011kj.cn:8080/valid.asp";
	static final String SUGUSTSERVERADDR="http://www.2011kj.cn:8080/suguest.asp";
	
	public static boolean valid(){
		//如果activeState 或 activeSIM与curSIM不同则未激活
		int n1= ((MainActivity)MainActivity.mainActivity).getActiveState() ;
		String s1 =((MainActivity)MainActivity.mainActivity).getActiveSIM() ;
		String s2 =((MainActivity)MainActivity.mainActivity).getCurSIM() ;
		try {
			s2 = MD5.getMD5(s2);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if ( n1!= 1 ||  !s1.equals(s2)    ){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	/*
	 * String activePhone, String activeSIM
	 */
	public static String validSoft(String activePhone, String activeSIM) {
		int res = 0;
		ArrayList pairs = new ArrayList();
		pairs.add(new BasicNameValuePair( "phoneNum",activePhone ));
		pairs.add(new BasicNameValuePair( "phoneSIM",activeSIM ));
		
		String returnStr = null;
		try {
			returnStr = RequestByHttpPost.doPost(pairs, SERVERADDR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == returnStr){
			return null;
		}else{
			String temp = EncodingUtils.getString(returnStr.getBytes(),"utf-8");
			return temp;
		}
		
	}
	
	public static String sendSuguest(String suguest) {
		ArrayList pairs = new ArrayList();
		pairs.add(new BasicNameValuePair( "suguests",EncodingUtils.getString(suguest.getBytes(),"utf-8") ));
		String returnStr = null;
		try {
			returnStr = RequestByHttpPost.doPost(pairs, SUGUSTSERVERADDR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == returnStr){
			return null;
		}else{
			String temp = EncodingUtils.getString(returnStr.getBytes(),"utf-8");
			return temp;
		}
		
	}
}
