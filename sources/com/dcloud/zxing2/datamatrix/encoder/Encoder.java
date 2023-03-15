package com.dcloud.zxing2.datamatrix.encoder;

interface Encoder {
    void encode(EncoderContext encoderContext);

    int getEncodingMode();
}
