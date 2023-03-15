package com.dcloud.zxing2;

import com.dcloud.zxing2.aztec.AztecWriter;
import com.dcloud.zxing2.common.BitMatrix;
import com.dcloud.zxing2.datamatrix.DataMatrixWriter;
import com.dcloud.zxing2.oned.CodaBarWriter;
import com.dcloud.zxing2.oned.Code128Writer;
import com.dcloud.zxing2.oned.Code39Writer;
import com.dcloud.zxing2.oned.Code93Writer;
import com.dcloud.zxing2.oned.EAN13Writer;
import com.dcloud.zxing2.oned.EAN8Writer;
import com.dcloud.zxing2.oned.ITFWriter;
import com.dcloud.zxing2.oned.UPCAWriter;
import com.dcloud.zxing2.oned.UPCEWriter;
import com.dcloud.zxing2.pdf417.PDF417Writer;
import com.dcloud.zxing2.qrcode.QRCodeWriter;
import java.util.Map;

public final class MultiFormatWriter implements Writer {

    /* renamed from: com.dcloud.zxing2.MultiFormatWriter$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing2$BarcodeFormat;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|(3:25|26|28)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.dcloud.zxing2.BarcodeFormat[] r0 = com.dcloud.zxing2.BarcodeFormat.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$dcloud$zxing2$BarcodeFormat = r0
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.EAN_8     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x001d }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.UPC_E     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.EAN_13     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.UPC_A     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x003e }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.QR_CODE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.CODE_39     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.CODE_93     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.CODE_128     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x006c }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.ITF     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.PDF_417     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0084 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.CODABAR     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x0090 }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.DATA_MATRIX     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$BarcodeFormat     // Catch:{ NoSuchFieldError -> 0x009c }
                com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.AZTEC     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.MultiFormatWriter.AnonymousClass1.<clinit>():void");
        }
    }

    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        return encode(str, barcodeFormat, i, i2, (Map<EncodeHintType, ?>) null);
    }

    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        Writer writer;
        switch (AnonymousClass1.$SwitchMap$com$dcloud$zxing2$BarcodeFormat[barcodeFormat.ordinal()]) {
            case 1:
                writer = new EAN8Writer();
                break;
            case 2:
                writer = new UPCEWriter();
                break;
            case 3:
                writer = new EAN13Writer();
                break;
            case 4:
                writer = new UPCAWriter();
                break;
            case 5:
                writer = new QRCodeWriter();
                break;
            case 6:
                writer = new Code39Writer();
                break;
            case 7:
                writer = new Code93Writer();
                break;
            case 8:
                writer = new Code128Writer();
                break;
            case 9:
                writer = new ITFWriter();
                break;
            case 10:
                writer = new PDF417Writer();
                break;
            case 11:
                writer = new CodaBarWriter();
                break;
            case 12:
                writer = new DataMatrixWriter();
                break;
            case 13:
                writer = new AztecWriter();
                break;
            default:
                throw new IllegalArgumentException("No encoder available for format " + barcodeFormat);
        }
        return writer.encode(str, barcodeFormat, i, i2, map);
    }
}
