package org.mozilla.universalchardet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Objects;

public final class ReaderFactory {
    private ReaderFactory() {
        throw new AssertionError("No instances allowed");
    }

    public static Reader createReaderFromFile(File file, Charset charset) throws IOException {
        Objects.requireNonNull(charset, "defaultCharset must be not null");
        String detectCharset = UniversalDetector.detectCharset(file);
        if (detectCharset != null) {
            charset = Charset.forName(detectCharset);
        }
        if (!charset.toString().contains("UTF")) {
            return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset);
        }
        return new InputStreamReader(new UnicodeBOMInputStream(new BufferedInputStream(new FileInputStream(file))), charset);
    }

    public static Reader createReaderFromFile(File file) throws IOException {
        return createReaderFromFile(file, Charset.defaultCharset());
    }
}
