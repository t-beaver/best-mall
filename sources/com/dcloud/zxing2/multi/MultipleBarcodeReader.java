package com.dcloud.zxing2.multi;

import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.Result;
import java.util.Map;

public interface MultipleBarcodeReader {
    Result[] decodeMultiple(BinaryBitmap binaryBitmap) throws NotFoundException;

    Result[] decodeMultiple(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException;
}
