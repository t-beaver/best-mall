package net.lingala.zip4j.io.inputstream;

import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.lingala.zip4j.util.FileUtils;

public class NumberedSplitInputStream extends SplitInputStream {
    public NumberedSplitInputStream(File file, boolean z, int i) throws FileNotFoundException {
        super(file, z, i);
    }

    /* access modifiers changed from: protected */
    public File getNextSplitFile(int i) throws IOException {
        String canonicalPath = this.zipFile.getCanonicalPath();
        String substring = canonicalPath.substring(0, canonicalPath.lastIndexOf(Operators.DOT_STR));
        return new File(substring + FileUtils.getNextNumberedSplitFileCounterAsExtension(i));
    }
}
