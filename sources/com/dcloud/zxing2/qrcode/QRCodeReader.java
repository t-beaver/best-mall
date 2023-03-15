package com.dcloud.zxing2.qrcode;

import android.os.Handler;
import android.os.Message;
import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.ChecksumException;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.Reader;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.ResultMetadataType;
import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.common.BitMatrix;
import com.dcloud.zxing2.common.DecoderResult;
import com.dcloud.zxing2.common.DetectorResult;
import com.dcloud.zxing2.qrcode.decoder.Decoder;
import com.dcloud.zxing2.qrcode.decoder.QRCodeDecoderMetaData;
import com.dcloud.zxing2.qrcode.detector.Detector;
import java.util.List;
import java.util.Map;

public class QRCodeReader implements Reader {
    private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
    private final Decoder decoder = new Decoder();
    private Handler hostHandler;

    private static BitMatrix extractPureBits(BitMatrix bitMatrix) throws NotFoundException {
        int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();
        int[] bottomRightOnBit = bitMatrix.getBottomRightOnBit();
        if (topLeftOnBit == null || bottomRightOnBit == null) {
            throw NotFoundException.getNotFoundInstance();
        }
        float moduleSize = moduleSize(topLeftOnBit, bitMatrix);
        int i = topLeftOnBit[1];
        int i2 = bottomRightOnBit[1];
        int i3 = topLeftOnBit[0];
        int i4 = bottomRightOnBit[0];
        if (i3 >= i4 || i >= i2) {
            throw NotFoundException.getNotFoundInstance();
        }
        int i5 = i2 - i;
        if (i5 != i4 - i3) {
            i4 = i3 + i5;
        }
        int round = Math.round(((float) ((i4 - i3) + 1)) / moduleSize);
        int round2 = Math.round(((float) (i5 + 1)) / moduleSize);
        if (round <= 0 || round2 <= 0) {
            throw NotFoundException.getNotFoundInstance();
        } else if (round2 == round) {
            int i6 = (int) (moduleSize / 2.0f);
            int i7 = i + i6;
            int i8 = i3 + i6;
            int i9 = (((int) (((float) (round - 1)) * moduleSize)) + i8) - i4;
            if (i9 > 0) {
                if (i9 <= i6) {
                    i8 -= i9;
                } else {
                    throw NotFoundException.getNotFoundInstance();
                }
            }
            int i10 = (((int) (((float) (round2 - 1)) * moduleSize)) + i7) - i2;
            if (i10 > 0) {
                if (i10 <= i6) {
                    i7 -= i10;
                } else {
                    throw NotFoundException.getNotFoundInstance();
                }
            }
            BitMatrix bitMatrix2 = new BitMatrix(round, round2);
            for (int i11 = 0; i11 < round2; i11++) {
                int i12 = ((int) (((float) i11) * moduleSize)) + i7;
                for (int i13 = 0; i13 < round; i13++) {
                    if (bitMatrix.get(((int) (((float) i13) * moduleSize)) + i8, i12)) {
                        bitMatrix2.set(i13, i11);
                    }
                }
            }
            return bitMatrix2;
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    private static float moduleSize(int[] iArr, BitMatrix bitMatrix) throws NotFoundException {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        int i = iArr[0];
        boolean z = true;
        int i2 = iArr[1];
        int i3 = 0;
        while (i < width && i2 < height) {
            if (z != bitMatrix.get(i, i2)) {
                i3++;
                if (i3 == 5) {
                    break;
                }
                z = !z;
            }
            i++;
            i2++;
        }
        if (i != width && i2 != height) {
            return ((float) (i - iArr[0])) / 7.0f;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, ChecksumException, FormatException {
        return decode(binaryBitmap, (Map<DecodeHintType, ?>) null);
    }

    /* access modifiers changed from: protected */
    public final Decoder getDecoder() {
        return this.decoder;
    }

    public void reset() {
    }

    public void updateHandler(Handler handler) {
        this.hostHandler = handler;
    }

    public final Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        ResultPoint[] resultPointArr;
        DecoderResult decoderResult;
        DecoderResult decoderResult2;
        if (map == null || !map.containsKey(DecodeHintType.PURE_BARCODE)) {
            DetectorResult detect = new Detector(binaryBitmap.getBlackMatrix()).detect(map);
            try {
                decoderResult2 = this.decoder.decode(detect.getBits(), map);
            } catch (Exception unused) {
                if (!(this.hostHandler == null || detect == null)) {
                    Message message = new Message();
                    message.what = 1010;
                    message.obj = Float.valueOf(detect.moduleSize);
                    this.hostHandler.sendMessage(message);
                }
                decoderResult2 = null;
            }
            if (decoderResult2 != null) {
                DecoderResult decoderResult3 = decoderResult2;
                resultPointArr = detect.getPoints();
                decoderResult = decoderResult3;
            } else {
                throw NotFoundException.getNotFoundInstance();
            }
        } else {
            decoderResult = this.decoder.decode(extractPureBits(binaryBitmap.getBlackMatrix()), map);
            resultPointArr = NO_POINTS;
        }
        if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
            ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(resultPointArr);
        }
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), resultPointArr, BarcodeFormat.QR_CODE);
        result.textCharset = decoderResult.textCharset;
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String eCLevel = decoderResult.getECLevel();
        if (eCLevel != null) {
            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, eCLevel);
        }
        if (decoderResult.hasStructuredAppend()) {
            result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE, Integer.valueOf(decoderResult.getStructuredAppendSequenceNumber()));
            result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY, Integer.valueOf(decoderResult.getStructuredAppendParity()));
        }
        return result;
    }
}
