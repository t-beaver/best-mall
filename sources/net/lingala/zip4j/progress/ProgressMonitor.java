package net.lingala.zip4j.progress;

public class ProgressMonitor {
    private boolean cancelAllTasks;
    private Task currentTask;
    private Exception exception;
    private String fileName;
    private boolean pause;
    private int percentDone;
    private Result result;
    private State state;
    private long totalWork;
    private long workCompleted;

    public enum Result {
        SUCCESS,
        WORK_IN_PROGRESS,
        ERROR,
        CANCELLED
    }

    public enum State {
        READY,
        BUSY
    }

    public enum Task {
        NONE,
        ADD_ENTRY,
        REMOVE_ENTRY,
        CALCULATE_CRC,
        EXTRACT_ENTRY,
        MERGE_ZIP_FILES,
        SET_COMMENT,
        RENAME_FILE
    }

    public ProgressMonitor() {
        reset();
    }

    private void reset() {
        this.currentTask = Task.NONE;
        this.state = State.READY;
    }

    public void endProgressMonitor() {
        this.result = Result.SUCCESS;
        this.percentDone = 100;
        reset();
    }

    public void fullReset() {
        reset();
        this.fileName = null;
        this.totalWork = 0;
        this.workCompleted = 0;
        this.percentDone = 0;
    }

    public Task getCurrentTask() {
        return this.currentTask;
    }

    public Exception getException() {
        return this.exception;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getPercentDone() {
        return this.percentDone;
    }

    public Result getResult() {
        return this.result;
    }

    public State getState() {
        return this.state;
    }

    public long getTotalWork() {
        return this.totalWork;
    }

    public long getWorkCompleted() {
        return this.workCompleted;
    }

    public boolean isCancelAllTasks() {
        return this.cancelAllTasks;
    }

    public boolean isPause() {
        return this.pause;
    }

    public void setCancelAllTasks(boolean z) {
        this.cancelAllTasks = z;
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
    }

    public void setException(Exception exc) {
        this.exception = exc;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setPause(boolean z) {
        this.pause = z;
    }

    public void setPercentDone(int i) {
        this.percentDone = i;
    }

    public void setResult(Result result2) {
        this.result = result2;
    }

    public void setState(State state2) {
        this.state = state2;
    }

    public void setTotalWork(long j) {
        this.totalWork = j;
    }

    public void updateWorkCompleted(long j) {
        long j2 = this.workCompleted + j;
        this.workCompleted = j2;
        long j3 = this.totalWork;
        if (j3 > 0) {
            int i = (int) ((j2 * 100) / j3);
            this.percentDone = i;
            if (i > 100) {
                this.percentDone = 100;
            }
        }
        while (this.pause) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void endProgressMonitor(Exception exc) {
        this.result = Result.ERROR;
        this.exception = exc;
        reset();
    }
}
