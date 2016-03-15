
package io.chino.api.blob;

public class GetBlobResponse {

	String filename;
	long size;
	String sha1;
	String md5;
	String path;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

   public String toString(){
	   String s="";
	   s+="filename: "+filename;
	   s+=", size: "+size+" bytes";
	   s+=", sha1: "+sha1;
	   s+=", md5: "+md5;
	   s+=", path: "+path;
	   return s;
   }


}
