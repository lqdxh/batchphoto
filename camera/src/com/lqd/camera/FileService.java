package com.lqd.camera;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.util.EncodingUtils;

public class FileService {

	public void saveFile(String content, String fileName) throws IOException{
		BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(fileName)); 
		buf.write(content.getBytes());
		buf.flush();
		buf.close();
	}
	
	public String getFileContent(String fileName){
		File file = new File(fileName);   
	    BufferedReader reader;   
	    StringBuffer res = new StringBuffer();
        FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);   
	        in.mark(4);   
	        byte[] first3bytes = new byte[3];   
	        in.read(first3bytes);   
	        in.reset();   
	        if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB   
	                && first3bytes[2] == (byte) 0xBF) {// utf-8   
	              reader = new BufferedReader(new InputStreamReader(in, "utf-8"));   
	        } else if (first3bytes[0] == (byte) 0xFF   
	                && first3bytes[1] == (byte) 0xFE) {   
	              reader = new BufferedReader(new InputStreamReader(in, "unicode"));   
	        } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {   
	              reader = new BufferedReader(new InputStreamReader(in,  "utf-16be"));   
	        } else if (first3bytes[0] == (byte) 0xFF  && first3bytes[1] == (byte) 0xFF) {   
	              reader = new BufferedReader(new InputStreamReader(in,  "utf-16le"));   
	        } else if (first3bytes[0] == (byte) 0xE6  && first3bytes[1] == (byte) 0xB5) {   
	              reader = new BufferedReader(new InputStreamReader(in));   
	        } else {   
	              reader = new BufferedReader(new InputStreamReader(in, "GBK"));   
	        }
        
	        
	        String line;
			String tempName;
			int count = 1;
			boolean validate = ValidUtils.valid();
			StringBuffer s = new StringBuffer();
			String name;
			String photo;
			while ((line=reader.readLine())!=null){
				
				if ( !validate ){
					if (count>MainActivity.MAXPERSONS) break;
				}
				
				String temp = EncodingUtils.getString(line.getBytes(),"utf-8");
				String[] split = temp.split("\\s{1,}");
		        int arrLen = split.length;
		        if (arrLen >= 2){
		        	s.setLength(0);
		        	for(int i=0;i<arrLen-1;i++){
		        		s.append(split[i]);
		        	}
		        	name = s.toString();
		        	photo = split[arrLen-1];
		        	res.append(name + "      " + photo +"\r\n");
					count++;
		        }
				
			}
			fis.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return res.toString();
	}
}
