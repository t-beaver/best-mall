package net.lingala.zip4j.model;

public class DigitalSignature extends ZipHeader {
    private String signatureData;
    private int sizeOfData;

    public String getSignatureData() {
        return this.signatureData;
    }

    public int getSizeOfData() {
        return this.sizeOfData;
    }

    public void setSignatureData(String str) {
        this.signatureData = str;
    }

    public void setSizeOfData(int i) {
        this.sizeOfData = i;
    }
}
