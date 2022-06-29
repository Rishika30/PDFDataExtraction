package com.springboot.dataextraction.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dataextraction.model.PDFFile;
import com.springboot.dataextraction.model.PDFSignatureInfo;
import com.springboot.dataextraction.service.DataExtractionService;

@RestController
@RequestMapping("/extract")
public class DataExtractionController {

	private DataExtractionService dataExtractionService;

	public DataExtractionController(DataExtractionService dataExtractionService) {
		super();
		this.dataExtractionService = dataExtractionService;
	}

	@GetMapping("/text")
	public ResponseEntity<String> getExtractedText(@RequestBody PDFFile file) {
		return new ResponseEntity<String>(dataExtractionService.getExtractedText(file), HttpStatus.OK);
	}

	@GetMapping("/metadata")
	public ResponseEntity<Map<String, String>> getMetaData(@RequestBody PDFFile file) {
		return new ResponseEntity<Map<String, String>>(dataExtractionService.getMetadata(file), HttpStatus.OK);
	}

	@GetMapping("/sigInfo")
	public ResponseEntity<List<PDFSignatureInfo>> getSignatureInfo(@RequestBody PDFFile file){
		return new ResponseEntity<List<PDFSignatureInfo>>(dataExtractionService.getSignatureInfo(file), HttpStatus.OK);
	}
}
