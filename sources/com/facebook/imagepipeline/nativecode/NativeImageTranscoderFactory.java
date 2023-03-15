package com.facebook.imagepipeline.nativecode;

import com.facebook.imagepipeline.transcoder.ImageTranscoderFactory;
import java.lang.reflect.InvocationTargetException;

public final class NativeImageTranscoderFactory {
    private NativeImageTranscoderFactory() {
    }

    public static ImageTranscoderFactory getNativeImageTranscoderFactory(int i, boolean z, boolean z2) {
        try {
            return (ImageTranscoderFactory) Class.forName("com.facebook.imagepipeline.nativecode.NativeJpegTranscoderFactory").getConstructor(new Class[]{Integer.TYPE, Boolean.TYPE, Boolean.TYPE}).newInstance(new Object[]{Integer.valueOf(i), Boolean.valueOf(z), Boolean.valueOf(z2)});
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException("Dependency ':native-imagetranscoder' is needed to use the default native image transcoder.", e);
        }
    }
}
