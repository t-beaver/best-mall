package com.dcloud.zxing2.oned;

import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.ChecksumException;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.common.BitArray;
import java.util.Map;

public final class UPCAReader extends UPCEANReader {
    private final UPCEANReader ean13Reader = new EAN13Reader();

    private static Result maybeReturnResult(Result result) throws FormatException {
        String text = result.getText();
        if (text.charAt(0) == '0') {
            return new Result(text.substring(1), (byte[]) null, result.getResultPoints(), BarcodeFormat.UPC_A);
        }
        throw FormatException.getFormatInstance();
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return maybeReturnResult(this.ean13Reader.decode(binaryBitmap));
    }

    /* access modifiers changed from: protected */
    public int decodeMiddle(BitArray bitArray, int[] iArr, StringBuilder sb) throws NotFoundException {
        return this.ean13Reader.decodeMiddle(bitArray, iArr, sb);
    }

    public Result decodeRow(int i, BitArray bitArray, int[] iArr, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException, ChecksumException {
        return maybeReturnResult(this.ean13Reader.decodeRow(i, bitArray, iArr, map));
    }

    /* access modifiers changed from: package-private */
    public BarcodeFormat getBarcodeFormat() {
        return BarcodeFormat.UPC_A;
    }

    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        return maybeReturnResult(this.ean13Reader.decode(binaryBitmap, map));
    }

    public Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException, ChecksumException {
        return maybeReturnResult(this.ean13Reader.decodeRow(i, bitArray, map));
    }
}
