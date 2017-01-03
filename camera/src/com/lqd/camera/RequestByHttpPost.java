package com.lqd.camera;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class RequestByHttpPost {

 public static String TIME_OUT = "������ʱ";
 
 public static String doPost(List<NameValuePair> params,String url) throws Exception{
  String result = null;
      // �½�HttpPost����
      HttpPost httpPost = new HttpPost(url);
      // �����ַ���
      HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
      // ���ò���ʵ��
      httpPost.setEntity(entity);
      // ��ȡHttpClient����
      HttpClient httpClient = new DefaultHttpClient();
      //���ӳ�ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
      //����ʱ
      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
      try {
       // ��ȡHttpResponseʵ��
          HttpResponse httpResp = httpClient.execute(httpPost);
          // �ж��ǹ�����ɹ�
          if (httpResp.getStatusLine().getStatusCode() == 200) {
           // ��ȡ���ص�����
           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
          } else {
          }
      } catch (ConnectTimeoutException e){
       result = TIME_OUT;
      }
     
      return result;
 }
}