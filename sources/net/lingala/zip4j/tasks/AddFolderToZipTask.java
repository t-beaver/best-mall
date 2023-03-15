package net.lingala.zip4j.tasks;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.util.FileUtils;

public class AddFolderToZipTask extends AbstractAddFileToZipTask<AddFolderToZipTaskParameters> {

    public static class AddFolderToZipTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public final File folderToAdd;
        /* access modifiers changed from: private */
        public final ZipParameters zipParameters;

        public AddFolderToZipTaskParameters(File file, ZipParameters zipParameters2, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.folderToAdd = file;
            this.zipParameters = zipParameters2;
        }
    }

    public AddFolderToZipTask(ZipModel zipModel, char[] cArr, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(zipModel, cArr, headerWriter, asyncTaskParameters);
    }

    private List<File> getFilesToAdd(AddFolderToZipTaskParameters addFolderToZipTaskParameters) throws ZipException {
        List<File> filesInDirectoryRecursive = FileUtils.getFilesInDirectoryRecursive(addFolderToZipTaskParameters.folderToAdd, addFolderToZipTaskParameters.zipParameters.isReadHiddenFiles(), addFolderToZipTaskParameters.zipParameters.isReadHiddenFolders(), addFolderToZipTaskParameters.zipParameters.getExcludeFileFilter());
        if (addFolderToZipTaskParameters.zipParameters.isIncludeRootFolder()) {
            filesInDirectoryRecursive.add(addFolderToZipTaskParameters.folderToAdd);
        }
        return filesInDirectoryRecursive;
    }

    private void setDefaultFolderPath(AddFolderToZipTaskParameters addFolderToZipTaskParameters) throws IOException {
        String str;
        File access$100 = addFolderToZipTaskParameters.folderToAdd;
        if (!addFolderToZipTaskParameters.zipParameters.isIncludeRootFolder()) {
            str = access$100.getCanonicalPath();
        } else if (access$100.getCanonicalFile().getParentFile() == null) {
            str = access$100.getCanonicalPath();
        } else {
            str = access$100.getCanonicalFile().getParentFile().getCanonicalPath();
        }
        addFolderToZipTaskParameters.zipParameters.setDefaultFolderPath(str);
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(AddFolderToZipTaskParameters addFolderToZipTaskParameters) throws ZipException {
        List<File> filesInDirectoryRecursive = FileUtils.getFilesInDirectoryRecursive(addFolderToZipTaskParameters.folderToAdd, addFolderToZipTaskParameters.zipParameters.isReadHiddenFiles(), addFolderToZipTaskParameters.zipParameters.isReadHiddenFolders(), addFolderToZipTaskParameters.zipParameters.getExcludeFileFilter());
        if (addFolderToZipTaskParameters.zipParameters.isIncludeRootFolder()) {
            filesInDirectoryRecursive.add(addFolderToZipTaskParameters.folderToAdd);
        }
        return calculateWorkForFiles(filesInDirectoryRecursive, addFolderToZipTaskParameters.zipParameters);
    }

    /* access modifiers changed from: protected */
    public void executeTask(AddFolderToZipTaskParameters addFolderToZipTaskParameters, ProgressMonitor progressMonitor) throws IOException {
        List<File> filesToAdd = getFilesToAdd(addFolderToZipTaskParameters);
        setDefaultFolderPath(addFolderToZipTaskParameters);
        addFilesToZip(filesToAdd, progressMonitor, addFolderToZipTaskParameters.zipParameters, addFolderToZipTaskParameters.zip4jConfig);
    }
}
