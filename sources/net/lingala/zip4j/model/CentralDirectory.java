package net.lingala.zip4j.model;

import java.util.ArrayList;
import java.util.List;

public class CentralDirectory {
    private DigitalSignature digitalSignature = new DigitalSignature();
    private List<FileHeader> fileHeaders = new ArrayList();

    public DigitalSignature getDigitalSignature() {
        return this.digitalSignature;
    }

    public List<FileHeader> getFileHeaders() {
        return this.fileHeaders;
    }

    public void setDigitalSignature(DigitalSignature digitalSignature2) {
        this.digitalSignature = digitalSignature2;
    }

    public void setFileHeaders(List<FileHeader> list) {
        this.fileHeaders = list;
    }
}
