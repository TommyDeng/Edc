package com.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.io.IOUtils;

/**
 * DES文件加密
 * 
 * @author t1lixx
 * 
 */
public class CipherDESUtil {
	// 要求写在代码中
	public final static String DEFAULT_KEY = "19850812";

	public static void encrypt(String key, InputStream is, OutputStream os)
			throws Exception {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

	public static void decrypt(String key, InputStream is, OutputStream os)
			throws Exception {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

	/**
	 * 加密方法
	 * 
	 * @param key
	 * @param inputFile
	 *            明文文件输入路径如:d:/aaa/aaa.xls
	 * @param outputFile
	 *            密文文件输出路径如:d:/aaa/aaa.xls
	 * @throws Exception
	 */
	public static void encrypt(String key, String inputFile, String outputFile)
			throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputFile);
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, fis, fos);
	}

	/**
	 * 解密方法
	 * 
	 * @param key
	 * @param inputFile
	 *            密文文件输入路径如:d:/aaa/aaa.xls
	 * @param outputFile
	 *            明文文件输出路径如:d:/aaa/aaa.xls
	 * @throws Exception
	 */
	public static void decrypt(String key, String inputFile, String outputFile)
			throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputFile);
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, fis, fos);
	}

	public static void encryptOrDecrypt(String key, int mode, InputStream is,
			OutputStream os) throws Exception {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for
													// SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

	public static void doCopy(InputStream is, OutputStream os)
			throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}

	public static void main(String[] args) throws Exception {
		String inputFileString = "D:/temp/in.rar";
		String outFileString = "D:/temp/out.rar";
		String tempFileString = "D:/temp/temp.rar";

		CipherDESUtil.encrypt(DEFAULT_KEY, inputFileString, tempFileString);
		CipherDESUtil.decrypt(DEFAULT_KEY, tempFileString, outFileString);
		
	}
}