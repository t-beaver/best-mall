package androidtranscoder;

import android.media.MediaFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidtranscoder.engine.MediaTranscoderEngine;
import androidtranscoder.format.MediaFormatPresets;
import androidtranscoder.format.MediaFormatStrategy;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MediaTranscoder {
    private static final int MAXIMUM_THREAD = 1;
    private static final String TAG = "MediaTranscoder";
    private static volatile MediaTranscoder sMediaTranscoder;
    private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "MediaTranscoder-Worker");
        }
    });

    public interface Listener {
        void onTranscodeCanceled();

        void onTranscodeCompleted();

        void onTranscodeFailed(Exception exc);

        void onTranscodeProgress(double d);
    }

    private MediaTranscoder() {
    }

    public static MediaTranscoder getInstance() {
        if (sMediaTranscoder == null) {
            synchronized (MediaTranscoder.class) {
                if (sMediaTranscoder == null) {
                    sMediaTranscoder = new MediaTranscoder();
                }
            }
        }
        return sMediaTranscoder;
    }

    @Deprecated
    public Future<Void> transcodeVideo(FileDescriptor fileDescriptor, String str, Listener listener) {
        return transcodeVideo(fileDescriptor, str, (MediaFormatStrategy) new MediaFormatStrategy() {
            public MediaFormat createAudioOutputFormat(MediaFormat mediaFormat) {
                return null;
            }

            public MediaFormat createVideoOutputFormat(MediaFormat mediaFormat) {
                return MediaFormatPresets.getExportPreset960x540();
            }
        }, listener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0019 A[SYNTHETIC, Splitter:B:10:0x0019] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.concurrent.Future<java.lang.Void> transcodeVideo(java.lang.String r3, java.lang.String r4, androidtranscoder.format.MediaFormatStrategy r5, final androidtranscoder.MediaTranscoder.Listener r6) throws java.io.IOException {
        /*
            r2 = this;
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0015 }
            r0.<init>(r3)     // Catch:{ IOException -> 0x0015 }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ IOException -> 0x0013 }
            androidtranscoder.MediaTranscoder$3 r1 = new androidtranscoder.MediaTranscoder$3
            r1.<init>(r6, r0)
            java.util.concurrent.Future r3 = r2.transcodeVideo((java.io.FileDescriptor) r3, (java.lang.String) r4, (androidtranscoder.format.MediaFormatStrategy) r5, (androidtranscoder.MediaTranscoder.Listener) r1)
            return r3
        L_0x0013:
            r3 = move-exception
            goto L_0x0017
        L_0x0015:
            r3 = move-exception
            r0 = 0
        L_0x0017:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0025
        L_0x001d:
            r4 = move-exception
            java.lang.String r5 = "MediaTranscoder"
            java.lang.String r6 = "Can't close input stream: "
            android.util.Log.e(r5, r6, r4)
        L_0x0025:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidtranscoder.MediaTranscoder.transcodeVideo(java.lang.String, java.lang.String, androidtranscoder.format.MediaFormatStrategy, androidtranscoder.MediaTranscoder$Listener):java.util.concurrent.Future");
    }

    public Future<Void> transcodeVideo(FileDescriptor fileDescriptor, String str, MediaFormatStrategy mediaFormatStrategy, Listener listener) {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            myLooper = Looper.getMainLooper();
        }
        final Handler handler = new Handler(myLooper);
        AtomicReference atomicReference = new AtomicReference();
        final Listener listener2 = listener;
        final FileDescriptor fileDescriptor2 = fileDescriptor;
        final String str2 = str;
        final MediaFormatStrategy mediaFormatStrategy2 = mediaFormatStrategy;
        final AtomicReference atomicReference2 = atomicReference;
        Future<Void> submit = this.mExecutor.submit(new Callable<Void>() {
            public Void call() throws Exception {
                try {
                    MediaTranscoderEngine mediaTranscoderEngine = new MediaTranscoderEngine();
                    mediaTranscoderEngine.setProgressCallback(new MediaTranscoderEngine.ProgressCallback() {
                        public void onProgress(final double d) {
                            handler.post(new Runnable() {
                                public void run() {
                                    listener2.onTranscodeProgress(d);
                                }
                            });
                        }
                    });
                    mediaTranscoderEngine.setDataSource(fileDescriptor2);
                    mediaTranscoderEngine.transcodeVideo(str2, mediaFormatStrategy2);
                    e = null;
                } catch (IOException e) {
                    e = e;
                    Log.w(MediaTranscoder.TAG, "Transcode failed: input file (fd: " + fileDescriptor2.toString() + ") not found or could not open output file ('" + str2 + "') .", e);
                } catch (InterruptedException e2) {
                    e = e2;
                    Log.i(MediaTranscoder.TAG, "Cancel transcode video file.", e);
                } catch (RuntimeException e3) {
                    e = e3;
                    Log.e(MediaTranscoder.TAG, "Fatal error while transcoding, this might be invalid format or bug in engine or Android.", e);
                }
                handler.post(new Runnable() {
                    public void run() {
                        if (e == null) {
                            listener2.onTranscodeCompleted();
                            return;
                        }
                        Future future = (Future) atomicReference2.get();
                        if (future == null || !future.isCancelled()) {
                            listener2.onTranscodeFailed(e);
                        } else {
                            listener2.onTranscodeCanceled();
                        }
                    }
                });
                if (e == null) {
                    return null;
                }
                throw e;
            }
        });
        atomicReference.set(submit);
        return submit;
    }
}
