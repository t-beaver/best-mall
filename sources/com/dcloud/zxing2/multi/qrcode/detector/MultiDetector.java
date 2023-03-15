package com.dcloud.zxing2.multi.qrcode.detector;

import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.ReaderException;
import com.dcloud.zxing2.ResultPointCallback;
import com.dcloud.zxing2.common.BitMatrix;
import com.dcloud.zxing2.common.DetectorResult;
import com.dcloud.zxing2.qrcode.detector.Detector;
import com.dcloud.zxing2.qrcode.detector.FinderPatternInfo;
import java.util.ArrayList;
import java.util.Map;

public final class MultiDetector extends Detector {
    private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

    public MultiDetector(BitMatrix bitMatrix) {
        super(bitMatrix);
    }

    public DetectorResult[] detectMulti(Map<DecodeHintType, ?> map) throws NotFoundException {
        ResultPointCallback resultPointCallback;
        BitMatrix image = getImage();
        if (map == null) {
            resultPointCallback = null;
        } else {
            resultPointCallback = (ResultPointCallback) map.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
        }
        FinderPatternInfo[] findMulti = new MultiFinderPatternFinder(image, resultPointCallback).findMulti(map);
        if (findMulti.length != 0) {
            ArrayList arrayList = new ArrayList();
            for (FinderPatternInfo processFinderPatternInfo : findMulti) {
                try {
                    arrayList.add(processFinderPatternInfo(processFinderPatternInfo));
                } catch (ReaderException unused) {
                }
            }
            if (arrayList.isEmpty()) {
                return EMPTY_DETECTOR_RESULTS;
            }
            return (DetectorResult[]) arrayList.toArray(new DetectorResult[arrayList.size()]);
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
