package com.lqd.camera;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		WebView web = ((WebView)this.findViewById(R.id.webView1));
		String loc = Locale.getDefault().getCountry();
		if (loc.equalsIgnoreCase("CN")){
			web.loadDataWithBaseURL("file:///android_asset/",getFromAssets("help.html"), "text/html", "utf-8", null);
		}else{
			web.loadDataWithBaseURL("file:///android_asset/",getFromAssets("help-en.html"), "text/html", "utf-8", null);
		}
		
	}
	
	public String getFromAssets(String fileName) {  
        String result = "";  
        try {  
            InputStream in = this.getResources().getAssets().open(fileName); 
            InputStreamReader inputreader = new InputStreamReader(in,"GB2312");
            BufferedReader buffreader = new BufferedReader(inputreader);
            StringBuffer sb = new StringBuffer();
            String line;
            while((line=buffreader.readLine())!=null){
            	sb.append(line);
            }
            result = sb.toString();
            in.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return  result;
    }  

}
