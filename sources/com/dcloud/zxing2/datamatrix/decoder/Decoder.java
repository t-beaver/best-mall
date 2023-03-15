package com.dcloud.zxing2.datamatrix.decoder;

import com.dcloud.zxing2.ChecksumException;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.common.BitMatrix;
import com.dcloud.zxing2.common.DecoderResult;
import com.dcloud.zxing2.common.reedsolomon.GenericGF;
import com.dcloud.zxing2.common.reedsolomon.ReedSolomonDecoder;
import com.dcloud.zxing2.common.reedsolomon.ReedSolomonException;
import io.dcloud.common.DHInterface.IApp;

public final class Decoder {
    private final ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(GenericGF.DATA_MATRIX_FIELD_256);

    private void correctErrors(byte[] bArr, int i) throws ChecksumException {
        int length = bArr.length;
        int[] iArr = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = bArr[i2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        }
        try {
            this.rsDecoder.decode(iArr, bArr.length - i);
            for (int i3 = 0; i3 < i; i3++) {
                bArr[i3] = (byte) iArr[i3];
            }
        } catch (ReedSolomonException unused) {
            throw ChecksumException.getChecksumInstance();
        }
    }

    public DecoderResult decode(boolean[][] zArr) throws FormatException, ChecksumException {
        int length = zArr.length;
        BitMatrix bitMatrix = new BitMatrix(length);
        for (int i = 0; i < length; i++) {
            for (int i2 = 0; i2 < length; i2++) {
                if (zArr[i][i2]) {
                    bitMatrix.set(i2, i);
                }
            }
        }
        return decode(bitMatrix);
    }

    public DecoderResult decode(BitMatrix bitMatrix) throws FormatException, ChecksumException {
        BitMatrixParser bitMatrixParser = new BitMatrixParser(bitMatrix);
        DataBlock[] dataBlocks = DataBlock.getDataBlocks(bitMatrixParser.readCodewords(), bitMatrixParser.getVersion());
        int length = dataBlocks.length;
        int i = 0;
        for (DataBlock numDataCodewords : dataBlocks) {
            i += numDataCodewords.getNumDataCodewords();
        }
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < length; i2++) {
            DataBlock dataBlock = dataBlocks[i2];
            byte[] codewords = dataBlock.getCodewords();
            int numDataCodewords2 = dataBlock.getNumDataCodewords();
            correctErrors(codewords, numDataCodewords2);
            for (int i3 = 0; i3 < numDataCodewords2; i3++) {
                bArr[(i3 * length) + i2] = codewords[i3];
            }
        }
        return DecodedBitStreamParser.decode(bArr);
    }
}
