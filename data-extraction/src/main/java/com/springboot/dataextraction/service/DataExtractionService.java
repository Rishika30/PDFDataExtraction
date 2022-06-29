package com.springboot.dataextraction.service;

import java.util.List;
import java.util.Map;

import com.springboot.dataextraction.model.PDFFile;
import com.springboot.dataextraction.model.PDFSignatureInfo;

public interface DataExtractionService {

	Map<String, String> getMetadata(PDFFile file);
	String getExtractedText(PDFFile file);
	List<PDFSignatureInfo> getSignatureInfo(PDFFile file);
}
