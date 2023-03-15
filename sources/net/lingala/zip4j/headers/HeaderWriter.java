package net.lingala.zip4j.headers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.outputstream.CountingOutputStream;
import net.lingala.zip4j.io.outputstream.OutputStreamWithSplitZipSupport;
import net.lingala.zip4j.io.outputstream.SplitOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.RawIO;
import net.lingala.zip4j.util.Zip4jUtil;

public class HeaderWriter {
    private static final short AES_EXTRA_DATA_RECORD_SIZE = 11;
    private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_FH = 28;
    private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_LFH = 16;
    private final byte[] intBuff = new byte[4];
    private final byte[] longBuff = new byte[8];
    private final RawIO rawIO = new RawIO();

    private Zip64EndOfCentralDirectoryRecord buildZip64EndOfCentralDirectoryRecord(ZipModel zipModel, int i, long j) throws ZipException {
        Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
        zip64EndOfCentralDirectoryRecord.setSignature(HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD);
        zip64EndOfCentralDirectoryRecord.setSizeOfZip64EndCentralDirectoryRecord(44);
        if (!(zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null || zipModel.getCentralDirectory().getFileHeaders().size() <= 0)) {
            FileHeader fileHeader = zipModel.getCentralDirectory().getFileHeaders().get(0);
            zip64EndOfCentralDirectoryRecord.setVersionMadeBy(fileHeader.getVersionMadeBy());
            zip64EndOfCentralDirectoryRecord.setVersionNeededToExtract(fileHeader.getVersionNeededToExtract());
        }
        zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
        zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDiskStartOfCentralDir());
        long size = (long) zipModel.getCentralDirectory().getFileHeaders().size();
        zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(zipModel.isSplitArchive() ? countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk()) : size);
        zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(size);
        zip64EndOfCentralDirectoryRecord.setSizeOfCentralDirectory((long) i);
        zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(j);
        return zip64EndOfCentralDirectoryRecord;
    }

    private int calculateExtraDataRecordsSize(FileHeader fileHeader, boolean z) {
        int i = z ? 32 : 0;
        if (fileHeader.getAesExtraDataRecord() != null) {
            i += 11;
        }
        if (fileHeader.getExtraDataRecords() != null) {
            for (ExtraDataRecord next : fileHeader.getExtraDataRecords()) {
                if (!(next.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || next.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue())) {
                    i += next.getSizeOfData() + 4;
                }
            }
        }
        return i;
    }

    private long countNumberOfFileHeaderEntriesOnDisk(List<FileHeader> list, int i) throws ZipException {
        if (list != null) {
            int i2 = 0;
            for (FileHeader diskNumberStart : list) {
                if (diskNumberStart.getDiskNumberStart() == i) {
                    i2++;
                }
            }
            return (long) i2;
        }
        throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
    }

    private int getCurrentSplitFileCounter(OutputStream outputStream) {
        if (outputStream instanceof SplitOutputStream) {
            return ((SplitOutputStream) outputStream).getCurrentSplitFileCounter();
        }
        return ((CountingOutputStream) outputStream).getCurrentSplitFileCounter();
    }

    private long getOffsetOfCentralDirectory(ZipModel zipModel) {
        if (!zipModel.isZip64Format() || zipModel.getZip64EndOfCentralDirectoryRecord() == null || zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() == -1) {
            return zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
        }
        return zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber();
    }

    private boolean isSplitZipFile(OutputStream outputStream) {
        if (outputStream instanceof SplitOutputStream) {
            return ((SplitOutputStream) outputStream).isSplitZipFile();
        }
        if (outputStream instanceof CountingOutputStream) {
            return ((CountingOutputStream) outputStream).isSplitZipFile();
        }
        return false;
    }

    private boolean isZip64Entry(FileHeader fileHeader) {
        return fileHeader.getCompressedSize() >= InternalZipConstants.ZIP_64_SIZE_LIMIT || fileHeader.getUncompressedSize() >= InternalZipConstants.ZIP_64_SIZE_LIMIT || fileHeader.getOffsetLocalHeader() >= InternalZipConstants.ZIP_64_SIZE_LIMIT || fileHeader.getDiskNumberStart() >= 65535;
    }

    private void processHeaderData(ZipModel zipModel, OutputStream outputStream) throws IOException {
        int i;
        if (outputStream instanceof OutputStreamWithSplitZipSupport) {
            OutputStreamWithSplitZipSupport outputStreamWithSplitZipSupport = (OutputStreamWithSplitZipSupport) outputStream;
            zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(outputStreamWithSplitZipSupport.getFilePointer());
            i = outputStreamWithSplitZipSupport.getCurrentSplitFileCounter();
        } else {
            i = 0;
        }
        if (zipModel.isZip64Format()) {
            if (zipModel.getZip64EndOfCentralDirectoryRecord() == null) {
                zipModel.setZip64EndOfCentralDirectoryRecord(new Zip64EndOfCentralDirectoryRecord());
            }
            if (zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
                zipModel.setZip64EndOfCentralDirectoryLocator(new Zip64EndOfCentralDirectoryLocator());
            }
            zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory());
            zipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(i);
            zipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(i + 1);
        }
        zipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDisk(i);
        zipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDiskStartOfCentralDir(i);
    }

    private void updateFileSizesInLocalFileHeader(SplitOutputStream splitOutputStream, FileHeader fileHeader) throws IOException {
        if (fileHeader.getUncompressedSize() >= InternalZipConstants.ZIP_64_SIZE_LIMIT) {
            this.rawIO.writeLongLittleEndian(this.longBuff, 0, InternalZipConstants.ZIP_64_SIZE_LIMIT);
            splitOutputStream.write(this.longBuff, 0, 4);
            splitOutputStream.write(this.longBuff, 0, 4);
            int fileNameLength = fileHeader.getFileNameLength() + 4 + 2 + 2;
            if (splitOutputStream.skipBytes(fileNameLength) == fileNameLength) {
                this.rawIO.writeLongLittleEndian(splitOutputStream, fileHeader.getUncompressedSize());
                this.rawIO.writeLongLittleEndian(splitOutputStream, fileHeader.getCompressedSize());
                return;
            }
            throw new ZipException("Unable to skip " + fileNameLength + " bytes to update LFH");
        }
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCompressedSize());
        splitOutputStream.write(this.longBuff, 0, 4);
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getUncompressedSize());
        splitOutputStream.write(this.longBuff, 0, 4);
    }

    private void writeCentralDirectory(ZipModel zipModel, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO2, Charset charset) throws ZipException {
        if (zipModel.getCentralDirectory() != null && zipModel.getCentralDirectory().getFileHeaders() != null && zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
            for (FileHeader writeFileHeader : zipModel.getCentralDirectory().getFileHeaders()) {
                writeFileHeader(zipModel, writeFileHeader, byteArrayOutputStream, rawIO2, charset);
            }
        }
    }

    private void writeRemainingExtraDataRecordsIfPresent(FileHeader fileHeader, OutputStream outputStream) throws IOException {
        if (fileHeader.getExtraDataRecords() != null && fileHeader.getExtraDataRecords().size() != 0) {
            for (ExtraDataRecord next : fileHeader.getExtraDataRecords()) {
                if (!(next.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || next.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue())) {
                    this.rawIO.writeShortLittleEndian(outputStream, (int) next.getHeader());
                    this.rawIO.writeShortLittleEndian(outputStream, next.getSizeOfData());
                    if (next.getSizeOfData() > 0 && next.getData() != null) {
                        outputStream.write(next.getData());
                    }
                }
            }
        }
    }

    private void writeZip64EndOfCentralDirectoryLocator(Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO2) throws IOException {
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, (int) HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR.getValue());
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryLocator.getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryLocator.getOffsetZip64EndOfCentralDirectoryRecord());
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryLocator.getTotalNumberOfDiscs());
    }

    private void writeZip64EndOfCentralDirectoryRecord(Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO2) throws IOException {
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, (int) zip64EndOfCentralDirectoryRecord.getSignature().getValue());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getSizeOfZip64EndCentralDirectoryRecord());
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getVersionMadeBy());
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getVersionNeededToExtract());
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getNumberOfThisDisk());
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getNumberOfThisDiskStartOfCentralDirectory());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectoryOnThisDisk());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectory());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getSizeOfCentralDirectory());
        rawIO2.writeLongLittleEndian(byteArrayOutputStream, zip64EndOfCentralDirectoryRecord.getOffsetStartCentralDirectoryWRTStartDiskNumber());
    }

    private void writeZipHeaderBytes(ZipModel zipModel, OutputStream outputStream, byte[] bArr, Charset charset) throws IOException {
        if (bArr == null) {
            throw new ZipException("invalid buff to write as zip headers");
        } else if (!(outputStream instanceof CountingOutputStream) || !((CountingOutputStream) outputStream).checkBuffSizeAndStartNextSplitFile(bArr.length)) {
            outputStream.write(bArr);
        } else {
            finalizeZipFile(zipModel, outputStream, charset);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b3, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b8, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b9, code lost:
        r10.addSuppressed(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00bc, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void finalizeZipFile(net.lingala.zip4j.model.ZipModel r10, java.io.OutputStream r11, java.nio.charset.Charset r12) throws java.io.IOException {
        /*
            r9 = this;
            if (r10 == 0) goto L_0x00bd
            if (r11 == 0) goto L_0x00bd
            java.io.ByteArrayOutputStream r8 = new java.io.ByteArrayOutputStream
            r8.<init>()
            r9.processHeaderData(r10, r11)     // Catch:{ all -> 0x00b1 }
            long r3 = r9.getOffsetOfCentralDirectory(r10)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.util.RawIO r0 = r9.rawIO     // Catch:{ all -> 0x00b1 }
            r9.writeCentralDirectory(r10, r8, r0, r12)     // Catch:{ all -> 0x00b1 }
            int r2 = r8.size()     // Catch:{ all -> 0x00b1 }
            boolean r0 = r10.isZip64Format()     // Catch:{ all -> 0x00b1 }
            if (r0 != 0) goto L_0x0039
            r0 = 4294967295(0xffffffff, double:2.1219957905E-314)
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 >= 0) goto L_0x0039
            net.lingala.zip4j.model.CentralDirectory r0 = r10.getCentralDirectory()     // Catch:{ all -> 0x00b1 }
            java.util.List r0 = r0.getFileHeaders()     // Catch:{ all -> 0x00b1 }
            int r0 = r0.size()     // Catch:{ all -> 0x00b1 }
            r1 = 65535(0xffff, float:9.1834E-41)
            if (r0 < r1) goto L_0x009d
        L_0x0039:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = r10.getZip64EndOfCentralDirectoryRecord()     // Catch:{ all -> 0x00b1 }
            if (r0 != 0) goto L_0x0047
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = new net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord     // Catch:{ all -> 0x00b1 }
            r0.<init>()     // Catch:{ all -> 0x00b1 }
            r10.setZip64EndOfCentralDirectoryRecord(r0)     // Catch:{ all -> 0x00b1 }
        L_0x0047:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            if (r0 != 0) goto L_0x0055
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = new net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator     // Catch:{ all -> 0x00b1 }
            r0.<init>()     // Catch:{ all -> 0x00b1 }
            r10.setZip64EndOfCentralDirectoryLocator(r0)     // Catch:{ all -> 0x00b1 }
        L_0x0055:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            long r5 = (long) r2     // Catch:{ all -> 0x00b1 }
            long r5 = r5 + r3
            r0.setOffsetZip64EndOfCentralDirectoryRecord(r5)     // Catch:{ all -> 0x00b1 }
            boolean r0 = r9.isSplitZipFile(r11)     // Catch:{ all -> 0x00b1 }
            r1 = 1
            if (r0 == 0) goto L_0x0079
            int r0 = r9.getCurrentSplitFileCounter(r11)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r5 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            r5.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(r0)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r5 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            int r0 = r0 + r1
            r5.setTotalNumberOfDiscs(r0)     // Catch:{ all -> 0x00b1 }
            goto L_0x0088
        L_0x0079:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            r5 = 0
            r0.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(r5)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            r0.setTotalNumberOfDiscs(r1)     // Catch:{ all -> 0x00b1 }
        L_0x0088:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = r9.buildZip64EndOfCentralDirectoryRecord(r10, r2, r3)     // Catch:{ all -> 0x00b1 }
            r10.setZip64EndOfCentralDirectoryRecord(r0)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x00b1 }
            r9.writeZip64EndOfCentralDirectoryRecord(r0, r8, r1)     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x00b1 }
            r9.writeZip64EndOfCentralDirectoryLocator(r0, r8, r1)     // Catch:{ all -> 0x00b1 }
        L_0x009d:
            net.lingala.zip4j.util.RawIO r6 = r9.rawIO     // Catch:{ all -> 0x00b1 }
            r0 = r9
            r1 = r10
            r5 = r8
            r7 = r12
            r0.writeEndOfCentralDirectoryRecord(r1, r2, r3, r5, r6, r7)     // Catch:{ all -> 0x00b1 }
            byte[] r0 = r8.toByteArray()     // Catch:{ all -> 0x00b1 }
            r9.writeZipHeaderBytes(r10, r11, r0, r12)     // Catch:{ all -> 0x00b1 }
            r8.close()
            return
        L_0x00b1:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x00b3 }
        L_0x00b3:
            r11 = move-exception
            r8.close()     // Catch:{ all -> 0x00b8 }
            goto L_0x00bc
        L_0x00b8:
            r12 = move-exception
            r10.addSuppressed(r12)
        L_0x00bc:
            throw r11
        L_0x00bd:
            net.lingala.zip4j.exception.ZipException r10 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r11 = "input parameters is null, cannot finalize zip file"
            r10.<init>((java.lang.String) r11)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.headers.HeaderWriter.finalizeZipFile(net.lingala.zip4j.model.ZipModel, java.io.OutputStream, java.nio.charset.Charset):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x008a, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008f, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0090, code lost:
        r10.addSuppressed(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0093, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void finalizeZipFileWithoutValidations(net.lingala.zip4j.model.ZipModel r10, java.io.OutputStream r11, java.nio.charset.Charset r12) throws java.io.IOException {
        /*
            r9 = this;
            if (r10 == 0) goto L_0x0094
            if (r11 == 0) goto L_0x0094
            java.io.ByteArrayOutputStream r8 = new java.io.ByteArrayOutputStream
            r8.<init>()
            net.lingala.zip4j.model.EndOfCentralDirectoryRecord r0 = r10.getEndOfCentralDirectoryRecord()     // Catch:{ all -> 0x0088 }
            long r3 = r0.getOffsetOfStartOfCentralDirectory()     // Catch:{ all -> 0x0088 }
            net.lingala.zip4j.util.RawIO r0 = r9.rawIO     // Catch:{ all -> 0x0088 }
            r9.writeCentralDirectory(r10, r8, r0, r12)     // Catch:{ all -> 0x0088 }
            int r2 = r8.size()     // Catch:{ all -> 0x0088 }
            boolean r0 = r10.isZip64Format()     // Catch:{ all -> 0x0088 }
            if (r0 != 0) goto L_0x003a
            r0 = 4294967295(0xffffffff, double:2.1219957905E-314)
            int r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r5 >= 0) goto L_0x003a
            net.lingala.zip4j.model.CentralDirectory r0 = r10.getCentralDirectory()     // Catch:{ all -> 0x0088 }
            java.util.List r0 = r0.getFileHeaders()     // Catch:{ all -> 0x0088 }
            int r0 = r0.size()     // Catch:{ all -> 0x0088 }
            r1 = 65535(0xffff, float:9.1834E-41)
            if (r0 < r1) goto L_0x0074
        L_0x003a:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = r10.getZip64EndOfCentralDirectoryRecord()     // Catch:{ all -> 0x0088 }
            if (r0 != 0) goto L_0x0048
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = new net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord     // Catch:{ all -> 0x0088 }
            r0.<init>()     // Catch:{ all -> 0x0088 }
            r10.setZip64EndOfCentralDirectoryRecord(r0)     // Catch:{ all -> 0x0088 }
        L_0x0048:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x0088 }
            if (r0 != 0) goto L_0x0056
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = new net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator     // Catch:{ all -> 0x0088 }
            r0.<init>()     // Catch:{ all -> 0x0088 }
            r10.setZip64EndOfCentralDirectoryLocator(r0)     // Catch:{ all -> 0x0088 }
        L_0x0056:
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x0088 }
            long r5 = (long) r2     // Catch:{ all -> 0x0088 }
            long r5 = r5 + r3
            r0.setOffsetZip64EndOfCentralDirectoryRecord(r5)     // Catch:{ all -> 0x0088 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r0 = r9.buildZip64EndOfCentralDirectoryRecord(r10, r2, r3)     // Catch:{ all -> 0x0088 }
            r10.setZip64EndOfCentralDirectoryRecord(r0)     // Catch:{ all -> 0x0088 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0088 }
            r9.writeZip64EndOfCentralDirectoryRecord(r0, r8, r1)     // Catch:{ all -> 0x0088 }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator r0 = r10.getZip64EndOfCentralDirectoryLocator()     // Catch:{ all -> 0x0088 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0088 }
            r9.writeZip64EndOfCentralDirectoryLocator(r0, r8, r1)     // Catch:{ all -> 0x0088 }
        L_0x0074:
            net.lingala.zip4j.util.RawIO r6 = r9.rawIO     // Catch:{ all -> 0x0088 }
            r0 = r9
            r1 = r10
            r5 = r8
            r7 = r12
            r0.writeEndOfCentralDirectoryRecord(r1, r2, r3, r5, r6, r7)     // Catch:{ all -> 0x0088 }
            byte[] r0 = r8.toByteArray()     // Catch:{ all -> 0x0088 }
            r9.writeZipHeaderBytes(r10, r11, r0, r12)     // Catch:{ all -> 0x0088 }
            r8.close()
            return
        L_0x0088:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x008a }
        L_0x008a:
            r11 = move-exception
            r8.close()     // Catch:{ all -> 0x008f }
            goto L_0x0093
        L_0x008f:
            r12 = move-exception
            r10.addSuppressed(r12)
        L_0x0093:
            throw r11
        L_0x0094:
            net.lingala.zip4j.exception.ZipException r10 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r11 = "input parameters is null, cannot finalize zip file without validations"
            r10.<init>((java.lang.String) r11)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.headers.HeaderWriter.finalizeZipFileWithoutValidations(net.lingala.zip4j.model.ZipModel, java.io.OutputStream, java.nio.charset.Charset):void");
    }

    public void updateLocalFileHeader(FileHeader fileHeader, ZipModel zipModel, SplitOutputStream splitOutputStream) throws IOException {
        SplitOutputStream splitOutputStream2;
        String str;
        String str2;
        if (fileHeader == null || zipModel == null) {
            throw new ZipException("invalid input parameters, cannot update local file header");
        }
        boolean z = true;
        if (fileHeader.getDiskNumberStart() != splitOutputStream.getCurrentSplitFileCounter()) {
            String parent = zipModel.getZipFile().getParent();
            String zipFileNameWithoutExtension = FileUtils.getZipFileNameWithoutExtension(zipModel.getZipFile().getName());
            if (parent != null) {
                str = parent + System.getProperty("file.separator");
            } else {
                str = "";
            }
            if (fileHeader.getDiskNumberStart() < 9) {
                str2 = str + zipFileNameWithoutExtension + ".z0" + (fileHeader.getDiskNumberStart() + 1);
            } else {
                str2 = str + zipFileNameWithoutExtension + ".z" + (fileHeader.getDiskNumberStart() + 1);
            }
            splitOutputStream2 = new SplitOutputStream(new File(str2));
        } else {
            splitOutputStream2 = splitOutputStream;
            z = false;
        }
        long filePointer = splitOutputStream2.getFilePointer();
        splitOutputStream2.seek(fileHeader.getOffsetLocalHeader() + 14);
        this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCrc());
        splitOutputStream2.write(this.longBuff, 0, 4);
        updateFileSizesInLocalFileHeader(splitOutputStream2, fileHeader);
        if (z) {
            splitOutputStream2.close();
        } else {
            splitOutputStream.seek(filePointer);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006d, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0072, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0073, code lost:
        r9.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0076, code lost:
        throw r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeExtendedLocalHeader(net.lingala.zip4j.model.LocalFileHeader r9, java.io.OutputStream r10) throws java.io.IOException {
        /*
            r8 = this;
            if (r9 == 0) goto L_0x0077
            if (r10 == 0) goto L_0x0077
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            net.lingala.zip4j.headers.HeaderSignature r2 = net.lingala.zip4j.headers.HeaderSignature.EXTRA_DATA_RECORD     // Catch:{ all -> 0x006b }
            long r2 = r2.getValue()     // Catch:{ all -> 0x006b }
            int r3 = (int) r2     // Catch:{ all -> 0x006b }
            r1.writeIntLittleEndian(r0, r3)     // Catch:{ all -> 0x006b }
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            byte[] r2 = r8.longBuff     // Catch:{ all -> 0x006b }
            long r3 = r9.getCrc()     // Catch:{ all -> 0x006b }
            r5 = 0
            r1.writeLongLittleEndian(r2, r5, r3)     // Catch:{ all -> 0x006b }
            byte[] r1 = r8.longBuff     // Catch:{ all -> 0x006b }
            r2 = 4
            r0.write(r1, r5, r2)     // Catch:{ all -> 0x006b }
            boolean r1 = r9.isWriteCompressedSizeInZip64ExtraRecord()     // Catch:{ all -> 0x006b }
            if (r1 == 0) goto L_0x0040
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            long r2 = r9.getCompressedSize()     // Catch:{ all -> 0x006b }
            r1.writeLongLittleEndian(r0, r2)     // Catch:{ all -> 0x006b }
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            long r2 = r9.getUncompressedSize()     // Catch:{ all -> 0x006b }
            r1.writeLongLittleEndian(r0, r2)     // Catch:{ all -> 0x006b }
            goto L_0x0060
        L_0x0040:
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            byte[] r3 = r8.longBuff     // Catch:{ all -> 0x006b }
            long r6 = r9.getCompressedSize()     // Catch:{ all -> 0x006b }
            r1.writeLongLittleEndian(r3, r5, r6)     // Catch:{ all -> 0x006b }
            byte[] r1 = r8.longBuff     // Catch:{ all -> 0x006b }
            r0.write(r1, r5, r2)     // Catch:{ all -> 0x006b }
            net.lingala.zip4j.util.RawIO r1 = r8.rawIO     // Catch:{ all -> 0x006b }
            byte[] r3 = r8.longBuff     // Catch:{ all -> 0x006b }
            long r6 = r9.getUncompressedSize()     // Catch:{ all -> 0x006b }
            r1.writeLongLittleEndian(r3, r5, r6)     // Catch:{ all -> 0x006b }
            byte[] r9 = r8.longBuff     // Catch:{ all -> 0x006b }
            r0.write(r9, r5, r2)     // Catch:{ all -> 0x006b }
        L_0x0060:
            byte[] r9 = r0.toByteArray()     // Catch:{ all -> 0x006b }
            r10.write(r9)     // Catch:{ all -> 0x006b }
            r0.close()
            return
        L_0x006b:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x006d }
        L_0x006d:
            r10 = move-exception
            r0.close()     // Catch:{ all -> 0x0072 }
            goto L_0x0076
        L_0x0072:
            r0 = move-exception
            r9.addSuppressed(r0)
        L_0x0076:
            throw r10
        L_0x0077:
            net.lingala.zip4j.exception.ZipException r9 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r10 = "input parameters is null, cannot write extended local header"
            r9.<init>((java.lang.String) r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.headers.HeaderWriter.writeExtendedLocalHeader(net.lingala.zip4j.model.LocalFileHeader, java.io.OutputStream):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0166, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x016b, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x016c, code lost:
        r10.addSuppressed(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x016f, code lost:
        throw r11;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x006e A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0086 A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x00b5 A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00c5 A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x00c8 A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00cf A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00d9 A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00de A[Catch:{ all -> 0x0166 }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0109 A[Catch:{ all -> 0x0166 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeLocalFileHeader(net.lingala.zip4j.model.ZipModel r10, net.lingala.zip4j.model.LocalFileHeader r11, java.io.OutputStream r12, java.nio.charset.Charset r13) throws java.io.IOException {
        /*
            r9 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.headers.HeaderSignature r2 = r11.getSignature()     // Catch:{ all -> 0x0164 }
            long r2 = r2.getValue()     // Catch:{ all -> 0x0164 }
            int r3 = (int) r2     // Catch:{ all -> 0x0164 }
            r1.writeIntLittleEndian(r0, r3)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0164 }
            int r2 = r11.getVersionNeededToExtract()     // Catch:{ all -> 0x0164 }
            r1.writeShortLittleEndian(r0, r2)     // Catch:{ all -> 0x0164 }
            byte[] r1 = r11.getGeneralPurposeFlag()     // Catch:{ all -> 0x0164 }
            r0.write(r1)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.model.enums.CompressionMethod r2 = r11.getCompressionMethod()     // Catch:{ all -> 0x0164 }
            int r2 = r2.getCode()     // Catch:{ all -> 0x0164 }
            r1.writeShortLittleEndian(r0, r2)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0164 }
            byte[] r2 = r9.longBuff     // Catch:{ all -> 0x0164 }
            long r3 = r11.getLastModifiedTime()     // Catch:{ all -> 0x0164 }
            r5 = 0
            r1.writeLongLittleEndian(r2, r5, r3)     // Catch:{ all -> 0x0164 }
            byte[] r1 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r2 = 4
            r0.write(r1, r5, r2)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r1 = r9.rawIO     // Catch:{ all -> 0x0164 }
            byte[] r3 = r9.longBuff     // Catch:{ all -> 0x0164 }
            long r6 = r11.getCrc()     // Catch:{ all -> 0x0164 }
            r1.writeLongLittleEndian(r3, r5, r6)     // Catch:{ all -> 0x0164 }
            byte[] r1 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r0.write(r1, r5, r2)     // Catch:{ all -> 0x0164 }
            long r3 = r11.getCompressedSize()     // Catch:{ all -> 0x0164 }
            r6 = 4294967295(0xffffffff, double:2.1219957905E-314)
            r1 = 1
            int r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r8 >= 0) goto L_0x006b
            long r3 = r11.getUncompressedSize()     // Catch:{ all -> 0x0164 }
            int r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r8 < 0) goto L_0x0069
            goto L_0x006b
        L_0x0069:
            r3 = 0
            goto L_0x006c
        L_0x006b:
            r3 = 1
        L_0x006c:
            if (r3 == 0) goto L_0x0086
            net.lingala.zip4j.util.RawIO r4 = r9.rawIO     // Catch:{ all -> 0x0164 }
            byte[] r8 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r4.writeLongLittleEndian(r8, r5, r6)     // Catch:{ all -> 0x0164 }
            byte[] r4 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r0.write(r4, r5, r2)     // Catch:{ all -> 0x0164 }
            byte[] r4 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r0.write(r4, r5, r2)     // Catch:{ all -> 0x0164 }
            r10.setZip64Format(r1)     // Catch:{ all -> 0x0164 }
            r11.setWriteCompressedSizeInZip64ExtraRecord(r1)     // Catch:{ all -> 0x0164 }
            goto L_0x00a9
        L_0x0086:
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            byte[] r4 = r9.longBuff     // Catch:{ all -> 0x0164 }
            long r6 = r11.getCompressedSize()     // Catch:{ all -> 0x0164 }
            r10.writeLongLittleEndian(r4, r5, r6)     // Catch:{ all -> 0x0164 }
            byte[] r10 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r0.write(r10, r5, r2)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            byte[] r4 = r9.longBuff     // Catch:{ all -> 0x0164 }
            long r6 = r11.getUncompressedSize()     // Catch:{ all -> 0x0164 }
            r10.writeLongLittleEndian(r4, r5, r6)     // Catch:{ all -> 0x0164 }
            byte[] r10 = r9.longBuff     // Catch:{ all -> 0x0164 }
            r0.write(r10, r5, r2)     // Catch:{ all -> 0x0164 }
            r11.setWriteCompressedSizeInZip64ExtraRecord(r5)     // Catch:{ all -> 0x0164 }
        L_0x00a9:
            byte[] r10 = new byte[r5]     // Catch:{ all -> 0x0164 }
            java.lang.String r2 = r11.getFileName()     // Catch:{ all -> 0x0164 }
            boolean r2 = net.lingala.zip4j.util.Zip4jUtil.isStringNotNullAndNotEmpty(r2)     // Catch:{ all -> 0x0164 }
            if (r2 == 0) goto L_0x00bd
            java.lang.String r10 = r11.getFileName()     // Catch:{ all -> 0x0164 }
            byte[] r10 = net.lingala.zip4j.headers.HeaderUtil.getBytesFromString(r10, r13)     // Catch:{ all -> 0x0164 }
        L_0x00bd:
            net.lingala.zip4j.util.RawIO r13 = r9.rawIO     // Catch:{ all -> 0x0164 }
            int r2 = r10.length     // Catch:{ all -> 0x0164 }
            r13.writeShortLittleEndian(r0, r2)     // Catch:{ all -> 0x0164 }
            if (r3 == 0) goto L_0x00c8
            r13 = 20
            goto L_0x00c9
        L_0x00c8:
            r13 = 0
        L_0x00c9:
            net.lingala.zip4j.model.AESExtraDataRecord r2 = r11.getAesExtraDataRecord()     // Catch:{ all -> 0x0164 }
            if (r2 == 0) goto L_0x00d1
            int r13 = r13 + 11
        L_0x00d1:
            net.lingala.zip4j.util.RawIO r2 = r9.rawIO     // Catch:{ all -> 0x0164 }
            r2.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            int r13 = r10.length     // Catch:{ all -> 0x0164 }
            if (r13 <= 0) goto L_0x00dc
            r0.write(r10)     // Catch:{ all -> 0x0164 }
        L_0x00dc:
            if (r3 == 0) goto L_0x0103
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.headers.HeaderSignature r13 = net.lingala.zip4j.headers.HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE     // Catch:{ all -> 0x0164 }
            long r2 = r13.getValue()     // Catch:{ all -> 0x0164 }
            int r13 = (int) r2     // Catch:{ all -> 0x0164 }
            r10.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            r13 = 16
            r10.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            long r2 = r11.getUncompressedSize()     // Catch:{ all -> 0x0164 }
            r10.writeLongLittleEndian(r0, r2)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r10 = r9.rawIO     // Catch:{ all -> 0x0164 }
            long r2 = r11.getCompressedSize()     // Catch:{ all -> 0x0164 }
            r10.writeLongLittleEndian(r0, r2)     // Catch:{ all -> 0x0164 }
        L_0x0103:
            net.lingala.zip4j.model.AESExtraDataRecord r10 = r11.getAesExtraDataRecord()     // Catch:{ all -> 0x0164 }
            if (r10 == 0) goto L_0x0159
            net.lingala.zip4j.model.AESExtraDataRecord r10 = r11.getAesExtraDataRecord()     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r11 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.headers.HeaderSignature r13 = r10.getSignature()     // Catch:{ all -> 0x0164 }
            long r2 = r13.getValue()     // Catch:{ all -> 0x0164 }
            int r13 = (int) r2     // Catch:{ all -> 0x0164 }
            r11.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r11 = r9.rawIO     // Catch:{ all -> 0x0164 }
            int r13 = r10.getDataSize()     // Catch:{ all -> 0x0164 }
            r11.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r11 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.model.enums.AesVersion r13 = r10.getAesVersion()     // Catch:{ all -> 0x0164 }
            int r13 = r13.getVersionNumber()     // Catch:{ all -> 0x0164 }
            r11.writeShortLittleEndian(r0, r13)     // Catch:{ all -> 0x0164 }
            java.lang.String r11 = r10.getVendorID()     // Catch:{ all -> 0x0164 }
            byte[] r11 = r11.getBytes()     // Catch:{ all -> 0x0164 }
            r0.write(r11)     // Catch:{ all -> 0x0164 }
            byte[] r11 = new byte[r1]     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.model.enums.AesKeyStrength r13 = r10.getAesKeyStrength()     // Catch:{ all -> 0x0164 }
            int r13 = r13.getRawCode()     // Catch:{ all -> 0x0164 }
            byte r13 = (byte) r13     // Catch:{ all -> 0x0164 }
            r11[r5] = r13     // Catch:{ all -> 0x0164 }
            r0.write(r11)     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.util.RawIO r11 = r9.rawIO     // Catch:{ all -> 0x0164 }
            net.lingala.zip4j.model.enums.CompressionMethod r10 = r10.getCompressionMethod()     // Catch:{ all -> 0x0164 }
            int r10 = r10.getCode()     // Catch:{ all -> 0x0164 }
            r11.writeShortLittleEndian(r0, r10)     // Catch:{ all -> 0x0164 }
        L_0x0159:
            byte[] r10 = r0.toByteArray()     // Catch:{ all -> 0x0164 }
            r12.write(r10)     // Catch:{ all -> 0x0164 }
            r0.close()
            return
        L_0x0164:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0166 }
        L_0x0166:
            r11 = move-exception
            r0.close()     // Catch:{ all -> 0x016b }
            goto L_0x016f
        L_0x016b:
            r12 = move-exception
            r10.addSuppressed(r12)
        L_0x016f:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.headers.HeaderWriter.writeLocalFileHeader(net.lingala.zip4j.model.ZipModel, net.lingala.zip4j.model.LocalFileHeader, java.io.OutputStream, java.nio.charset.Charset):void");
    }

    private void writeEndOfCentralDirectoryRecord(ZipModel zipModel, int i, long j, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO2, Charset charset) throws IOException {
        byte[] bArr = new byte[8];
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, (int) HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue());
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDiskStartOfCentralDir());
        long size = (long) zipModel.getCentralDirectory().getFileHeaders().size();
        long countNumberOfFileHeaderEntriesOnDisk = zipModel.isSplitArchive() ? countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk()) : size;
        if (countNumberOfFileHeaderEntriesOnDisk > 65535) {
            countNumberOfFileHeaderEntriesOnDisk = 65535;
        }
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, (int) countNumberOfFileHeaderEntriesOnDisk);
        if (size > 65535) {
            size = 65535;
        }
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, (int) size);
        rawIO2.writeIntLittleEndian(byteArrayOutputStream, i);
        if (j > InternalZipConstants.ZIP_64_SIZE_LIMIT) {
            rawIO2.writeLongLittleEndian(bArr, 0, InternalZipConstants.ZIP_64_SIZE_LIMIT);
            byteArrayOutputStream.write(bArr, 0, 4);
        } else {
            rawIO2.writeLongLittleEndian(bArr, 0, j);
            byteArrayOutputStream.write(bArr, 0, 4);
        }
        String comment = zipModel.getEndOfCentralDirectoryRecord().getComment();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(comment)) {
            byte[] bytesFromString = HeaderUtil.getBytesFromString(comment, charset);
            rawIO2.writeShortLittleEndian(byteArrayOutputStream, bytesFromString.length);
            byteArrayOutputStream.write(bytesFromString);
            return;
        }
        rawIO2.writeShortLittleEndian(byteArrayOutputStream, 0);
    }

    private void writeFileHeader(ZipModel zipModel, FileHeader fileHeader, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO2, Charset charset) throws ZipException {
        byte[] bArr;
        ZipModel zipModel2 = zipModel;
        FileHeader fileHeader2 = fileHeader;
        ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
        RawIO rawIO3 = rawIO2;
        Charset charset2 = charset;
        if (fileHeader2 != null) {
            try {
                byte[] bArr2 = {0, 0};
                boolean isZip64Entry = isZip64Entry(fileHeader2);
                rawIO3.writeIntLittleEndian(byteArrayOutputStream2, (int) fileHeader.getSignature().getValue());
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, fileHeader.getVersionMadeBy());
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, fileHeader.getVersionNeededToExtract());
                byteArrayOutputStream2.write(fileHeader.getGeneralPurposeFlag());
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, fileHeader.getCompressionMethod().getCode());
                rawIO3.writeLongLittleEndian(this.longBuff, 0, fileHeader.getLastModifiedTime());
                byteArrayOutputStream2.write(this.longBuff, 0, 4);
                rawIO3.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCrc());
                byteArrayOutputStream2.write(this.longBuff, 0, 4);
                if (isZip64Entry) {
                    rawIO3.writeLongLittleEndian(this.longBuff, 0, InternalZipConstants.ZIP_64_SIZE_LIMIT);
                    byteArrayOutputStream2.write(this.longBuff, 0, 4);
                    byteArrayOutputStream2.write(this.longBuff, 0, 4);
                    zipModel2.setZip64Format(true);
                    bArr = bArr2;
                } else {
                    bArr = bArr2;
                    rawIO3.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCompressedSize());
                    byteArrayOutputStream2.write(this.longBuff, 0, 4);
                    rawIO3.writeLongLittleEndian(this.longBuff, 0, fileHeader.getUncompressedSize());
                    byteArrayOutputStream2.write(this.longBuff, 0, 4);
                }
                byte[] bArr3 = new byte[0];
                if (Zip4jUtil.isStringNotNullAndNotEmpty(fileHeader.getFileName())) {
                    bArr3 = HeaderUtil.getBytesFromString(fileHeader.getFileName(), charset2);
                }
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, bArr3.length);
                byte[] bArr4 = new byte[4];
                if (isZip64Entry) {
                    rawIO3.writeLongLittleEndian(this.longBuff, 0, InternalZipConstants.ZIP_64_SIZE_LIMIT);
                    System.arraycopy(this.longBuff, 0, bArr4, 0, 4);
                } else {
                    rawIO3.writeLongLittleEndian(this.longBuff, 0, fileHeader.getOffsetLocalHeader());
                    System.arraycopy(this.longBuff, 0, bArr4, 0, 4);
                }
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, calculateExtraDataRecordsSize(fileHeader2, isZip64Entry));
                String fileComment = fileHeader.getFileComment();
                byte[] bArr5 = new byte[0];
                if (Zip4jUtil.isStringNotNullAndNotEmpty(fileComment)) {
                    bArr5 = HeaderUtil.getBytesFromString(fileComment, charset2);
                }
                rawIO3.writeShortLittleEndian(byteArrayOutputStream2, bArr5.length);
                if (isZip64Entry) {
                    rawIO3.writeIntLittleEndian(this.intBuff, 0, 65535);
                    byteArrayOutputStream2.write(this.intBuff, 0, 2);
                } else {
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, fileHeader.getDiskNumberStart());
                }
                byteArrayOutputStream2.write(bArr);
                byteArrayOutputStream2.write(fileHeader.getExternalFileAttributes());
                byteArrayOutputStream2.write(bArr4);
                if (bArr3.length > 0) {
                    byteArrayOutputStream2.write(bArr3);
                }
                if (isZip64Entry) {
                    zipModel2.setZip64Format(true);
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, (int) HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue());
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, 28);
                    rawIO3.writeLongLittleEndian(byteArrayOutputStream2, fileHeader.getUncompressedSize());
                    rawIO3.writeLongLittleEndian(byteArrayOutputStream2, fileHeader.getCompressedSize());
                    rawIO3.writeLongLittleEndian(byteArrayOutputStream2, fileHeader.getOffsetLocalHeader());
                    rawIO3.writeIntLittleEndian(byteArrayOutputStream2, fileHeader.getDiskNumberStart());
                }
                if (fileHeader.getAesExtraDataRecord() != null) {
                    AESExtraDataRecord aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, (int) aesExtraDataRecord.getSignature().getValue());
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, aesExtraDataRecord.getDataSize());
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, aesExtraDataRecord.getAesVersion().getVersionNumber());
                    byteArrayOutputStream2.write(aesExtraDataRecord.getVendorID().getBytes());
                    byteArrayOutputStream2.write(new byte[]{(byte) aesExtraDataRecord.getAesKeyStrength().getRawCode()});
                    rawIO3.writeShortLittleEndian(byteArrayOutputStream2, aesExtraDataRecord.getCompressionMethod().getCode());
                }
                writeRemainingExtraDataRecordsIfPresent(fileHeader2, byteArrayOutputStream2);
                if (bArr5.length > 0) {
                    byteArrayOutputStream2.write(bArr5);
                }
            } catch (Exception e) {
                throw new ZipException(e);
            }
        } else {
            throw new ZipException("input parameters is null, cannot write local file header");
        }
    }
}
