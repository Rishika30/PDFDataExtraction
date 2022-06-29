package com.springboot.dataextraction.model;

public class PDFFile {

	private String base64;

	public PDFFile() {

	}

	public PDFFile(String base64) {
		super();
		this.base64 = base64;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
