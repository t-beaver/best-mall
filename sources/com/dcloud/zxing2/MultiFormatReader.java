package com.dcloud.zxing2;

import android.os.Handler;
import com.dcloud.zxing2.aztec.AztecReader;
import com.dcloud.zxing2.datamatrix.DataMatrixReader;
import com.dcloud.zxing2.maxicode.MaxiCodeReader;
import com.dcloud.zxing2.oned.MultiFormatOneDReader;
import com.dcloud.zxing2.pdf417.PDF417Reader;
import com.dcloud.zxing2.qrcode.QRCodeReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MultiFormatReader implements Reader {
    private Map<DecodeHintType, ?> hints;
    private Handler hostHandler;
    private Reader[] readers;

    public MultiFormatReader(Handler handler) {
        this.hostHandler = handler;
    }

    private Result decodeInternal(BinaryBitmap binaryBitmap) throws NotFoundException {
        Reader[] readerArr = this.readers;
        if (readerArr != null) {
            int length = readerArr.length;
            int i = 0;
            while (i < length) {
                try {
                    return readerArr[i].decode(binaryBitmap, this.hints);
                } catch (ReaderException unused) {
                    i++;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException {
        setHints((Map<DecodeHintType, ?>) null);
        return decodeInternal(binaryBitmap);
    }

    public Result decodeWithState(BinaryBitmap binaryBitmap) throws NotFoundException {
        if (this.readers == null) {
            setHints((Map<DecodeHintType, ?>) null);
        }
        return decodeInternal(binaryBitmap);
    }

    public void reset() {
        Reader[] readerArr = this.readers;
        if (readerArr != null) {
            for (Reader reset : readerArr) {
                reset.reset();
            }
        }
    }

    public void setHints(Map<DecodeHintType, ?> map) {
        Collection collection;
        this.hints = map;
        boolean z = true;
        boolean z2 = map != null && map.containsKey(DecodeHintType.TRY_HARDER);
        if (map == null) {
            collection = null;
        } else {
            collection = (Collection) map.get(DecodeHintType.POSSIBLE_FORMATS);
        }
        ArrayList arrayList = new ArrayList();
        if (collection != null) {
            if (!collection.contains(BarcodeFormat.UPC_A) && !collection.contains(BarcodeFormat.UPC_E) && !collection.contains(BarcodeFormat.EAN_13) && !collection.contains(BarcodeFormat.EAN_8) && !collection.contains(BarcodeFormat.CODABAR) && !collection.contains(BarcodeFormat.CODE_39) && !collection.contains(BarcodeFormat.CODE_93) && !collection.contains(BarcodeFormat.CODE_128) && !collection.contains(BarcodeFormat.ITF) && !collection.contains(BarcodeFormat.RSS_14) && !collection.contains(BarcodeFormat.RSS_EXPANDED)) {
                z = false;
            }
            if (z && !z2) {
                arrayList.add(new MultiFormatOneDReader(map));
            }
            if (collection.contains(BarcodeFormat.QR_CODE)) {
                QRCodeReader qRCodeReader = new QRCodeReader();
                qRCodeReader.updateHandler(this.hostHandler);
                arrayList.add(qRCodeReader);
            }
            if (collection.contains(BarcodeFormat.DATA_MATRIX)) {
                arrayList.add(new DataMatrixReader());
            }
            if (collection.contains(BarcodeFormat.AZTEC)) {
                arrayList.add(new AztecReader());
            }
            if (collection.contains(BarcodeFormat.PDF_417)) {
                arrayList.add(new PDF417Reader());
            }
            if (collection.contains(BarcodeFormat.MAXICODE)) {
                arrayList.add(new MaxiCodeReader());
            }
            if (z && z2) {
                arrayList.add(new MultiFormatOneDReader(map));
            }
        }
        if (arrayList.isEmpty()) {
            if (!z2) {
                arrayList.add(new MultiFormatOneDReader(map));
            }
            QRCodeReader qRCodeReader2 = new QRCodeReader();
            qRCodeReader2.updateHandler(this.hostHandler);
            arrayList.add(qRCodeReader2);
            arrayList.add(new DataMatrixReader());
            arrayList.add(new AztecReader());
            arrayList.add(new PDF417Reader());
            arrayList.add(new MaxiCodeReader());
            if (z2) {
                arrayList.add(new MultiFormatOneDReader(map));
            }
        }
        this.readers = (Reader[]) arrayList.toArray(new Reader[arrayList.size()]);
    }

    public Result decode(BinaryBitmap binaryBitmap, Map<DecodeHintType, ?> map) throws NotFoundException {
        setHints(map);
        return decodeInternal(binaryBitmap);
    }
}
