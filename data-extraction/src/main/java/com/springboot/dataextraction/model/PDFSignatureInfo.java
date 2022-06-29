package com.springboot.dataextraction.model;

import java.util.Date;

public class PDFSignatureInfo {


    public String reason;
    public String name;
    public String subFilter;
    public String filter;
    public String contactInfo;
    public String location;

    public Date signDate;

    public boolean coversWholeDocument;
    public boolean isSelfSigned;

    public String signatureVerified;

}
