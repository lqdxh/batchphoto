package com.lqd.camera;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.http.util.EncodingUtils;

import android.util.Config;
import android.util.Log;
import android.widget.Toast;

class Person{
	
	String name;
	String photoName;
	String pinyin;
	
	
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public Person(String name, String photoName, String pinyin) {
		super();
		this.name = name;
		this.photoName = photoName;
		this.pinyin = pinyin;
	}
	
}

public class GenData {
	
	private List<Person> personList = new ArrayList<Person>();
	private List<String> fileNameList = new ArrayList<String>();
	
	
	public List<Person> getPersonList() {
		return personList;
	}

//	public void GenData2(String path){
//		File file = new File(path);
//		try{
//			InputStream instream = new FileInputStream(file) ;
//			if (instream != null){
//				InputStreamReader inputreader = new InputStreamReader(instream,"gb2312");
//				BufferedReader buffreader = new BufferedReader(inputreader);
//				String line;
//				Person person;
//				int count = 1;
//				boolean validate = ValidUtils.valid();
//				while ((line=buffreader.readLine())!=null){
//					
//					if ( !validate ){
//						if (count>MAXPERSONS) break;
//					}
//					String temp = EncodingUtils.getString(line.getBytes(),"utf-8");
//					person = parseLine(temp);
//					if (person != null){
//						personList.add(person);
//						count++;
//					}
//				}
//				instream.close();
//			}
//		}catch(IOException e){
//			
//		}
//	}
	
	public GenData(String fileName){
		File file = new File(fileName);   
	    BufferedReader reader;   

        FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			
			
//			ReadTxtFile readTextFile = new ReadTxtFile();
//			reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(fis), readTextFile.getFileCode(file)));   
			
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
	        
//	        if (first3bytes[0] == (byte) 0xE6 && first3bytes[1] == (byte) 0xB8   
//	                && first3bytes[2] == (byte) 0xAC) {// utf-8   
//	              reader = new BufferedReader(new InputStreamReader(in, "utf-8"));   
//	        } else if (first3bytes[0] == (byte) 0xFF   
//	                && first3bytes[1] == (byte) 0xFE) {   
//	              reader = new BufferedReader(new InputStreamReader(in, "unicode"));   
//	        } else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {   
//	              reader = new BufferedReader(new InputStreamReader(in,  "utf-16be"));   
//	        } else if (first3bytes[0] == (byte) 0xFF  && first3bytes[1] == (byte) 0xFF) {   
//	              reader = new BufferedReader(new InputStreamReader(in,  "utf-16le"));   
//	        } else if (first3bytes[0] == (byte) 0xE6  && first3bytes[1] == (byte) 0xB5) {   
//	              reader = new BufferedReader(new InputStreamReader(in));   
//	        } else {   
//	              reader = new BufferedReader(new InputStreamReader(in, "GBK"));   
//	        }
	        
	        
	        String line;
			Person person;
			String photoName;
			int photoCount = 0;
			String tempName;
			int count = 1;
			boolean validate = ValidUtils.valid();
			while ((line=reader.readLine())!=null){
				
				if ( !validate ){
					if (count>MainActivity.MAXPERSONS) break;
				}
				
				String temp = EncodingUtils.getString(line.getBytes(),"utf-8");
//				String temp = readTextFile.changeCharset(line, readTextFile.GBK);
				
				person = parseLine(temp);
				if (person != null){
					photoCount = 0;
					photoName = person.getPhotoName();
					tempName = photoName + (photoCount==0?"":"_"+photoCount);
					while (fileNameList.indexOf(tempName ) != -1) {
						photoCount++;
						tempName = photoName + "_" + photoCount;
					}
					fileNameList.add(tempName);
					person.setPhotoName(tempName);
					File jpgfile = new File(MainActivity.getInstance().getPath() + '/' +MainActivity.getInstance().getSelectFile()+ '/' +tempName + ".jpg");
					if (!MainActivity.getInstance().getHidePhotoed() || !jpgfile.exists()){
						personList.add(person);	
					}
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
        
	}
	
	public String getCharacterPinYin(char c)
    {
		String[] pinyin = null;
		pinyin = PinyinHelper.toHanyuPinyinStringArray(c);
		// 如果c不是汉字，toHanyuPinyinStringArray会返回null，为了支持英文字符，返回原字符
		if (pinyin == null){
			return String.valueOf(c).toLowerCase();
		}
		// 只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0].substring(0, 1);
//		return "";
    }
	
	
	//解析每一行
	private Person parseLine(String line) {
        if (line == null)
            return null;
        String[] split = line.split("\\s{1,}");
        int arrLen = split.length;
        if (arrLen < 2){
            return null;
        }else{
        	StringBuffer s = new StringBuffer();
        	for(int i=0;i<arrLen-1;i++){
        		s.append(split[i]);
        	}
        	String name = s.toString();
        	String phone = split[arrLen-1];
        	String pinyin = "";
        	for (int i=0; i<name.length(); i++){
        		pinyin = pinyin + getCharacterPinYin(name.charAt(i));
        	}
//        	String pinyin = getFirstPinYin(EncodingUtils.getString(name.getBytes(),"unicode"));
        	
//        	("bmCamra", name + ":" + pinyin);
        	return new Person(s.toString(),split[arrLen-1],pinyin);
        }
        
    }

}
