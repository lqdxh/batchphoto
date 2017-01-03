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

	/** ���ַ�����ת����UTF-8 */
	public String toUTF_8(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_8);
	}

	/** ���ַ�����ת����UTF-16BE */
	public String toUTF_16BE(String str) throws UnsupportedEncodingException {
		return this.changeCharset(str, UTF_16BE);
	}
	
	/*
	 * ȡ���ļ������ʽ
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
	 * �ַ�������ת����ʵ�ַ���
	 * @param str ��ת�����ַ���
	 * @param newCharset Ŀ�����
	 */
	public String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// ��Ĭ���ַ���������ַ�������ϵͳ��أ�����Ĭ��ΪGBK
			byte[] bs = str.getBytes();
			return new String(bs, newCharset); // ���µ��ַ����������ַ���
		}
		return null;
	}


 	/**
	 * �ַ�������ת����ʵ�ַ���
	 * @param str
	 *            ��ת�����ַ���
	 * @param oldCharset
	 *            Դ�ַ���
	 * @param newCharset
	 *            Ŀ���ַ���
	 */
	public String changeCharset(String str, String oldCharset, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// ��Դ�ַ���������ַ���
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}
	

}
