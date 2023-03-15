package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.oned.UPCEReader;

public final class ProductResultParser extends ResultParser {
    public ProductParsedResult parse(Result result) {
        BarcodeFormat barcodeFormat = result.getBarcodeFormat();
        if (barcodeFormat != BarcodeFormat.UPC_A && barcodeFormat != BarcodeFormat.UPC_E && barcodeFormat != BarcodeFormat.EAN_8 && barcodeFormat != BarcodeFormat.EAN_13) {
            return null;
        }
        String massagedText = ResultParser.getMassagedText(result);
        if (!ResultParser.isStringOfDigits(massagedText, massagedText.length())) {
            return null;
        }
        return new ProductParsedResult(massagedText, (barcodeFormat == BarcodeFormat.UPC_E && massagedText.length() == 8) ? UPCEReader.convertUPCEtoUPCA(massagedText) : massagedText);
    }
}
