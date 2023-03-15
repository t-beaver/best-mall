package net.lingala.zip4j.io.inputstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;

public abstract class SplitInputStream extends InputStream {
    private int currentSplitFileCounter = 0;
    private boolean isSplitZipArchive;
    protected RandomAccessFile randomAccessFile;
    private byte[] singleByteArray = new byte[1];
    protected File zipFile;

    public SplitInputStream(File file, boolean z, int i) throws FileNotFoundException {
        this.randomAccessFile = new RandomAccessFile(file, RandomAccessFileMode.READ.getValue());
        this.zipFile = file;
        this.isSplitZipArchive = z;
        if (z) {
            this.currentSplitFileCounter = i;
        }
    }

    public void close() throws IOException {
        RandomAccessFile randomAccessFile2 = this.randomAccessFile;
        if (randomAccessFile2 != null) {
            randomAccessFile2.close();
        }
    }

    /* access modifiers changed from: protected */
    public abstract File getNextSplitFile(int i) throws IOException;

    /* access modifiers changed from: protected */
    public void openRandomAccessFileForIndex(int i) throws IOException {
        File nextSplitFile = getNextSplitFile(i);
        if (nextSplitFile.exists()) {
            this.randomAccessFile.close();
            this.randomAccessFile = new RandomAccessFile(nextSplitFile, RandomAccessFileMode.READ.getValue());
            return;
        }
        throw new FileNotFoundException("zip split file does not exist: " + nextSplitFile);
    }

    public void prepareExtractionForFileHeader(FileHeader fileHeader) throws IOException {
        if (this.isSplitZipArchive && this.currentSplitFileCounter != fileHeader.getDiskNumberStart()) {
            openRandomAccessFileForIndex(fileHeader.getDiskNumberStart());
            this.currentSplitFileCounter = fileHeader.getDiskNumberStart();
        }
        this.randomAccessFile.seek(fileHeader.getOffsetLocalHeader());
    }

    public int read() throws IOException {
        if (read(this.singleByteArray) == -1) {
            return -1;
        }
        return this.singleByteArray[0];
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.randomAccessFile.read(bArr, i, i2);
        if ((read == i2 && read != -1) || !this.isSplitZipArchive) {
            return read;
        }
        openRandomAccessFileForIndex(this.currentSplitFileCounter + 1);
        this.currentSplitFileCounter++;
        if (read < 0) {
            read = 0;
        }
        int read2 = this.randomAccessFile.read(bArr, read, i2 - read);
        return read2 > 0 ? read + read2 : read;
    }
}
