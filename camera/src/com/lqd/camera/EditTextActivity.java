package com.lqd.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditTextActivity extends Activity {

	private List<String> spinnerList = new ArrayList<String>();   
	Spinner mySpinner;
	private ArrayAdapter<String> adapter;    
	String path;
	String selectFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.edittxt);
		path = ((MainActivity) MainActivity.mainActivity).getPath();
		//遍历根目录下bmCamera目录里面的TXT文件名到spinnerList列表中
		getTxtFiles(path);
        //填充spinnerList数据到下拉框中
        fillSpinner();
	}
	
	public void getTxtFiles(String filePath){  
		File file = new File(filePath);
        File files[] = file.listFiles();  
        String fileName="";
        if(files != null){  
            for (File f : files){  
                if(f.isFile()){  
                	fileName = f.getName();
                	try{
                		spinnerList.add(fileName.substring(0,fileName.lastIndexOf(".")));
                	}catch(Exception e){
                		
                	}
                }
            }  
        }  
    } 
	
	//填充SPINNER数据
	private void fillSpinner(){
		mySpinner = (Spinner)findViewById(R.id.selectfile_spinner);    
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerList);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        mySpinner.setAdapter(adapter);    
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                selectFile = path + "/" + adapter.getItem(arg2) +".txt";
                showData(selectFile);
                /* 将mySpinner 显示*/    
                arg0.setVisibility(View.VISIBLE);    
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
                arg0.setVisibility(View.VISIBLE);    
            }    
        });  
	}
	
	void showData(String fileName){
		EditText et = (EditText) findViewById(R.id.filecontent_e);
		et.setText(new FileService().getFileContent(fileName));
	}
	
	public void btnSave(View v){
		EditText et = (EditText) findViewById(R.id.filecontent_e);
		try {
			new FileService().saveFile(et.getText().toString(), selectFile);
			Toast.makeText(this, AppResource.saveSuccess, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(this, AppResource.saveFail, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
	}
	
	public void btnCancel(View v){
		
		
	}



}
