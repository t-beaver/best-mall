package com.dcloud.zxing2.oned;

import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.ChecksumException;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.Reader;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.ResultMetadataType;
import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.common.BitArray;
import java.util.Arrays;
import java.util.Map;

public abstract class OneDReader implements Reader {
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007d A[Catch:{ ReaderException -> 0x00c6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00c5 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.dcloud.zxing2.Result doDecode(com.dcloud.zxing2.BinaryBitmap r22, java.util.Map<com.dcloud.zxing2.DecodeHintType, ?> r23) throws com.dcloud.zxing2.NotFoundException {
        /*
            r21 = this;
            r0 = r23
            int r1 = r22.getWidth()
            int r2 = r22.getHeight()
            com.dcloud.zxing2.common.BitArray r3 = new com.dcloud.zxing2.common.BitArray
            r3.<init>(r1)
            int r4 = r2 >> 1
            r5 = 0
            r6 = 1
            if (r0 == 0) goto L_0x001f
            com.dcloud.zxing2.DecodeHintType r7 = com.dcloud.zxing2.DecodeHintType.TRY_HARDER
            boolean r7 = r0.containsKey(r7)
            if (r7 == 0) goto L_0x001f
            r7 = 1
            goto L_0x0020
        L_0x001f:
            r7 = 0
        L_0x0020:
            if (r7 == 0) goto L_0x0025
            r8 = 8
            goto L_0x0026
        L_0x0025:
            r8 = 5
        L_0x0026:
            int r8 = r2 >> r8
            int r8 = java.lang.Math.max(r6, r8)
            if (r7 == 0) goto L_0x0030
            r7 = r2
            goto L_0x0032
        L_0x0030:
            r7 = 15
        L_0x0032:
            r9 = 0
        L_0x0033:
            if (r9 >= r7) goto L_0x00e6
            int r10 = r9 + 1
            int r11 = r10 / 2
            r9 = r9 & 1
            if (r9 != 0) goto L_0x003f
            r9 = 1
            goto L_0x0040
        L_0x003f:
            r9 = 0
        L_0x0040:
            if (r9 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            int r11 = -r11
        L_0x0044:
            int r11 = r11 * r8
            int r11 = r11 + r4
            if (r11 < 0) goto L_0x00e6
            if (r11 >= r2) goto L_0x00e6
            r9 = r22
            com.dcloud.zxing2.common.BitArray r3 = r9.getBlackRow(r11, r3)     // Catch:{ NotFoundException -> 0x00da }
            r12 = 0
        L_0x0052:
            r13 = 2
            if (r12 >= r13) goto L_0x00d5
            if (r12 != r6) goto L_0x0075
            r3.reverse()
            if (r0 == 0) goto L_0x0075
            com.dcloud.zxing2.DecodeHintType r13 = com.dcloud.zxing2.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            boolean r14 = r0.containsKey(r13)
            if (r14 == 0) goto L_0x0075
            java.util.EnumMap r14 = new java.util.EnumMap
            java.lang.Class<com.dcloud.zxing2.DecodeHintType> r15 = com.dcloud.zxing2.DecodeHintType.class
            r14.<init>(r15)
            r14.putAll(r0)
            r14.remove(r13)
            r13 = r21
            r0 = r14
            goto L_0x0077
        L_0x0075:
            r13 = r21
        L_0x0077:
            com.dcloud.zxing2.Result r14 = r13.decodeRow(r11, r3, r0)     // Catch:{ ReaderException -> 0x00c6 }
            if (r12 != r6) goto L_0x00c5
            com.dcloud.zxing2.ResultMetadataType r15 = com.dcloud.zxing2.ResultMetadataType.ORIENTATION     // Catch:{ ReaderException -> 0x00c6 }
            r16 = 180(0xb4, float:2.52E-43)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r16)     // Catch:{ ReaderException -> 0x00c6 }
            r14.putMetadata(r15, r6)     // Catch:{ ReaderException -> 0x00c6 }
            com.dcloud.zxing2.ResultPoint[] r6 = r14.getResultPoints()     // Catch:{ ReaderException -> 0x00c6 }
            if (r6 == 0) goto L_0x00c5
            com.dcloud.zxing2.ResultPoint r15 = new com.dcloud.zxing2.ResultPoint     // Catch:{ ReaderException -> 0x00c6 }
            r16 = r0
            float r0 = (float) r1
            r18 = r6[r5]     // Catch:{ ReaderException -> 0x00c8 }
            float r18 = r18.getX()     // Catch:{ ReaderException -> 0x00c8 }
            float r18 = r0 - r18
            r19 = 1065353216(0x3f800000, float:1.0)
            r20 = r1
            float r1 = r18 - r19
            r18 = r6[r5]     // Catch:{ ReaderException -> 0x00ca }
            float r5 = r18.getY()     // Catch:{ ReaderException -> 0x00ca }
            r15.<init>(r1, r5)     // Catch:{ ReaderException -> 0x00ca }
            r1 = 0
            r6[r1] = r15     // Catch:{ ReaderException -> 0x00ca }
            com.dcloud.zxing2.ResultPoint r5 = new com.dcloud.zxing2.ResultPoint     // Catch:{ ReaderException -> 0x00ca }
            r15 = 1
            r17 = r6[r15]     // Catch:{ ReaderException -> 0x00cb }
            float r17 = r17.getX()     // Catch:{ ReaderException -> 0x00cb }
            float r0 = r0 - r17
            float r0 = r0 - r19
            r17 = r6[r15]     // Catch:{ ReaderException -> 0x00cb }
            float r1 = r17.getY()     // Catch:{ ReaderException -> 0x00cb }
            r5.<init>(r0, r1)     // Catch:{ ReaderException -> 0x00cb }
            r6[r15] = r5     // Catch:{ ReaderException -> 0x00cb }
        L_0x00c5:
            return r14
        L_0x00c6:
            r16 = r0
        L_0x00c8:
            r20 = r1
        L_0x00ca:
            r15 = 1
        L_0x00cb:
            int r12 = r12 + 1
            r0 = r16
            r1 = r20
            r5 = 0
            r6 = 1
            goto L_0x0052
        L_0x00d5:
            r13 = r21
            r9 = r10
            goto L_0x0033
        L_0x00da:
            r13 = r21
            r20 = r1
            r15 = 1
            r9 = r10
            r1 = r20
            r5 = 0
            r6 = 1
            goto L_0x0033
        L_0x00e6:
            r13 = r21
            com.dcloud.zxing2.NotFoundException r0 = com.dcloud.zxing2.NotFoundException.getNotFoundInstance()
            goto L_0x00ee
        L_0x00ed:
            throw r0
        L_0x00ee:
            goto L_0x00ed
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.oned.OneDReader.doDecode(com.dcloud.zxing2.BinaryBitmap, java.util.Map):com.dcloud.zxing2.Result");
    }

    protected static float patternMatchVariance(int[] iArr, int[] iArr2, float f) {
        int length = iArr.length;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            i += iArr[i3];
            i2 += iArr2[i3];
        }
        if (i < i2) {
            return Float.POSITIVE_INFINITY;
        }
        float f2 = (float) i;
        float f3 = f2 / ((float) i2);
        float f4 = f * f3;
        float f5 = 0.0f;
        for (int i4 = 0; i4 < length; i4++) {
            int i5 = iArr[i4];
            float f6 = ((float) iArr2[i4]) * f3;
            float f7 = (float) i5;
            float f8 = f7 > f6 ? f7 - f6 : f6 - f7;
            if (f8 > f4) {
                return Float.POSITIVE_INFINITY;
            }
            f5 += f8;
        }
        return f5 / f2;
    }

    protected static void recordPattern(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        int i2 = 0;
        Arrays.fill(iArr, 0, length, 0);
        int size = bitArray.getSize();
        if (i < size) {
            boolean z = !bitArray.get(i);
            while (i < size) {
                if (bitArray.get(i) ^ z) {
                    iArr[i2] = iArr[i2] + 1;
                } else {
                    i2++;
                    if (i2 == length) {
                        break;
                    }
                    iArr[i2] = 1;
                    z = !z;
                }
                i++;
            }
            if (i2 == length) {
                return;
            }
            if (i2 != length - 1 || i != size) {
                throw NotFoundException.getNotFoundInstance();
            }
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    protected static void recordPatternInReverse(BitArray bitArray, int i, int[] iArr) throws NotFoundException {
        int length = iArr.length;
        boolean z = bitArray.get(i);
        while (i > 0 && length >= 0) {
            i--;
            if (bitArray.get(i) != z) {
                length--;
                z = !z;
            }
        }
        if (length < 0) {
            recordPattern(bitArray, i + 1, iArr);
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, (Map<DecodeHintType, ?>) null);
    }

    public abstract Result decodeRow(int i, BitArray bitArray, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException;

    public void reset() {
    }

    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, FormatException {
        try {
            return doDecode(binaryBitmap, map);
        } catch (NotFoundException e) {
            if (!(map != null && map.containsKey(DecodeHintType.TRY_HARDER)) || !binaryBitmap.isRotateSupported()) {
                throw e;
            }
            BinaryBitmap rotateCounterClockwise = binaryBitmap.rotateCounterClockwise();
            Result doDecode = doDecode(rotateCounterClockwise, map);
            Map<ResultMetadataType, Object> resultMetadata = doDecode.getResultMetadata();
            int i = 270;
            if (resultMetadata != null) {
                ResultMetadataType resultMetadataType = ResultMetadataType.ORIENTATION;
                if (resultMetadata.containsKey(resultMetadataType)) {
                    i = (((Integer) resultMetadata.get(resultMetadataType)).intValue() + 270) % 360;
                }
            }
            doDecode.putMetadata(ResultMetadataType.ORIENTATION, Integer.valueOf(i));
            ResultPoint[] resultPoints = doDecode.getResultPoints();
            if (resultPoints != null) {
                int height = rotateCounterClockwise.getHeight();
                for (int i2 = 0; i2 < resultPoints.length; i2++) {
                    resultPoints[i2] = new ResultPoint((((float) height) - resultPoints[i2].getY()) - 1.0f, resultPoints[i2].getX());
                }
            }
            return doDecode;
        }
    }
}
