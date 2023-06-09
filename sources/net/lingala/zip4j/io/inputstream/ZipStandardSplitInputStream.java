package net.lingala.zip4j.io.inputstream;

import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ZipStandardSplitInputStream extends SplitInputStream {
    private int lastSplitZipFileNumber;

    public ZipStandardSplitInputStream(File file, boolean z, int i) throws FileNotFoundException {
        super(file, z, i);
        this.lastSplitZipFileNumber = i;
    }

    /* access modifiers changed from: protected */
    public File getNextSplitFile(int i) throws IOException {
        if (i == this.lastSplitZipFileNumber) {
            return this.zipFile;
        }
        String canonicalPath = this.zipFile.getCanonicalPath();
        String str = i >= 9 ? ".z" : ".z0";
        return new File(canonicalPath.substring(0, canonicalPath.lastIndexOf(Operators.DOT_STR)) + str + (i + 1));
    }
}
