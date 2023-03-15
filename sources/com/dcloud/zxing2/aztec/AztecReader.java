package com.dcloud.zxing2.aztec;

import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.Reader;
import com.dcloud.zxing2.Result;
import java.util.Map;

public final class AztecReader implements Reader {
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, (Map<DecodeHintType, ?>) null);
    }

    public void reset() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005d A[LOOP:0: B:28:0x005b->B:29:0x005d, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.dcloud.zxing2.Result decode(com.dcloud.zxing2.BinaryBitmap r6, java.util.Map<com.dcloud.zxing2.DecodeHintType, ?> r7) throws com.dcloud.zxing2.NotFoundException, com.dcloud.zxing2.FormatException {
        /*
            r5 = this;
            com.dcloud.zxing2.aztec.detector.Detector r0 = new com.dcloud.zxing2.aztec.detector.Detector
            com.dcloud.zxing2.common.BitMatrix r6 = r6.getBlackMatrix()
            r0.<init>(r6)
            r6 = 0
            r1 = 0
            com.dcloud.zxing2.aztec.AztecDetectorResult r2 = r0.detect(r6)     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.dcloud.zxing2.ResultPoint[] r3 = r2.getPoints()     // Catch:{ NotFoundException -> 0x002b, FormatException -> 0x0025 }
            com.dcloud.zxing2.aztec.decoder.Decoder r4 = new com.dcloud.zxing2.aztec.decoder.Decoder     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r4.<init>()     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            com.dcloud.zxing2.common.DecoderResult r2 = r4.decode(r2)     // Catch:{ NotFoundException -> 0x0023, FormatException -> 0x0021 }
            r4 = r3
            r3 = r1
            r1 = r2
            r2 = r3
            goto L_0x002f
        L_0x0021:
            r2 = move-exception
            goto L_0x0027
        L_0x0023:
            r2 = move-exception
            goto L_0x002d
        L_0x0025:
            r2 = move-exception
            r3 = r1
        L_0x0027:
            r4 = r3
            r3 = r2
            r2 = r1
            goto L_0x002f
        L_0x002b:
            r2 = move-exception
            r3 = r1
        L_0x002d:
            r4 = r3
            r3 = r1
        L_0x002f:
            if (r1 != 0) goto L_0x004e
            r1 = 1
            com.dcloud.zxing2.aztec.AztecDetectorResult r0 = r0.detect(r1)     // Catch:{ NotFoundException -> 0x0046, FormatException -> 0x0044 }
            com.dcloud.zxing2.ResultPoint[] r4 = r0.getPoints()     // Catch:{ NotFoundException -> 0x0046, FormatException -> 0x0044 }
            com.dcloud.zxing2.aztec.decoder.Decoder r1 = new com.dcloud.zxing2.aztec.decoder.Decoder     // Catch:{ NotFoundException -> 0x0046, FormatException -> 0x0044 }
            r1.<init>()     // Catch:{ NotFoundException -> 0x0046, FormatException -> 0x0044 }
            com.dcloud.zxing2.common.DecoderResult r1 = r1.decode(r0)     // Catch:{ NotFoundException -> 0x0046, FormatException -> 0x0044 }
            goto L_0x004e
        L_0x0044:
            r6 = move-exception
            goto L_0x0047
        L_0x0046:
            r6 = move-exception
        L_0x0047:
            if (r2 != 0) goto L_0x004d
            if (r3 == 0) goto L_0x004c
            throw r3
        L_0x004c:
            throw r6
        L_0x004d:
            throw r2
        L_0x004e:
            if (r7 == 0) goto L_0x0065
            com.dcloud.zxing2.DecodeHintType r0 = com.dcloud.zxing2.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            java.lang.Object r7 = r7.get(r0)
            com.dcloud.zxing2.ResultPointCallback r7 = (com.dcloud.zxing2.ResultPointCallback) r7
            if (r7 == 0) goto L_0x0065
            int r0 = r4.length
        L_0x005b:
            if (r6 >= r0) goto L_0x0065
            r2 = r4[r6]
            r7.foundPossibleResultPoint(r2)
            int r6 = r6 + 1
            goto L_0x005b
        L_0x0065:
            com.dcloud.zxing2.Result r6 = new com.dcloud.zxing2.Result
            java.lang.String r7 = r1.getText()
            byte[] r0 = r1.getRawBytes()
            com.dcloud.zxing2.BarcodeFormat r2 = com.dcloud.zxing2.BarcodeFormat.AZTEC
            r6.<init>(r7, r0, r4, r2)
            java.util.List r7 = r1.getByteSegments()
            if (r7 == 0) goto L_0x007f
            com.dcloud.zxing2.ResultMetadataType r0 = com.dcloud.zxing2.ResultMetadataType.BYTE_SEGMENTS
            r6.putMetadata(r0, r7)
        L_0x007f:
            java.lang.String r7 = r1.getECLevel()
            if (r7 == 0) goto L_0x008a
            com.dcloud.zxing2.ResultMetadataType r0 = com.dcloud.zxing2.ResultMetadataType.ERROR_CORRECTION_LEVEL
            r6.putMetadata(r0, r7)
        L_0x008a:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.aztec.AztecReader.decode(com.dcloud.zxing2.BinaryBitmap, java.util.Map):com.dcloud.zxing2.Result");
    }
}
