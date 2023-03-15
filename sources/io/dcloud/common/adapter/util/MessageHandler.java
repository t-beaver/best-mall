package io.dcloud.common.adapter.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MessageHandler {
    private static Handler myHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            try {
                Object[] objArr = (Object[]) message.obj;
                if (objArr[0] instanceof IMessages) {
                    ((IMessages) objArr[0]).execute(objArr[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("MessageHandler.handleMessage e=" + e);
            }
        }
    };

    public interface IMessages {
        void execute(Object obj);
    }

    public interface UncheckedCallable {
        void run(WaitableRunnable waitableRunnable);
    }

    public static abstract class WaitableRunnable implements Runnable {
        private Exception mException;
        private String mTimeOutKey = "evalJSSync_time_out";
        private Object mValue;

        protected WaitableRunnable() {
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0011 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void join() {
            /*
                r2 = this;
                monitor-enter(r2)
                r0 = 500(0x1f4, double:2.47E-321)
                r2.wait(r0)     // Catch:{ InterruptedException -> 0x0011 }
                java.lang.Object r0 = r2.mValue     // Catch:{ InterruptedException -> 0x0011 }
                if (r0 != 0) goto L_0x0011
                java.lang.String r0 = r2.mTimeOutKey     // Catch:{ InterruptedException -> 0x0011 }
                r2.mValue = r0     // Catch:{ InterruptedException -> 0x0011 }
                goto L_0x0011
            L_0x000f:
                r0 = move-exception
                goto L_0x0013
            L_0x0011:
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                return
            L_0x0013:
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.MessageHandler.WaitableRunnable.join():void");
        }

        public void callBack(Object obj) {
            this.mValue = obj;
            synchronized (this) {
                notifyAll();
            }
        }

        public Object invoke(Handler handler) {
            if (handler.post(this)) {
                join();
                if (this.mException == null) {
                    return this.mValue;
                }
                throw new RuntimeException(this.mException);
            }
            throw new RuntimeException("Handler.post() returned false");
        }

        /* access modifiers changed from: protected */
        public abstract void onRun(WaitableRunnable waitableRunnable);

        public final void run() {
            try {
                onRun(this);
                this.mException = null;
            } catch (Exception e) {
                this.mValue = null;
                this.mException = e;
            }
        }
    }

    public static boolean hasCallbacks(Runnable runnable) {
        return myHandler.hasCallbacks(runnable);
    }

    public static void post(Runnable runnable) {
        myHandler.post(runnable);
    }

    public static Object postAndWait(final UncheckedCallable uncheckedCallable) {
        return new WaitableRunnable() {
            /* access modifiers changed from: protected */
            public void onRun(WaitableRunnable waitableRunnable) {
                UncheckedCallable.this.run(waitableRunnable);
            }
        }.invoke(myHandler);
    }

    public static void postDelayed(Runnable runnable, long j) {
        myHandler.postDelayed(runnable, j);
    }

    public static void removeCallbacks(Runnable runnable) {
        myHandler.removeCallbacks(runnable);
    }

    public static void removeCallbacksAndMessages() {
    }

    public static void sendMessage(IMessages iMessages, Object obj) {
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = new Object[]{iMessages, obj};
        myHandler.sendMessage(obtain);
    }

    public static void sendMessage(IMessages iMessages, long j, Object obj) {
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = new Object[]{iMessages, obj};
        myHandler.sendMessageDelayed(obtain, j);
    }
}
