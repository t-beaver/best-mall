package net.lingala.zip4j.tasks;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

public abstract class AsyncZipTask<T> {
    private final ExecutorService executorService;
    private final ProgressMonitor progressMonitor;
    private final boolean runInThread;

    public static class AsyncTaskParameters {
        /* access modifiers changed from: private */
        public final ExecutorService executorService;
        /* access modifiers changed from: private */
        public final ProgressMonitor progressMonitor;
        /* access modifiers changed from: private */
        public final boolean runInThread;

        public AsyncTaskParameters(ExecutorService executorService2, boolean z, ProgressMonitor progressMonitor2) {
            this.executorService = executorService2;
            this.runInThread = z;
            this.progressMonitor = progressMonitor2;
        }
    }

    public AsyncZipTask(AsyncTaskParameters asyncTaskParameters) {
        this.progressMonitor = asyncTaskParameters.progressMonitor;
        this.runInThread = asyncTaskParameters.runInThread;
        this.executorService = asyncTaskParameters.executorService;
    }

    private void initProgressMonitor() {
        this.progressMonitor.fullReset();
        this.progressMonitor.setState(ProgressMonitor.State.BUSY);
        this.progressMonitor.setCurrentTask(getTask());
    }

    private void performTaskWithErrorHandling(T t, ProgressMonitor progressMonitor2) throws ZipException {
        try {
            executeTask(t, progressMonitor2);
            progressMonitor2.endProgressMonitor();
        } catch (ZipException e) {
            progressMonitor2.endProgressMonitor(e);
            throw e;
        } catch (Exception e2) {
            progressMonitor2.endProgressMonitor(e2);
            throw new ZipException(e2);
        }
    }

    /* access modifiers changed from: protected */
    public abstract long calculateTotalWork(T t) throws ZipException;

    public void execute(T t) throws ZipException {
        if (!this.runInThread || !ProgressMonitor.State.BUSY.equals(this.progressMonitor.getState())) {
            initProgressMonitor();
            if (this.runInThread) {
                this.progressMonitor.setTotalWork(calculateTotalWork(t));
                this.executorService.execute(new Runnable(t) {
                    public final /* synthetic */ Object f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        AsyncZipTask.this.lambda$execute$0$AsyncZipTask(this.f$1);
                    }
                });
                return;
            }
            performTaskWithErrorHandling(t, this.progressMonitor);
            return;
        }
        throw new ZipException("invalid operation - Zip4j is in busy state");
    }

    /* access modifiers changed from: protected */
    public abstract void executeTask(T t, ProgressMonitor progressMonitor2) throws IOException;

    /* access modifiers changed from: protected */
    public abstract ProgressMonitor.Task getTask();

    public /* synthetic */ void lambda$execute$0$AsyncZipTask(Object obj) {
        try {
            performTaskWithErrorHandling(obj, this.progressMonitor);
        } catch (ZipException unused) {
        } finally {
            this.executorService.shutdown();
        }
    }

    /* access modifiers changed from: protected */
    public void verifyIfTaskIsCancelled() throws ZipException {
        if (this.progressMonitor.isCancelAllTasks()) {
            this.progressMonitor.setResult(ProgressMonitor.Result.CANCELLED);
            this.progressMonitor.setState(ProgressMonitor.State.READY);
            throw new ZipException("Task cancelled", ZipException.Type.TASK_CANCELLED_EXCEPTION);
        }
    }
}
