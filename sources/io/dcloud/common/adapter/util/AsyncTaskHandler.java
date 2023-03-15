package io.dcloud.common.adapter.util;

import android.os.AsyncTask;
import io.dcloud.common.util.ThreadPool;

public class AsyncTaskHandler {

    public interface IAsyncTaskListener {
        void onCancel();

        void onExecuteBegin();

        void onExecuteEnd(Object obj);

        Object onExecuting();
    }

    static class MyAsyncTask extends AsyncTask<String[], Integer, Object> {
        IAsyncTaskListener mListener = null;

        MyAsyncTask(IAsyncTaskListener iAsyncTaskListener) {
            this.mListener = iAsyncTaskListener;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            this.mListener.onExecuteEnd(obj);
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            this.mListener.onExecuteBegin();
        }

        /* access modifiers changed from: protected */
        public Object doInBackground(String[]... strArr) {
            return this.mListener.onExecuting();
        }
    }

    public static void executeAsyncTask(IAsyncTaskListener iAsyncTaskListener, String[] strArr) {
        new MyAsyncTask(iAsyncTaskListener).execute(new String[][]{strArr});
    }

    public static void executeThreadTask(final IAsyncTaskListener iAsyncTaskListener) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                IAsyncTaskListener iAsyncTaskListener = IAsyncTaskListener.this;
                if (iAsyncTaskListener != null) {
                    iAsyncTaskListener.onExecuteBegin();
                    final Object onExecuting = IAsyncTaskListener.this.onExecuting();
                    MessageHandler.post(new Runnable() {
                        public void run() {
                            IAsyncTaskListener.this.onExecuteEnd(onExecuting);
                        }
                    });
                }
            }
        }, true);
    }
}
