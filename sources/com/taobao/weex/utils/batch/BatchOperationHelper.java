package com.taobao.weex.utils.batch;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class BatchOperationHelper implements Interceptor {
    private boolean isCollecting = false;
    private BactchExecutor mExecutor;
    /* access modifiers changed from: private */
    public CopyOnWriteArrayList<Runnable> sRegisterTasks = new CopyOnWriteArrayList<>();

    public BatchOperationHelper(BactchExecutor bactchExecutor) {
        this.mExecutor = bactchExecutor;
        bactchExecutor.setInterceptor(this);
        this.isCollecting = true;
    }

    public boolean take(Runnable runnable) {
        if (!this.isCollecting) {
            return false;
        }
        this.sRegisterTasks.add(runnable);
        return true;
    }

    public void flush() {
        this.isCollecting = false;
        this.mExecutor.post(new Runnable() {
            public void run() {
                Iterator it = BatchOperationHelper.this.sRegisterTasks.iterator();
                while (it.hasNext()) {
                    Runnable runnable = (Runnable) it.next();
                    runnable.run();
                    BatchOperationHelper.this.sRegisterTasks.remove(runnable);
                }
            }
        });
        this.mExecutor.setInterceptor((Interceptor) null);
    }
}
