package io.chino.api.blob;

import java.io.*;
import java.security.MessageDigest;

public class SHA1Calc {

	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("SHA1");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	// see this How-to for a faster way to convert
	// a byte array to a HEX string
	public static String getSHA1Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		StringBuilder result = new StringBuilder();
		for (byte aB : b) {
			result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	public static String getSHA1Checksum(byte[] digest){
		StringBuilder result = new StringBuilder();
		for (byte aDigest : digest) {
			result.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}
	
	
}