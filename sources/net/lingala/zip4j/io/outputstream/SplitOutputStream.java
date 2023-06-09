package net.lingala.zip4j.io.outputstream;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.RawIO;

public class SplitOutputStream extends OutputStream implements OutputStreamWithSplitZipSupport {
    private long bytesWrittenForThisPart;
    private int currSplitFileCounter;
    private RandomAccessFile raf;
    private RawIO rawIO;
    private long splitLength;
    private File zipFile;

    public SplitOutputStream(File file) throws FileNotFoundException, ZipException {
        this(file, -1);
    }

    private boolean isBufferSizeFitForCurrSplitFile(int i) {
        long j = this.splitLength;
        if (j < PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH || this.bytesWrittenForThisPart + ((long) i) <= j) {
            return true;
        }
        return false;
    }

    private boolean isHeaderData(byte[] bArr) {
        int readIntLittleEndian = this.rawIO.readIntLittleEndian(bArr);
        for (HeaderSignature headerSignature : HeaderSignature.values()) {
            if (headerSignature != HeaderSignature.SPLIT_ZIP && headerSignature.getValue() == ((long) readIntLittleEndian)) {
                return true;
            }
        }
        return false;
    }

    private void startNextSplitFile() throws IOException {
        String str;
        String zipFileNameWithoutExtension = FileUtils.getZipFileNameWithoutExtension(this.zipFile.getName());
        String absolutePath = this.zipFile.getAbsolutePath();
        if (this.zipFile.getParent() == null) {
            str = "";
        } else {
            str = this.zipFile.getParent() + System.getProperty("file.separator");
        }
        String str2 = ".z0" + (this.currSplitFileCounter + 1);
        if (this.currSplitFileCounter >= 9) {
            str2 = ".z" + (this.currSplitFileCounter + 1);
        }
        File file = new File(str + zipFileNameWithoutExtension + str2);
        this.raf.close();
        if (file.exists()) {
            throw new IOException("split file: " + file.getName() + " already exists in the current directory, cannot rename this file");
        } else if (this.zipFile.renameTo(file)) {
            this.zipFile = new File(absolutePath);
            this.raf = new RandomAccessFile(this.zipFile, RandomAccessFileMode.WRITE.getValue());
            this.currSplitFileCounter++;
        } else {
            throw new IOException("cannot rename newly created split file");
        }
    }

    public boolean checkBufferSizeAndStartNextSplitFile(int i) throws ZipException {
        if (i < 0) {
            throw new ZipException("negative buffersize for checkBufferSizeAndStartNextSplitFile");
        } else if (isBufferSizeFitForCurrSplitFile(i)) {
            return false;
        } else {
            try {
                startNextSplitFile();
                this.bytesWrittenForThisPart = 0;
                return true;
            } catch (IOException e) {
                throw new ZipException((Exception) e);
            }
        }
    }

    public void close() throws IOException {
        this.raf.close();
    }

    public int getCurrentSplitFileCounter() {
        return this.currSplitFileCounter;
    }

    public long getFilePointer() throws IOException {
        return this.raf.getFilePointer();
    }

    public long getSplitLength() {
        return this.splitLength;
    }

    public boolean isSplitZipFile() {
        return this.splitLength != -1;
    }

    public void seek(long j) throws IOException {
        this.raf.seek(j);
    }

    public int skipBytes(int i) throws IOException {
        return this.raf.skipBytes(i);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }

    public SplitOutputStream(File file, long j) throws FileNotFoundException, ZipException {
        this.rawIO = new RawIO();
        if (j < 0 || j >= PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
            this.raf = new RandomAccessFile(file, RandomAccessFileMode.WRITE.getValue());
            this.splitLength = j;
            this.zipFile = file;
            this.currSplitFileCounter = 0;
            this.bytesWrittenForThisPart = 0;
            return;
        }
        throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (i2 > 0) {
            long j = this.splitLength;
            if (j == -1) {
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart += (long) i2;
                return;
            }
            long j2 = this.bytesWrittenForThisPart;
            if (j2 >= j) {
                startNextSplitFile();
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart = (long) i2;
                return;
            }
            long j3 = (long) i2;
            if (j2 + j3 <= j) {
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart += j3;
            } else if (isHeaderData(bArr)) {
                startNextSplitFile();
                this.raf.write(bArr, i, i2);
                this.bytesWrittenForThisPart = j3;
            } else {
                this.raf.write(bArr, i, (int) (this.splitLength - this.bytesWrittenForThisPart));
                startNextSplitFile();
                RandomAccessFile randomAccessFile = this.raf;
                long j4 = this.splitLength - this.bytesWrittenForThisPart;
                randomAccessFile.write(bArr, i + ((int) j4), (int) (j3 - j4));
                this.bytesWrittenForThisPart = j3 - (this.splitLength - this.bytesWrittenForThisPart);
            }
        }
    }
}
