package com.springboot.dataextraction.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.springboot.dataextraction.model.PDFFile;
import com.springboot.dataextraction.model.PDFSignatureInfo;

@Service
public class DataExractionServiceImpl implements DataExtractionService {

	@Override
	public String getExtractedText(PDFFile file) {

		AutoDetectParser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext parseContext = new ParseContext();
		byte[] bytes = Base64Utils.decodeFromString(file.getBase64());
		try {
			InputStream stream = new ByteArrayInputStream(bytes);
			parser.parse(stream, handler, metadata, parseContext);
		}catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (TikaException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		}
			
		String content = handler.toString();

		return content;

	}

	@Override
	public Map<String, String> getMetadata(PDFFile file) {

		AutoDetectParser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext parseContext = new ParseContext();
		byte[] bytes = Base64Utils.decodeFromString(file.getBase64());
		try {
			InputStream stream = new ByteArrayInputStream(bytes);
			parser.parse(stream, handler, metadata, parseContext);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (TikaException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		}

		Map<String, String> pdfMetadata = new HashMap<>();
		String[] metadataNames = metadata.names();
		for (String name : metadataNames) {
			pdfMetadata.put(name, metadata.get(name));
		}
		return pdfMetadata;
	}

	@Override
	public List<PDFSignatureInfo> getSignatureInfo(PDFFile file) {
		byte[] bytes = Base64Utils.decodeFromString(file.getBase64());
		InputStream stream = new ByteArrayInputStream(bytes);
		List<PDFSignatureInfo> lpsi = new ArrayList<PDFSignatureInfo>();
		try (PDDocument document = PDDocument.load(stream)) {
			for (PDSignature sig : document.getSignatureDictionaries()) {
				PDFSignatureInfo psi = new PDFSignatureInfo();
				lpsi.add(psi);

				COSDictionary sigDict = sig.getCOSObject();

				psi.reason = sig.getReason();
				psi.name = sig.getName();
				psi.signDate = sig.getSignDate().getTime();
				psi.subFilter = sig.getSubFilter();
				psi.contactInfo = sig.getContactInfo();
				psi.filter = sig.getFilter();
				psi.location = sig.getLocation();

				int[] byteRange = sig.getByteRange();
				if (byteRange.length != 4) {
					throw new IOException("Signature byteRange must have 4 items");
				} else {
					long fileLen = bytes.length;
					long rangeMax = byteRange[2] + (long) byteRange[3];
					int contentLen = sigDict.getString(COSName.CONTENTS).length() * 2 + 2;
					if (fileLen != rangeMax || byteRange[0] != 0 || byteRange[1] + contentLen != byteRange[2]) {
						psi.coversWholeDocument = false;
					} else {
						psi.coversWholeDocument = true;
					}
				}

			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return lpsi;
	}

}
