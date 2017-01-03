package com.lqd.camera;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ReadTxtFile {
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String UTF_16BE = "UTF-16BE";
	public static final String UNICODE = "Unicode";

	/** 将字符编码转换成UTF-8 */
	public String toUTF_8(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_8);
	}

	/** 将字符编码转换成UTF-16BE */
	public String toUTF_16BE(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_16BE);
	}
	
	/*
	 * 取得文件编码格式
	 */
	public String getFileCode(File file) throws IOException{
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		switch (p)
		{
		case 0xefbb:
			code = UTF_8;
			break;
		case 0xfffe:
			code = UNICODE;
			break;
		case 0xfeff:
			code = UTF_16BE;
			break;
		default:
			code = GBK;
		}

		return code;
	}

	/*
	 * 字符串编码转换的实现方法
	 * @param str 待转换的字符串
	 * @param newCharset 目标编码
	 */
	public String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用默认字符编码解码字符串。与系统相关，中文默认为GBK
			byte[] bs = str.getBytes();
			return new String(bs, newCharset); // 用新的字符编码生成字符串
		}
		return null;
	}


 	/**
	 * 字符串编码转换的实现方法
	 * @param str
	 *            待转换的字符串
	 * @param oldCharset
	 *            源字符集
	 * @param newCharset
	 *            目标字符集
	 */
	public String changeCharset(String str, String oldCharset, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用源字符编码解码字符串
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}
	

}
